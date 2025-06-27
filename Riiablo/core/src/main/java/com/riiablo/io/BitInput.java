package com.riiablo.io;

import org.apache.commons.lang3.StringUtils;

import static com.riiablo.io.BitConstants.MAX_SAFE_CACHED_BITS;

/**
 * Wraps a {@link ByteInput} to support reading sequences of bits and
 * supporting {@link #align() re-aligning} the byte stream to read sequences of
 * {@link ByteInput bytes}. All read functions will return results in little
 * endian byte order.
 *
 * @see BitInput
 * @see #align()
 */
// TODO: improve placeholder documentation
public class BitInput {
  public static BitInput wrap(byte[] bytes) {
    return ByteInput.wrap(bytes).unalign();
  }

  private static final long[] MASKS = BitConstants.UNSIGNED_MASKS;

  private final ByteInput byteInput;
  private final long numBits;
  private final long maxBit; // logical max bit that can be read
  private final long maxBitByteOffset; // distance from maxBit to maxByte (< {@value Byte#SIZE})
  private long bitsRead;
  private int bitsCached;
  private long cache;

  /**
   * Constructs a BitInput instance at the start of the byteInput with
   * {@code numBits} including all bits within {@code byteInput}.
   */
  BitInput(ByteInput byteInput) {
    this(byteInput, 0, 0L, (long) byteInput.bytesRead() * Byte.SIZE, (long) byteInput.bytesRemaining() * Byte.SIZE);
  }

  /**
   * Constructs a BitInput instance with an initial state. This is typically
   * done when the BitInput is created as the child of another BitInput.
   */
  private BitInput(ByteInput byteInput, int bitsCached, long cache, long bitsRead, long numBits) {
    this.byteInput = byteInput;
    this.bitsCached = bitsCached;
    this.cache = cache;
    this.bitsRead = bitsRead;
    this.numBits = numBits;
    this.maxBit = bitsRead + numBits;
    final long maxByte = (maxBit + Byte.SIZE - 1) / Byte.SIZE;
    this.maxBitByteOffset = (maxByte * Byte.SIZE) - maxBit;
  }

  ByteInput byteInput() {
    return byteInput;
  }

  public int bytesRead() {
    return byteInput.bytesRead();
  }

  public int bytesRemaining() {
    return byteInput.bytesRemaining();
  }

  public int numBytes() {
    return byteInput.numBytes();
  }

  public int byteMark() {
    return byteInput.mark();
  }

  public int bitsCached() {
    return bitsCached;
  }

  public long cache() {
    return cache;
  }

  void clearCache() {
    bitsCached = 0;
    cache = 0L;
  }

  public long bitsRead() {
    return bitsRead;
  }

  public long bitsRemaining() {
    assert (maxBit - bitsRead) == (bitsCached + ((long) bytesRemaining() * Byte.SIZE) - maxBitByteOffset)
        : "actual(" + (maxBit - bitsRead) + ") != expected(" + (bitsCached + ((long) bytesRemaining() * Byte.SIZE) - maxBitByteOffset) + ")";
    return maxBit - bitsRead;
  }

  public long numBits() {
    return numBits;
  }

  /**
   * Indicates whether or not this bit stream's current bit is located on a
   * byte boundary.
   */
  public boolean aligned() {
    assert bitsCached < Byte.SIZE : "bitsCached(" + bitsCached + ") > " + (Byte.SIZE - 1);
    return bitsCached == 0;
  }

  /**
   * Returns a byte aligned view of this bit stream's content. This method
   * should be called when multiple successive byte aligned operations are
   * required. Returning to the bit stream state can be done via
   * {@link ByteInput#unalign()}.
   *
   * @see ByteInput#unalign()
   */
  public ByteInput align() {
    // consume cache if bits remaining
    assert bitsCached < Byte.SIZE : "bitsCached(" + bitsCached + ") > " + (Byte.SIZE - 1);
    if (bitsCached > 0) {
      bitsRead = Math.min(maxBit, bitsRead + bitsCached);
      clearCache();
    }

    assert bitsRead <= maxBit : "bitsRead(" + bitsRead + ") > maxBit(" + maxBit + ")";
    return byteInput;
  }

