package com.riiablo.save;

import io.netty.buffer.ByteBufUtil;
import java.util.Arrays;

import com.badlogic.gdx.utils.Array;

import com.riiablo.CharacterClass;
import com.riiablo.Riiablo;
import com.riiablo.attributes.Attributes;
import com.riiablo.attributes.StatListReader;
import com.riiablo.codec.COF;
import com.riiablo.io.BitInput;
import com.riiablo.io.ByteInput;
import com.riiablo.io.EndOfInput;
import com.riiablo.io.InvalidFormat;
import com.riiablo.io.SignatureMismatch;
import com.riiablo.io.UnsafeNarrowing;
import com.riiablo.item.Item;
import com.riiablo.item.ItemReader;
import com.riiablo.logger.LogManager;
import com.riiablo.logger.Logger;
import com.riiablo.logger.MDC;
import com.riiablo.util.DebugUtils;

public class D2SReader96 {
  private static final Logger log = LogManager.getLogger(D2SReader96.class);

  private static final int VERSION = D2S.VERSION_110;

  private static final byte[] SIGNATURE = D2S.SIGNATURE;
  private static final byte[] QUESTS_SIGNATURE = {0x57, 0x6F, 0x6F, 0x21};
  private static final byte[] WAYPOINTS_SIGNATURE = {0x57, 0x53};
  private static final byte[] WAYPOINTS_DIFF_SIGNATURE = {0x02, 0x01};
  private static final byte[] NPCS_SIGNATURE = {0x01, 0x77};
  private static final byte[] STATS_SIGNATURE = {0x67, 0x66};
  private static final byte[] SKILLS_SIGNATURE = {0x69, 0x66};
  private static final byte[] ITEMS_SIGNATURE = {0x4A, 0x4D};
  private static final byte[] MERC_SIGNATURE = {0x6A, 0x66};
  private static final byte[] GOLEM_SIGNATURE = {0x6B, 0x66};

  static final int HEADER_SIZE = 0x14F;
  static final int MERC_SIZE = 0x10;
  static final int QUESTS_SIZE = QUESTS_SIGNATURE.length + 4 + 2 + (D2S.QuestData.NUM_QUESTFLAGS * D2S.NUM_DIFFS);
  static final int WAYPOINTS_DIFF_SIZE = WAYPOINTS_DIFF_SIGNATURE.length + D2S.WaypointData.NUM_WAYPOINTFLAGS;
  static final int WAYPOINTS_SIZE = WAYPOINTS_SIGNATURE.length + 4 + 2 + (WAYPOINTS_DIFF_SIZE * D2S.NUM_DIFFS);
  static final int NPCS_SIZE = NPCS_SIGNATURE.length + 2 + (D2S.NPCData.NUM_GREETINGS * D2S.NPCData.NUM_INTROS * D2S.NUM_DIFFS);
  static final int SKILLS_SIZE = SKILLS_SIGNATURE.length + (D2S.SkillData.NUM_TREES * D2S.SkillData.NUM_SKILLS);

  D2S readD2S(ByteInput in) {
    log.trace("Reading d2s...");
    log.trace("Validating d2s signature");
    in.readSignature(SIGNATURE);
    try {
      D2S d2s = new D2S();
      d2s.version = in.readSafe32u();
      log.debug("version: {}", d2s.version);
      if (d2s.version != VERSION) {
        throw new InvalidFormat(
            in,
            String.format("Unsupported version %d (%s), expected %d (%s)",
                d2s.version, D2S.getVersionString(d2s.version),
                VERSION, D2S.getVersionString(VERSION)));
      }
      return readHeader(in, d2s);
    } catch (UnsafeNarrowing t) {
      throw new InvalidFormat(in, t);
    }
  }

  private static int[] readInts(ByteInput in, int len) {
    int[] ints = new int[len];
    for (int i = 0; i < len; i++) ints[i] = in.read32();
    return ints;
  }

