package com.riiablo.item;


import com.badlogic.gdx.utils.Array;

import com.riiablo.Riiablo;
import com.riiablo.attributes.GemGenerator;
import com.riiablo.attributes.PropertiesGenerator;
import com.riiablo.attributes.Stat;
import com.riiablo.attributes.StatListFlags;
import com.riiablo.attributes.StatListReader;
import com.riiablo.io.BitInput;
import com.riiablo.io.ByteInput;
import com.riiablo.logger.LogManager;
import com.riiablo.logger.Logger;
import com.riiablo.logger.MDC;

public class ItemReader {
  private static final Logger log = LogManager.getLogger(ItemReader.class);

  private static final byte[] SIGNATURE = {0x4A, 0x4D};

  protected StatListReader statListReader = new StatListReader(); // TODO: inject
  protected PropertiesGenerator propertiesGenerator = new PropertiesGenerator(); // TODO: inject
  protected GemGenerator gems = new GemGenerator(propertiesGenerator); // TODO: inject

  public void skipUntil(ByteInput in) {
    in.skipUntil(SIGNATURE);
  }

  @SuppressWarnings("deprecation")
  public Item readItem(ByteInput in) {
    final int startOffset = in.bytesRead(); /** @see Item#data */
    Item item = readSingleItem(in);
    if (item.socketsFilled > 0) log.trace("Reading {} sockets...", item.socketsFilled);
    for (int i = 0; i < item.socketsFilled; i++) {
      try {
        MDC.put("socket", i);
        in.skipUntil(SIGNATURE);
        item.sockets.add(readSingleItem(in));
      } finally {
        MDC.remove("socket");
      }
    }
    final int endOffset = in.bytesRead();
    final int itemSize = endOffset - startOffset;  /** @see Item#data */
    item.data = in.duplicate(startOffset, itemSize);  /** @see Item#data */
    if (log.traceEnabled()) {
      log.trace("size: {} (0x{}) (+{} .. +{})",
          itemSize,
          Integer.toHexString(itemSize),
          Integer.toHexString(startOffset),
          Integer.toHexString(endOffset));
    }
    return item;
  }

  @SuppressWarnings("deprecation")
  Item readSingleItem(ByteInput in) {
    final int itemOffset = in.bytesRead();  /** @see Item#data */
    log.trace("Reading item...");
    log.trace("Validating item signature");
    in.readSignature(SIGNATURE);
    Item item = new Item();
    item.reset();
    item.flags = in.read32();
    log.tracef("flags: 0x%08X [%s]", item.flags, item.getFlagsString());
    item.version = in.readSafe8u();
    log.trace("version: {}", item.version);
    final BitInput bits = in.unalign();
    bits.skipBits(2); // Unknown use -- safe to skip
    item.location = Location.valueOf(bits.read7u(3));
    item.bodyLoc = BodyLoc.valueOf(bits.read7u(4));
    item.gridX = bits.read7u(4);
    item.gridY = bits.read7u(4);
    item.storeLoc = StoreLoc.valueOf(bits.read7u(3));

    if ((item.flags & Item.ITEMFLAG_BODYPART) == Item.ITEMFLAG_BODYPART) {
      int charClass = bits.read7u(3);
      int charLevel = bits.read7u(7);
      String charName = bits.readString(Riiablo.MAX_NAME_LENGTH + 1, 7, true);
      item.setEar(charClass, charLevel, charName);
    } else {
      item.setBase(bits.readString(4).trim());
      item.socketsFilled = bits.read7u(3);
    }

    log.trace("code: {} ({})", item.code, item.base.name);
    if ((item.flags & Item.ITEMFLAG_COMPACT) == Item.ITEMFLAG_COMPACT) {
      readCompact(item);
    } else {
      readStandard(bits, item);
    }

    bits.align();
    final int itemSize = in.bytesRead() - itemOffset;  /** @see Item#data */
    item.data = in.duplicate(itemOffset, itemSize);  /** @see Item#data */
    return item;
  }

  private void readCompact(Item item) {
    if (item.type.is(Type.GEM) || item.type.is(Type.RUNE)) {
      gems.set(item.attrs, item.code);
    } else {
      assert item.attrs.isEmpty();
    }
  }

  private void readStandard(BitInput bits, Item item) {
    item.id = (int) bits.readRaw(32);
    log.tracef("id: 0x%08X", item.id);
    item.ilvl = bits.read7u(7);
    item.quality = Quality.valueOf(bits.read7u(4));
    item.pictureId = bits.readBoolean() ? bits.read7u(3) : Item.NO_PICTURE_ID;
    item.classOnly = bits.readBoolean() ? bits.read15u(11) : Item.NO_CLASS_ONLY;
    readQualityData(bits, item);

    int listFlags = StatListFlags.FLAG_MAGIC;
    if (readRunewordData(bits, item)) listFlags |= StatListFlags.FLAG_RUNE;

    item.inscription = (item.flags & Item.ITEMFLAG_INSCRIBED) == Item.ITEMFLAG_INSCRIBED
        ? bits.readString(Riiablo.MAX_NAME_LENGTH + 1, 7, true)
        : null;

    bits.skipBits(1); // TODO: Unknown, this usually is 0, but is 1 on a Tome of Identify.  (It's still 0 on a Tome of Townportal.)

    readArmorClass(bits, item);
    readDurability(bits, item);
    readSockets(bits, item);
    readBook(bits, item);
    readQuantity(bits, item);

    item.aggFlags = listFlags |= (readSetFlags(bits, item) << StatListFlags.ITEM_SET_LIST);
    statListReader.read(item.attrs.list(), bits, listFlags);
  }