  /**
   * Reads a slice of this bit stream's sub-region starting at the current
   * bit and increases the bits read by the size of the new slice (= numBits).
   */
  public BitInput readSlice(long numBits) {
    // since this shouldn't go more than 1 level deep, can also generate a new
    // ByteInput with a new BitInput if allowing align
    if (numBits == 0) return ByteInput.emptyByteInput().unalign();
    if (numBits < 0) throw new IllegalArgumentException("numBits(" + numBits + ") < " + 0);
    if (bitsRead + numBits > maxBit) {
      throw new IllegalArgumentException(
          "bitsRead(" + bitsRead + ") + sliceBits(" + numBits + ") > maxBit(" + maxBit + ")");
    }

    assert bitsCached < Byte.SIZE : "bitsCached(" + bitsCached + ") > " + (Byte.SIZE - 1);

    // length should include the last byte that bits belong (round to ceil)
    final long numBytes = (numBits - bitsCached + Byte.SIZE - 1) / Byte.SIZE;
    final ByteInput byteInput = this.byteInput.slice(numBytes);
    final BitInput bitInput = byteInput.bitInput(new BitInput(byteInput, bitsCached, cache, bitsRead, numBits));
    skipBits(numBits);
    return bitInput;
  }

  /**
   * Skips <i>n</i> bits by discarding them.
   */
  public BitInput skipBits(long bits) {
    if (bits < 0) throw new IllegalArgumentException("bits(" + bits + ") < " + 0);
    if (bits == 0) return this;

    // aligns bit stream for multi-byte skipping
    assert bitsCached < Byte.SIZE : "bitsCached(" + bitsCached + ") > " + (Byte.SIZE - 1);
    if (bits >= bitsCached) {
      bits -= bitsCached;
      align();
    }

    // skips multiple bytes
    final long startingBitsRead = bitsRead;
    final long bytes = bits / Byte.SIZE;
    assert bytes <= Integer.MAX_VALUE : "bytes(" + bytes + ") > Integer.MAX_VALUE";
    if (bytes > 0) align().skipBytes((int) bytes);

    // skips overflow bits
    final long overflowBits = (startingBitsRead + bits) - bitsRead;
    // checks single byte, multi-byte and expected max value
    assert bytes != 0 || overflowBits < Byte.SIZE : "overflowBits(" + overflowBits + ") > " + (Byte.SIZE - 1);
    assert bytes == 0 || overflowBits < Short.SIZE : "overflowBits(" + overflowBits + ") > " + (Short.SIZE - 1);
    assert overflowBits < Short.SIZE : "overflowBits(" + overflowBits + ") > " + (Short.SIZE - 1);
    if (overflowBits > 0) _readRaw((int) overflowBits);
    return this;
  }

  long incrementBitsRead(long bits) {
    byteInput.updateMark();
    if ((bitsRead += bits) > maxBit) {
      bitsRead = maxBit;
      throw new EndOfInput();
    }

    return bitsRead;
  }

  long decrementBitsRead(long bits) {
    byteInput.updateMark();
    if ((bitsRead -= bits) < 0) {
      assert false : "bitsRead(" + bitsRead + ") < " + 0;
      bitsRead = 0;
    }

    return bitsRead;
  }

  /**
   * Reads up to {@value BitConstants#MAX_UNSIGNED_BITS} bits as unsigned and
   * casts the result into a {@code long}.
   * <p/>
   * <p>{@code bits} should be in [0, {@value BitConstants##MAX_UNSIGNED_BITS}]
   * <p>Reading {@code 0} bits will always return {@code 0}
   */
  long readUnsigned(int bits) {
    assert bits >= 0 : "bits(" + bits + ") < " + 0;
    assert bits < Long.SIZE : "bits(" + bits + ") > " + (Long.SIZE - 1);
    if (bits <= 0) return 0;
    incrementBitsRead(bits);
    ensureCache(bits);
    return bitsCached < bits
        ? readCacheSafe(bits)
        : readCacheUnsafe(bits);
  }