  static D2S readHeader(ByteInput in, D2S d2s) {
    assert d2s.version == VERSION : "d2s.version(" + d2s.version + ") != VERSION(" + VERSION + ")";
    in = in.readSlice(HEADER_SIZE - SIGNATURE.length - 4);
    d2s.size = in.readSafe32u(); // TODO: validate this field in a test case
    d2s.checksum = in.read32();
    d2s.alternate = in.readSafe32u();
    d2s.name = in.readString(Riiablo.MAX_NAME_LENGTH + 1);
    try {
      MDC.put("d2s.name", d2s.name);
      log.debug("name: \"{}\"", d2s.name);
      log.tracef("checksum: 0x%08X", d2s.checksum);
      d2s.flags = in.read32();
      log.debugf("flags: 0x%08X [%s]", d2s.flags, d2s.getFlagsString());
      d2s.charClass = in.readSafe8u();
      log.debug("charClass: {} ({})", d2s.charClass, DebugUtils.getClassString(d2s.charClass));
      in.skipBytes(2); // unknown
      d2s.level = in.readSafe8u();
      in.skipBytes(4); // unknown
      d2s.timestamp = in.read32();
      in.skipBytes(4); // unknown
      d2s.hotkeys = readInts(in, D2S.NUM_HOTKEYS);
      if (log.debugEnabled()) log.debug("hotkeys: {}", Arrays.toString(d2s.hotkeys));
      d2s.actions = new int[D2S.NUM_ACTIONS][D2S.NUM_BUTTONS];
      for (int i = 0; i < D2S.NUM_ACTIONS; i++) {
        final int[] actions = d2s.actions[i] = readInts(in, D2S.NUM_BUTTONS);
        if (log.debugEnabled()) log.debug("actions[{}]: {}", i, Arrays.toString(actions));
      }
      d2s.composites = in.readBytes(COF.Component.NUM_COMPONENTS);
      if (log.debugEnabled()) log.debug("composites: {}", ByteBufUtil.hexDump(d2s.composites));
      d2s.colors = in.readBytes(COF.Component.NUM_COMPONENTS);
      if (log.debugEnabled()) log.debug("colors: {}", ByteBufUtil.hexDump(d2s.colors));
      d2s.towns = in.readBytes(Riiablo.NUM_DIFFS);
      if (log.debugEnabled()) log.debug("towns: {} ({})", ByteBufUtil.hexDump(d2s.towns), d2s.getTownsString());
      d2s.mapSeed = in.read32();
      log.debugf("mapSeed: 0x%08X", d2s.mapSeed);
      try {
        MDC.put("d2s.section", "merc");
        d2s.merc = readMercData(in);
      } finally {
        MDC.remove("d2s.section");
      }
      in.skipBytes(144); // realm data (unused)
      assert in.bytesRemaining() == 0 : "in.bytesRemaining(" + in.bytesRemaining() + ") > " + 0;
    } finally {
      MDC.remove("d2s.name");
    }
    return d2s;
  }

  static D2S.MercData readMercData(ByteInput in) {
    in = in.readSlice(MERC_SIZE);
    D2S.MercData merc = new D2S.MercData();
    merc.flags = in.read32();
    log.debugf("merc.flags: 0x%08X", merc.flags);
    merc.seed = in.read32();
    log.debugf("merc.seed: 0x%08X", merc.seed);
    merc.name = in.readSafe16u();
    log.debug("merc.name: {}", merc.name);
    merc.type = in.readSafe16u();
    log.debug("merc.type: {}", merc.type);
    merc.experience = in.read32u();
    log.debug("merc.experience: {}", merc.experience);
    assert in.bytesRemaining() == 0 : "in.bytesRemaining(" + in.bytesRemaining() + ") > " + 0;
    return merc;
  }

  private static void recover(ByteInput in, byte[] signature, String tag) {
      try {
        in.skipUntil(signature);
      } catch (EndOfInput t) {
        throw new InvalidFormat(
            in,
            tag + " section " + ByteBufUtil.hexDump(signature) + " is missing!",
            t);
      }
  }

