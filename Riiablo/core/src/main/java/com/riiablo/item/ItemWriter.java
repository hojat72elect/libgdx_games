package com.riiablo.item;

import io.netty.buffer.ByteBufUtil;
import org.apache.commons.lang3.StringUtils;

import com.riiablo.Riiablo;
import com.riiablo.attributes.Stat;
import com.riiablo.attributes.StatListFlags;
import com.riiablo.attributes.StatListWriter;
import com.riiablo.attributes.StatRef;
import com.riiablo.codec.excel.SetItems;
import com.riiablo.codec.excel.Sets;
import com.riiablo.io.BitInput;
import com.riiablo.io.BitOutput;
import com.riiablo.io.ByteOutput;
import com.riiablo.logger.LogManager;
import com.riiablo.logger.Logger;
import com.riiablo.logger.MDC;

public class ItemWriter {
  private static final Logger log = LogManager.getLogger(ItemWriter.class);

  private static final byte[] SIGNATURE = {0x4A, 0x4D};

  protected StatListWriter statListWriter = new StatListWriter(); // TODO: inject

  public ByteOutput writeItem(Item item, ByteOutput out) {
    final int startOffset = out.bytesWritten();
    writeSingleItem(item, out);
    if (item.socketsFilled > 0) log.trace("Writing {} sockets...", item.socketsFilled);
    for (int i = 0; i < item.socketsFilled; i++) {
      try {
        MDC.put("socket", i);
        Item socket = item.sockets.get(i);
        writeSingleItem(socket, out);
      } finally {
        MDC.remove("socket");
      }
    }
    if (log.debugEnabled()) {
      final int endOffset = out.bytesWritten();
      final int itemSize = endOffset - startOffset;
      log.debug("size: {} (0x{}) (+{} .. +{})",
          itemSize,
          Integer.toHexString(itemSize),
          Integer.toHexString(startOffset),
          Integer.toHexString(endOffset));
      if (log.traceEnabled()) {
        log.tracef("bytes: %n%s", ByteBufUtil.prettyHexDump(out.buffer()));
      } else {
        log.debug("bytes: {}", ByteBufUtil.hexDump(out.buffer()));
      }
    }
    return out;
  }

  ByteOutput writeSingleItem(Item item, ByteOutput out) {
    log.trace("Writing item...");
    out.writeBytes(SIGNATURE);
    out.write32(item.flags & Item.ITEMFLAG_SAVE_MASK);
    out.write8(item.version);
    final BitOutput bits = out.unalign();
    bits.skipBits(2); // Unknown
    bits.write7u(item.location == null ? 0 : item.location.ordinal(), 3); // FIXME: workaround for #99
    bits.write7u(item.bodyLoc.ordinal(), 4);
    bits.write7u(item.gridX, 4);
    bits.write7u(item.gridY, 4);
    bits.write7u(item.storeLoc.ordinal(), 3);

    if ((item.flags & Item.ITEMFLAG_BODYPART) == Item.ITEMFLAG_BODYPART) {
      throw new UnsupportedOperationException("Cannot write body part items!");
    } else {
      assert item.code.length() <= 4 : "item.code.length(" + item.code.length() + ") > " + 4;
      bits.writeString(
          StringUtils.rightPad(item.code, 4, ' '),
          Byte.SIZE);
      bits.write7u(item.socketsFilled, 3);
    }

    if ((item.flags & Item.ITEMFLAG_COMPACT) == Item.ITEMFLAG_COMPACT) {
      writeCompact(item, bits);
    } else {
      writeStandard(item, bits);
    }

    return bits.align();
  }

  private void writeCompact(Item item, BitOutput bits) {
    // no-op
  }