  /**
   * Ensures {@link #cache} contains at least <i>n</i> bits, up to
   * {@value BitConstants#MAX_SAFE_CACHED_BITS} bits due to overflow.
   */
  private int ensureCache(int bits) {
    assert bits > 0 : "bits(" + bits + ") < " + 1;
    assert bits < Long.SIZE : "bits(" + bits + ") > " + (Long.SIZE - 1);
    while (bitsCached < bits && bitsCached <= MAX_SAFE_CACHED_BITS) {
      final long nextByte = byteInput._read8u();
      cache |= (nextByte << bitsCached);
      bitsCached += Byte.SIZE;
    }

    return bitsCached;
  }

  /**
   * Reads <i>n</i> bits from {@link #cache}, consuming the next byte in the
   * underlying byte stream.
   * <p/>
   * This function asserts that {@link #cache} would have overflowed if
   * <i>n</i> bits were read from the underlying byte stream and thus reads
   * the next byte accounting for this case.
   */
  private long readCacheSafe(int bits) {
    assert bits > 0 : "bits(" + bits + ") < " + 1;
    assert bits < Long.SIZE : "bits(" + bits + ") > " + (Long.SIZE - 1);
    assert bitsCached < bits : "bitsCached(" + bitsCached + ") >= bits(" + bits + ")";
    final int bitsToAddCount = bits - bitsCached;
    final int overflowBits = Byte.SIZE - bitsToAddCount;
    final long nextByte = byteInput._read8u();
    final long bitsToAdd = nextByte & MASKS[bitsToAddCount];
    cache |= (bitsToAdd << bitsCached);
    final long overflow = (nextByte >>> bitsToAddCount) & MASKS[overflowBits];
    final long bitsOut = bitsCached & MASKS[bits];
    cache = overflow;
    bitsCached = overflowBits;
    return bitsOut;
  }

  /**
   * Reads <i>n</i> bits from {@link #cache}.
   * <p/>
   * This function asserts {@link #cache} contains at least <i>n</i> bits.
   */
  private long readCacheUnsafe(int bits) {
    assert bits > 0 : "bits(" + bits + ") < " + 1;
    assert bits < Long.SIZE : "bits(" + bits + ") > " + (Long.SIZE - 1);
    assert bitsCached >= bits : "bitsCached(" + bitsCached + ") < bits(" + bits + ")";
    final long bitsOut = cache & MASKS[bits];
    cache >>>= bits;
    bitsCached -= bits;
    return bitsOut;
  }

  /**
   * Reads up to {@value Long#SIZE} bits and sign extending the result as a
   * {@code long}.
   * <p/>
   * <p>{@code bits} should be in [0, {@value Long#SIZE}].
   * <p>Reading {@code 0} bits will always return {@code 0}.
   */
  long readSigned(int bits) {
    assert bits >= 0 : "bits(" + bits + ") < " + 0;
    assert bits <= Long.SIZE : "bits(" + bits + ") > " + Long.SIZE;
    if (bits <= 0) return 0;
    if (bits == Long.SIZE) return _readRaw(Long.SIZE);
    final int shift = Long.SIZE - bits;
    assert shift > 0 : "shift(" + shift + ") <= " + 0;
    final long value = readUnsigned(bits);
    return value << shift >> shift;
  }

  long _readRaw(int bits) {
    assert bits > 0 : "bits(" + bits + ") <= " + 0;
    assert bits <= Long.SIZE : "bits(" + bits + ") > " + Long.SIZE;
    long lo = readUnsigned(Math.min(Integer.SIZE, bits));
    long hi = readUnsigned(Math.max(bits - Integer.SIZE, 0));
    return (hi << Integer.SIZE) | lo;
  }