  static D2S readRemaining(D2S d2s, ByteInput in, StatListReader statReader, ItemReader itemReader) {
    try {
      MDC.put("d2s.name", d2s.name);

      MDC.put("d2s.section", "quests");
      d2s.quests = readQuestData(in);

      MDC.put("d2s.section", "waypoints");
      d2s.waypoints = readWaypointData(in);

      MDC.put("d2s.section", "npcs");
      d2s.npcs = readNPCData(in);

      MDC.put("d2s.section", "stats");
      d2s.stats = readStatData(in, statReader);

      recover(in, SKILLS_SIGNATURE, "skills");
      MDC.put("d2s.section", "skills");
      d2s.skills = readSkillData(in);

      recover(in, ITEMS_SIGNATURE, "items");
      MDC.put("d2s.section", "items");
      d2s.items = readItemData(in, itemReader);

      recover(in, ITEMS_SIGNATURE, "corpse");
      MDC.put("d2s.section", "corpse");
      d2s.corpse = readItemData(in, itemReader);

      recover(in, MERC_SIGNATURE, "merc");
      MDC.put("d2s.section", "merc");
      d2s.merc = readMercData(d2s.merc, in, itemReader);

      recover(in, GOLEM_SIGNATURE, "golem");
      MDC.put("d2s.section", "golem");
      d2s.golem = readGolemData(in, itemReader);

      d2s.bodyRead = true;
    } finally {
      MDC.remove("d2s.section");
      MDC.remove("d2s.name");
    }
    return d2s;
  }

  static D2S.QuestData readQuestData(ByteInput in) {
    log.trace("Validating quests signature");
    in.readSignature(QUESTS_SIGNATURE);
    D2S.QuestData quests = new D2S.QuestData();
    final int version = in.readSafe32u();
    log.trace("quests.version: {}", version);
    if (version != 6) throw new InvalidFormat(in, "quests.version(" + version + ") != " + 6);
    final short size = in.readSafe16u();
    assert size == QUESTS_SIZE : "quests.size(" + size + ") != QUESTS_SIZE(" + QUESTS_SIZE + ")";
    in = in.readSlice(size - QUESTS_SIGNATURE.length - 4 - 2);
    final byte[][] flags = quests.flags = new byte[D2S.NUM_DIFFS][];
    for (int i = 0; i < D2S.NUM_DIFFS; i++) {
      flags[i] = in.readBytes(D2S.QuestData.NUM_QUESTFLAGS);
      if (log.debugEnabled()) {
        log.debugf("quests.flags[%.4s]: %s",
            DebugUtils.getDifficultyString(i),
            ByteBufUtil.hexDump(flags[i]));
      }
    }
    assert in.bytesRemaining() == 0 : "in.bytesRemaining(" + in.bytesRemaining() + ") > " + 0;
    return quests;
  }

  static D2S.WaypointData readWaypointData(ByteInput in) {
    log.trace("Validating waypoints signature");
    in.readSignature(WAYPOINTS_SIGNATURE);
    D2S.WaypointData waypoints = new D2S.WaypointData();
    final int version = in.readSafe32u();
    log.trace("waypoints.version: {}", version);
    if (version != 1) throw new InvalidFormat(in, "waypoints.version(" + version + ") != " + 1);
    final short size = in.readSafe16u();
    assert size == WAYPOINTS_SIZE : "waypoints.size(" + size + ") != WAYPOINTS_SIZE(" + WAYPOINTS_SIZE + ")";
    in = in.readSlice(size - WAYPOINTS_SIGNATURE.length - 4 - 2);
    final byte[][] flags = waypoints.flags = new byte[D2S.NUM_DIFFS][];
    for (int i = 0; i < D2S.NUM_DIFFS; i++) {
      flags[i] = readWaypointFlags(in);
      if (log.debugEnabled()) {
        log.debugf("waypoints.flags[%.4s]: %s",
            DebugUtils.getDifficultyString(i),
            ByteBufUtil.hexDump(flags[i]));
      }
    }
    assert in.bytesRemaining() == 0 : "in.bytesRemaining(" + in.bytesRemaining() + ") > " + 0;
    return waypoints;
  }

  static byte[] readWaypointFlags(ByteInput in) {
    in = in.readSlice(WAYPOINTS_DIFF_SIZE);
    log.trace("Validating waypoints.diff signature");
    in.readSignature(WAYPOINTS_DIFF_SIGNATURE);
    final byte[] flags = in.readBytes(D2S.WaypointData.NUM_WAYPOINTFLAGS);
    assert in.bytesRemaining() == 0 : "in.bytesRemaining(" + in.bytesRemaining() + ") > " + 0;
    return flags;
  }