  private static boolean readQualityData(BitInput bits, Item item) {
    log.trace("quality: {}", item.quality);
    switch (item.quality) {
      case LOW:
      case HIGH:
        item.qualityId = bits.read31u(3);
        log.trace("qualityId: {}", item.qualityId);
        return true;

      case NORMAL:
        item.qualityId = 0;
        return true;

      case SET:
        item.qualityId = bits.read31u(Item.SET_ID_SIZE);
        log.trace("qualityId: {}", item.qualityId);
        item.qualityData = Riiablo.files.SetItems.get(item.qualityId);
        log.trace("qualityData: {}", item.qualityData);
        if (item.qualityId == (1 << Item.SET_ID_SIZE) - 1) {
          log.error("Unknown set item id: {}", item.qualityId);
          // This is unexpected -- all set items should reference a set id
          // TODO: throw item format exception
        }
        return true;

      case UNIQUE:
        item.qualityId = bits.read31u(Item.UNIQUE_ID_SIZE);
        log.trace("qualityId: {}", item.qualityId);
        item.qualityData = Riiablo.files.UniqueItems.get(item.qualityId);
        log.trace("qualityData: {}", item.qualityData);
        if (item.qualityId == (1 << Item.UNIQUE_ID_SIZE) - 1) {
          log.warn("Unknown unique item id: {}", item.qualityId);
          // This is expected for hdm and possibly others
          // TODO: ensure item can be gracefully handled, else throw item format exception
        }
        return true;

      case MAGIC:
        item.qualityId = bits.read31u(2 * Item.MAGIC_AFFIX_SIZE); // 11 for prefix, 11 for suffix
        log.trace("qualityId: {}", item.qualityId);
        return true;

      case RARE:
      case CRAFTED:
        item.qualityId = bits.read31u(2 * Item.RARE_AFFIX_SIZE); // 8 for prefix, 8 for suffix
        log.trace("qualityId: {}", item.qualityId);
        item.qualityData = new RareQualityData(bits);
        log.trace("qualityData: {}", item.qualityData);
        return true;

      default:
        item.qualityId = 0;
        return false;
    }
  }

  private static int readSetFlags(BitInput bits, Item item) {
    return item.quality == Quality.SET ? bits.read7u(5) : 0;
  }

  private static boolean readRunewordData(BitInput bits, Item item) {
    boolean hasRunewordData = (item.flags & Item.ITEMFLAG_RUNEWORD) == Item.ITEMFLAG_RUNEWORD;
    item.runewordData = hasRunewordData ? (short) bits.readRaw(16) : 0;
    return hasRunewordData;
  }

  private boolean readArmorClass(BitInput bits, Item item) {
    boolean hasAC = item.type.is(Type.ARMO);
    if (hasAC) statListReader.read(item.attrs.base(), Stat.armorclass, bits, false);
    return hasAC;
  }

  private boolean readDurability(BitInput bits, Item item) {
    boolean hasDurability = item.type.is(Type.ARMO) || item.type.is(Type.WEAP);
    if (hasDurability) {
      int maxdurability = statListReader.read(item.attrs.base(), Stat.maxdurability, bits, false).asInt();
      if (maxdurability > 0) statListReader.read(item.attrs.base(), Stat.durability, bits, false);
    }
    return hasDurability;
  }

  private boolean readSockets(BitInput bits, Item item) {
    boolean hasSockets = (item.flags & Item.ITEMFLAG_SOCKETED) == Item.ITEMFLAG_SOCKETED;
    if (hasSockets) {
      int item_numsockets = statListReader.read(item.attrs.base(), Stat.item_numsockets, bits, false).asInt();
      item.sockets = new Array<>(item_numsockets);
    }
    return hasSockets;
  }

  private static boolean readBook(BitInput bits, Item item) {
    boolean isBook = item.type.is(Type.BOOK);
    if (isBook) bits.skipBits(5); // TODO: Appears to be 0 for tbk and 1 for ibk
    return isBook;
  }

  private static boolean readQuantity(BitInput bits, Item item) {
    boolean hasQuantity = item.base.stackable;
    if (hasQuantity) {
      int quantity = bits.read15u(9);
      item.attrs.base().put(Stat.quantity, quantity);
    }
    return hasQuantity;
  }
}