  /**
   * Reads up to {@value Long#SIZE} bits as a {@code long}. This method is
   * intended to be used to read raw memory (i.e., flags).
   */
  public long readRaw(int bits) {
    BitConstraints.validate64(bits);
    return _readRaw(bits);
  }

  /**
   * Reads up to {@value BitConstants##MAX_UBYTE_BITS} bits as unsigned and
   * casts the result into a {@code byte}.
   */
  public byte read7u(int bits) {
    BitConstraints.validate7u(bits);
    final byte value = (byte) readUnsigned(bits);
    assert BitUtils.isUnsigned(value);
    return value;
  }

  /**
   * Reads up to {@value BitConstants#MAX_USHORT_BITS} bits as unsigned and
   * casts the result into a {@code short}.
   */
  public short read15u(int bits) {
    BitConstraints.validate15u(bits);
    final short value = (short) readUnsigned(bits);
    assert BitUtils.isUnsigned(value);
    return value;
  }

  /**
   * Reads up to {@value BitConstants#MAX_UINT_BITS} bits as unsigned and casts
   * the result into a {@code int}.
   */
  public int read31u(int bits) {
    BitConstraints.validate31u(bits);
    final int value = (int) readUnsigned(bits);
    assert BitUtils.isUnsigned(value);
    return value;
  }

  /**
   * Reads up to {@value BitConstants#MAX_ULONG_BITS} bits as unsigned and
   * casts the result into a {@code long}.
   */
  public long read63u(int bits) {
    BitConstraints.validate63u(bits);
    final long value = (long) readUnsigned(bits);
    assert BitUtils.isUnsigned(value);
    return value;
  }

  /**
   * Reads {@code 1} bit as a {@code boolean}.
   *
   * @see #read1()
   */
  public boolean readBoolean() {
    return read1() != 0;
  }

  /**
   * Reads {@code 1} bit as a {@code byte}.
   */
  public byte read1() {
    final byte value = read7u(1);
    assert (value & ~1) == 0;
    return value;
  }

  /**
   * Reads up to {@value Byte#SIZE} bits as a sign-extended {@code byte}.
   */
  public byte read8(int bits) {
    BitConstraints.validate8(bits);
    return (byte) readSigned(bits);
  }

  /**
   * Reads up to {@value Short#SIZE} bits as a sign-extended {@code short}.
   */
  public short read16(int bits) {
    BitConstraints.validate16(bits);
    return (short) readSigned(bits);
  }

  /**
   * Reads up to {@value Integer#SIZE} bits as a sign-extended {@code int}.
   */
  public int read32(int bits) {
    BitConstraints.validate32(bits);
    return (int) readSigned(bits);
  }

  /**
   * Reads up to {@value Long#SIZE} bits as a sign-extended {@code long}.
   */
  public long read64(int bits) {
    BitConstraints.validate64(bits);
    return readSigned(bits);
  }

  /**
   * Reads an unsigned byte.
   */
  public short read8u() {
    return read15u(Byte.SIZE);
  }

  /**
   * Reads an unsigned 16-bit short integer.
   */
  public int read16u() {
    return read31u(Short.SIZE);
  }

  /**
   * Reads an unsigned 32-bit integer.
   */
  public long read32u() {
    return read63u(Integer.SIZE);
  }

  /**
   * Reads a byte.
   */
  public byte read8() {
    return read8(Byte.SIZE);
  }

  /**
   * Reads a 16-bit short integer.
   */
  public short read16() {
    return read16(Short.SIZE);
  }

  /**
   * Reads a 32-bit integer.
   */
  public int read32() {
    return read32(Integer.SIZE);
  }

  /**
   * Reads a 64-bit long integer.
   */
  public long read64() {
    return read64(Long.SIZE);
  }