  static D2S.NPCData readNPCData(ByteInput in) {
    log.trace("Validating npcs signature");
    in.readSignature(NPCS_SIGNATURE);
    D2S.NPCData npcs = new D2S.NPCData();
    final short size = in.readSafe16u();
    assert size == NPCS_SIZE : "npcs.size(" + size + ") != NPCS_SIZE(" + NPCS_SIZE + ")";
    in = in.readSlice(size - NPCS_SIGNATURE.length - 2);
    final byte[][][] flags = npcs.flags = new byte[D2S.NPCData.NUM_GREETINGS][D2S.NUM_DIFFS][];
    for (int i = 0; i < D2S.NPCData.NUM_GREETINGS; i++) {
      for (int j = 0; j < D2S.NUM_DIFFS; j++) {
        flags[i][j] = in.readBytes(D2S.NPCData.NUM_INTROS);
        if (log.debugEnabled()) {
          log.debugf("npcs.flags[%s][%.4s]: %s",
              D2S.NPCData.getGreetingString(i),
              DebugUtils.getDifficultyString(j),
              ByteBufUtil.hexDump(flags[i][j]));
        }
      }
    }
    assert in.bytesRemaining() == 0 : "in.bytesRemaining(" + in.bytesRemaining() + ") > " + 0;
    return npcs;
  }

  static D2S.StatData readStatData(ByteInput in, StatListReader statReader) {
    log.trace("Validating stats signature");
    in.readSignature(STATS_SIGNATURE);
    D2S.StatData stats = new D2S.StatData();
    final Attributes attrs = stats.attrs = Attributes.obtainLarge();
    BitInput bits = in.unalign();
    statReader.read(attrs.base(), bits, true);
    bits.align();
    return stats;
  }

  static D2S.SkillData readSkillData(ByteInput in) {
    log.trace("Validating skills signature");
    in.readSignature(SKILLS_SIGNATURE);
    in = in.readSlice(SKILLS_SIZE - SKILLS_SIGNATURE.length);
    D2S.SkillData skills = new D2S.SkillData();
    skills.skills = in.readBytes(D2S.SkillData.NUM_TREES * D2S.SkillData.NUM_SKILLS);
    if (log.debugEnabled()) {
      for (int i = 0, j = 0; i < D2S.SkillData.NUM_TREES; i++, j += D2S.SkillData.NUM_SKILLS) {
        log.debugf("skills.skills[%d] = %s",
            i,
            ByteBufUtil.hexDump(skills.skills, j, D2S.SkillData.NUM_SKILLS));
      }
    }
    assert in.bytesRemaining() == 0 : "in.bytesRemaining(" + in.bytesRemaining() + ") > " + 0;
    return skills;
  }

  static D2S.ItemData readItemData(ByteInput in, ItemReader itemReader) {
    log.trace("Validating items signature");
    in.readSignature(ITEMS_SIGNATURE);
    D2S.ItemData items = new D2S.ItemData();
    final short size = in.readSafe16u();
    Array<Item> itemList = items.items = new Array<>(size);
    log.trace("Reading {} items...", size);
    int errors = 0;
    for (int i = 0; i < size; i++) {
      try {
        MDC.put("item", i);
        final Item item = itemReader.readItem(in);
        log.debug("item: {}", item);
        itemList.add(item);
      } catch (SignatureMismatch t) {
        log.warn(t.getMessage(), t);
        i--;
        itemReader.skipUntil(in.realign());
      } catch (InvalidFormat t) {
        log.warn(t.getMessage(), t);
        errors++;
        itemReader.skipUntil(in.realign());
      } finally {
        MDC.remove("item");
      }
    }
    assert itemList.size == size : "itemList.size(" + itemList.size + ") != size(" + size + ")";
    if (errors > 0) {
      log.warn("{} items could not be loaded due to formatting errors.", errors);
    }
    return items;
  }

  static D2S.MercData readMercData(D2S.MercData merc, ByteInput in, ItemReader itemReader) {
    log.trace("Validating merc signature");
    in.readSignature(MERC_SIGNATURE);
    if (merc.seed == 0) return merc;
    // fixme: this is throwing end of stream exception -- something within ByteInput or BitInput isn't back tracking bytes read
    merc.items = readItemData(in, itemReader);
    return merc;
  }