  private void writeStandard(Item item, BitOutput bits) {
    bits.write32(item.id);
    bits.write7u(item.ilvl, 7);
    bits.write7u(item.quality.ordinal(), 4);
    boolean hasPictureId = item.pictureId != Item.NO_PICTURE_ID;
    bits.writeBoolean(hasPictureId);
    if (hasPictureId) bits.write7u(item.pictureId, 3);
    boolean hasClassOnly = item.classOnly != Item.NO_CLASS_ONLY;
    bits.writeBoolean(hasClassOnly);
    if (hasClassOnly) bits.write15u(item.classOnly, 11);
    writeQualityData(item, bits);

    int listFlags = StatListFlags.FLAG_MAGIC;
    if (writeRunewordData(item, bits)) listFlags |= StatListFlags.FLAG_RUNE;

    if ((item.flags & Item.ITEMFLAG_INSCRIBED) == Item.ITEMFLAG_INSCRIBED) {
      bits.writeString(item.inscription, 7, true);
    }

    /** @see ItemReader#readStandard(BitInput, Item) */
    if (item.type.is(Type.BOOK)) {
      bits.writeBoolean(item.code.equalsIgnoreCase("ibk"));
    } else {
      bits.skipBits(1);
    }

    writeArmorClass(item, bits);
    writeDurability(item, bits);
    writeSockets(item, bits);
    writeBook(item, bits);
    writeQuantity(item, bits);

    listFlags |= (writeSetFlags(item, bits) << StatListFlags.ITEM_SET_LIST);
    statListWriter.write(item.attrs.list(), bits, listFlags);
  }

  private static void writeQualityData(Item item, BitOutput bits) {
    switch (item.quality) {
      case LOW:
      case HIGH:
        bits.write31u(item.qualityId, 3);
        break;

      case NORMAL:
        break;

      case SET:
        bits.write31u(item.qualityId, Item.SET_ID_SIZE);
        break;

      case UNIQUE:
        bits.write31u(item.qualityId, Item.UNIQUE_ID_SIZE);
        break;

      case MAGIC:
        bits.write31u(item.qualityId, 2 * Item.MAGIC_AFFIX_SIZE);
        break;

      case RARE:
      case CRAFTED:
        bits.write31u(item.qualityId, 2 * Item.RARE_AFFIX_SIZE);
        ((RareQualityData) item.qualityData).write(bits);
        break;

      default:
        throw new UnsupportedOperationException("Cannot write item quality! " + item.quality);
    }
  }

  private static boolean writeRunewordData(Item item, BitOutput bits) {
    boolean hasRunewordData = (item.flags & Item.ITEMFLAG_RUNEWORD) == Item.ITEMFLAG_RUNEWORD;
    if (hasRunewordData) bits.write16((short) item.runewordData);
    return hasRunewordData;
  }

  private boolean writeArmorClass(Item item, BitOutput bits) {
    boolean hasAC = item.type.is(Type.ARMO);
    if (hasAC) {
      statListWriter.write(item.attrs.base(), Stat.armorclass, bits, false);
    }
    return hasAC;
  }

  private boolean writeDurability(Item item, BitOutput bits) {
    boolean hasDurability = item.type.is(Type.ARMO) || item.type.is(Type.WEAP);
    if (hasDurability) {
      int maxdurability = statListWriter.write(item.attrs.base(), Stat.maxdurability, bits, false).asInt();
      if (maxdurability > 0) statListWriter.write(item.attrs.base(), Stat.durability, bits, false);
    }
    return hasDurability;
  }

  private boolean writeSockets(Item item, BitOutput bits) {
    boolean hasSockets = (item.flags & Item.ITEMFLAG_SOCKETED) == Item.ITEMFLAG_SOCKETED;
    if (hasSockets) {
      statListWriter.write(item.attrs.base(), Stat.item_numsockets, bits, false);
    }
    return hasSockets;
  }

  private static boolean writeBook(Item item, BitOutput bits) {
    boolean isBook = item.type.is(Type.BOOK);
    if (isBook) bits.skipBits(5); /** @see ItemReader#readBook(BitInput, Item) */
    return isBook;
  }

  private static boolean writeQuantity(Item item, BitOutput bits) {
    boolean hasQuantity = item.base.stackable;
    if (hasQuantity) {
      StatRef quantity = item.attrs.base().get(Stat.quantity);
      bits.write15u(quantity.asInt(), 9);
    }
    return hasQuantity;
  }

  private static int writeSetFlags(Item item, BitOutput bits) {
    if (item.quality != Quality.SET) return 0;
    SetItems.Entry setItem = Riiablo.files.SetItems.get(item.qualityId);
    Sets.Entry set = setItem.getSet();
    final int numSetItems = set.getItems().size;
    final int flags = (1 << (numSetItems - 1)) - 1;
    bits.write7u(flags, 5);
    return flags;
  }
}