  /**
   * Reads an unsigned byte as a {@code byte}.
   *
   * @throws UnsafeNarrowing if the read value is larger than 7 bits.
   *
   * @see #read8u()
   */
  public byte readSafe8u() {
    try {
      final short value = read8u();
      return BitConstraints.safe8u(byteMark(), value);
    } catch (IndexOutOfBoundsException t) {
      throw new EndOfInput(t);
    }
  }

  /**
   * Reads an unsigned 16-bit short integer as a {@code short}.
   *
   * @throws UnsafeNarrowing if the read value is larger than 15 bits.
   *
   * @see #read16u()
   */
  public short readSafe16u() {
    try {
      final int value = read16u();
      return BitConstraints.safe16u(byteMark(), value);
    } catch (IndexOutOfBoundsException t) {
      throw new EndOfInput(t);
    }
  }

  /**
   * Reads an unsigned 32-bit integer as an {@code int}.
   *
   * @throws UnsafeNarrowing if the read value is larger than 31 bits.
   *
   * @see #read32u()
   */
  public int readSafe32u() {
    try {
      final long value = read32u();
      return BitConstraints.safe32u(byteMark(), value);
    } catch (IndexOutOfBoundsException t) {
      throw new EndOfInput(t);
    }
  }

  /**
   * Reads an unsigned 64-bit long integer as a {@code long}.
   *
   * @throws UnsafeNarrowing if the read value is larger than 63 bits.
   *
   * @see #read64()
   */
  public long readSafe64u() {
    try {
      final long value = read64();
      return BitConstraints.safe64u(byteMark(), value);
    } catch (IndexOutOfBoundsException t) {
      throw new EndOfInput(t);
    }
  }

//  /**
//   * @deprecated unaligned reads not supported!
//   *    use {@code align().readBytes(int)} instead!
//   *    <pre>{@link #align()} {@link ByteInput#readBytes(int)}</pre>
//   */
//  @Deprecated
//  public byte[] readBytes(int len) {
//    throw new UnsupportedOperationException("use align().readBytes(int) instead!");
//  }

//  /**
//   * @deprecated unaligned reads not supported!
//   *    use {@code align().readBytes(byte[])} instead!
//   *    <pre>{@link #align()} {@link ByteInput#readBytes(byte[])}</pre>
//   */
//  @Deprecated
//  public byte[] readBytes(byte[] dst) {
//    throw new UnsupportedOperationException("use align().readBytes(byte[]) instead!");
//  }

//  /**
//   * @deprecated unaligned reads not supported!
//   *    use {@code align().readBytes(byte[], int, int)} instead!
//   *    <pre>{@link #align()} {@link ByteInput#readBytes(byte[], int, int)}</pre>
//   */
//  @Deprecated
//  public byte[] readBytes(byte[] dst, int dstOffset, int len) {
//    throw new UnsupportedOperationException("use align().readBytes(byte[],int,int) instead!");
//  }

  /**
   * Reads <i>n</i> bytes from the bit stream and constructs a string.
   */
  public String readString(int len) {
    return readString(len, Byte.SIZE, false);
  }

  /**
   * Reads up to <i>n</i> characters of size {@code bits} and constructs a
   * string.
   *
   * @param maxLen maximum number of characters to read
   * @param bits size of each character ({@code 7} or {@code 8})
   * @param nullTerminated {@code true} to stop reading at {@code '\0'}
   */
  public String readString(int maxLen, int bits, boolean nullTerminated) {
    if (maxLen < 0) throw new IllegalArgumentException("maxLen(" + maxLen + ") < " + 0);
    BitConstraints.validateAscii(bits);

    if (maxLen == 0) return StringUtils.EMPTY;
    final byte[] dst = new byte[maxLen];
    for (int i = 0; i < maxLen; i++) {
      final byte b = dst[i] = (byte) readUnsigned(bits);
      if (nullTerminated && b == '\0') maxLen = i;
    }

    return new String(dst, 0, maxLen);
  }
}