  static D2S.GolemData readGolemData(ByteInput in, ItemReader itemReader) {
    log.trace("Validating golem signature");
    in.readSignature(GOLEM_SIGNATURE);
    D2S.GolemData golem = new D2S.GolemData();
    final BitInput bits = in.unalign();
    golem.exists = bits.readBoolean();
    log.trace("golem.exists: {}", golem.exists);
    if (golem.exists) {
      log.trace("Reading golem item...");
      golem.item = itemReader.readItem(bits.align());
    }
    return golem;
  }

  static CharData copyTo(D2S d2s, CharData data) {
//    readRemaining(d2s, in, itemReader);
    data.softReset();
    data.name = d2s.name;
    data.charClass = d2s.charClass;
    data.classId = CharacterClass.get(d2s.charClass);
    data.flags = d2s.flags;
    data.level = d2s.level;
    System.arraycopy(d2s.hotkeys, 0, data.hotkeys, 0, D2S.NUM_HOTKEYS);
    for (int i = 0, s = D2S.NUM_ACTIONS; i < s; i++) System.arraycopy(d2s.actions[i], 0, data.actions[i], 0, D2S.NUM_BUTTONS);
    System.arraycopy(d2s.towns, 0, data.towns, 0, D2S.NUM_DIFFS);
    data.mapSeed = d2s.mapSeed;
//    System.arraycopy(d2s.realmData, 0, data.realmData, 0, d2s.realmData.length);

    data.mercData.flags = d2s.merc.flags;
    data.mercData.seed  = d2s.merc.seed;
    data.mercData.name  = d2s.merc.name;
    data.mercData.type  = d2s.merc.type;
    data.mercData.xp    = d2s.merc.experience;
    data.mercData.itemData.clear();
    if (d2s.merc.seed != 0) data.mercData.itemData.addAll(d2s.merc.items.items);

    BitInput bits;
    for (int i = 0, i0 = Riiablo.NUM_DIFFS; i < i0; i++) {
      bits = BitInput.wrap(d2s.quests.flags[i]);
      for (int q = 0, q0 = 8; q < q0; q++) data.questData[i][Riiablo.ACT1][q] = (short) bits.readRaw(16);
      for (int q = 0, q0 = 8; q < q0; q++) data.questData[i][Riiablo.ACT2][q] = (short) bits.readRaw(16);
      for (int q = 0, q0 = 8; q < q0; q++) data.questData[i][Riiablo.ACT3][q] = (short) bits.readRaw(16);
      for (int q = 0, q0 = 8; q < q0; q++) data.questData[i][Riiablo.ACT4][q] = (short) bits.readRaw(16);
      for (int q = 0, q0 = 8; q < q0; q++) data.questData[i][Riiablo.ACT5][q] = (short) bits.readRaw(16);

      bits = BitInput.wrap(d2s.waypoints.flags[i]);
      data.waypointData[i][Riiablo.ACT1] = (int) bits.readRaw(9);
      data.waypointData[i][Riiablo.ACT2] = (int) bits.readRaw(9);
      data.waypointData[i][Riiablo.ACT3] = (int) bits.readRaw(9);
      data.waypointData[i][Riiablo.ACT4] = (int) bits.readRaw(3);
      data.waypointData[i][Riiablo.ACT5] = (int) bits.readRaw(9);

      bits = BitInput.wrap(d2s.npcs.flags[D2S.NPCData.GREETING_INTRO][i]);
      data.npcIntroData[i] = bits.readRaw(64);
      bits = BitInput.wrap(d2s.npcs.flags[D2S.NPCData.GREETING_RETURN][i]);
      data.npcReturnData[i] = bits.readRaw(64);
    }

    data.statData.base().addAll(d2s.stats.attrs.base());

    CharacterClass classId = data.classId;
    for (int spellId = classId.firstSpell, s = classId.lastSpell, i = 0; spellId < s; spellId++, i++) {
      data.skillData.put(spellId, d2s.skills.skills[i]);
    }

    data.itemData.clear();
    data.itemData.addAll(d2s.items.items);
    data.itemData.charStats = classId.entry();
    data.itemData.alternate = d2s.alternate;

    data.golemItemData = d2s.golem.item;

    return data;
  }

  // TODO: Tuples should be specific to versions -- versions may have different data types, fields, etc
//  static class D2SData extends D2S {}
}
