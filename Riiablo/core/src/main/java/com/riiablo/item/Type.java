package com.riiablo.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Bits;

import com.riiablo.Riiablo;
import com.riiablo.codec.excel.ItemTypes;

public class Type extends Bits {
  private static final String TAG = "Type";

  private static final Type[] TYPES;
  static {
    TYPES = new Type[128];
    for (int i = 0, size = Riiablo.files.ItemTypes.size(); i < size; i++) {
      build(TYPES, Riiablo.files.ItemTypes.get(i).Code);
    }
  }

  private static Type build(Type[] types, String type) {
    int id = Riiablo.files.ItemTypes.index(type);
    if (types[id] != null) {
      return types[id];
    }

    Type t = types[id] = new Type();
    t.set(id);

    ItemTypes.Entry entry = Riiablo.files.ItemTypes.get(id);
    for (String equiv : entry.Equiv) {
      if (equiv.isEmpty()) break;
      t.or(build(types, equiv));
    }

    return t;
  }

  public static Type get(ItemTypes.Entry type) {
    return get(type.Code);
  }

  public static Type get(String type) {
    int id = Riiablo.files.ItemTypes.index(type);
    return TYPES[id];
  }

  Type() {
    super(0x7F);
  }

  public boolean is(int index) {
    return get(index);
  }

  @Override
  public String toString() {
    return getOrdinalString();
  }

  public String getOrdinalString() {
    StringBuilder builder = new StringBuilder();
    for (int i = -1; (i = nextSetBit(i + 1)) != -1; ) {
      builder.append(Riiablo.files.ItemTypes.get(i).Code).append(',');
    }
    if (builder.length() > 0) builder.setLength(builder.length() - 1);
    return builder.toString();
  }

  public String getHexString() {
    // shorts because blocks of 4 is easier to read at this scale
    short[] words = new short[(numBits() + Short.SIZE - 1) / Short.SIZE];
    for (int i = -1; (i = nextSetBit(i + 1)) != -1; ) {
      int w0 = i / Short.SIZE;
      words[w0] |= (1 << (i >>> (w0 * Short.SIZE)));
    }
    StringBuilder builder = new StringBuilder();
    for (int i = words.length - 1; i >= 0; i--) {
      builder.append(String.format("%04x", words[i])).append(' ');
    }
    if (builder.length() > 0) builder.setLength(builder.length() - 1);
    return builder.toString();
  }

  public static final int NONE = Riiablo.files.ItemTypes.index("");
  public static final int SHIE = Riiablo.files.ItemTypes.index("shie");
  public static final int TORS = Riiablo.files.ItemTypes.index("tors");
  public static final int GOLD = Riiablo.files.ItemTypes.index("gold");
  public static final int BOWQ = Riiablo.files.ItemTypes.index("bowq");
  public static final int XBOQ = Riiablo.files.ItemTypes.index("xboq");
  public static final int PLAY = Riiablo.files.ItemTypes.index("play");
  public static final int HERB = Riiablo.files.ItemTypes.index("herb");
  public static final int POTI = Riiablo.files.ItemTypes.index("poti");
  public static final int RING = Riiablo.files.ItemTypes.index("ring");
  public static final int ELIX = Riiablo.files.ItemTypes.index("elix");
  public static final int AMUL = Riiablo.files.ItemTypes.index("amul");
  public static final int CHAR = Riiablo.files.ItemTypes.index("char");
  public static final int BOOT = Riiablo.files.ItemTypes.index("boot");
  public static final int GLOV = Riiablo.files.ItemTypes.index("glov");
  public static final int BOOK = Riiablo.files.ItemTypes.index("book");
  public static final int BELT = Riiablo.files.ItemTypes.index("belt");
  public static final int GEM  = Riiablo.files.ItemTypes.index("gem");
  public static final int TORC = Riiablo.files.ItemTypes.index("torc");
  public static final int SCRO = Riiablo.files.ItemTypes.index("scro");
  public static final int SCEP = Riiablo.files.ItemTypes.index("scep");
  public static final int WAND = Riiablo.files.ItemTypes.index("wand");
  public static final int STAF = Riiablo.files.ItemTypes.index("staf");
  public static final int BOW  = Riiablo.files.ItemTypes.index("bow");
  public static final int AXE  = Riiablo.files.ItemTypes.index("axe");
  public static final int CLUB = Riiablo.files.ItemTypes.index("club");
  public static final int SWOR = Riiablo.files.ItemTypes.index("swor");
  public static final int HAMM = Riiablo.files.ItemTypes.index("hamm");
  public static final int KNIF = Riiablo.files.ItemTypes.index("knif");
  public static final int SPEA = Riiablo.files.ItemTypes.index("spea");
  public static final int POLE = Riiablo.files.ItemTypes.index("pole");
  public static final int XBOW = Riiablo.files.ItemTypes.index("xbow");
  public static final int MACE = Riiablo.files.ItemTypes.index("mace");
  public static final int HELM = Riiablo.files.ItemTypes.index("helm");
  public static final int TPOT = Riiablo.files.ItemTypes.index("tpot");
  public static final int QUES = Riiablo.files.ItemTypes.index("ques");
  public static final int BODY = Riiablo.files.ItemTypes.index("body");
  public static final int KEY  = Riiablo.files.ItemTypes.index("key");
  public static final int TKNI = Riiablo.files.ItemTypes.index("tkni");
  public static final int TAXE = Riiablo.files.ItemTypes.index("taxe");
  public static final int JAVE = Riiablo.files.ItemTypes.index("jave");
  public static final int WEAP = Riiablo.files.ItemTypes.index("weap");
  public static final int MELE = Riiablo.files.ItemTypes.index("mele");
  public static final int MISS = Riiablo.files.ItemTypes.index("miss");
  public static final int THRO = Riiablo.files.ItemTypes.index("thro");
  public static final int COMB = Riiablo.files.ItemTypes.index("comb");
  public static final int ARMO = Riiablo.files.ItemTypes.index("armo");
  public static final int SHLD = Riiablo.files.ItemTypes.index("shld");
  public static final int MISC = Riiablo.files.ItemTypes.index("misc");
  public static final int SOCK = Riiablo.files.ItemTypes.index("sock");
  public static final int SECO = Riiablo.files.ItemTypes.index("seco");
  public static final int ROD  = Riiablo.files.ItemTypes.index("rod");
  public static final int MISL = Riiablo.files.ItemTypes.index("misl");
  public static final int BLUN = Riiablo.files.ItemTypes.index("blun");
  public static final int JEWL = Riiablo.files.ItemTypes.index("jewl");
  public static final int CLAS = Riiablo.files.ItemTypes.index("clas");
  public static final int AMAZ = Riiablo.files.ItemTypes.index("amaz");
  public static final int BARB = Riiablo.files.ItemTypes.index("barb");
  public static final int NECR = Riiablo.files.ItemTypes.index("necr");
  public static final int PALA = Riiablo.files.ItemTypes.index("pala");
  public static final int SORC = Riiablo.files.ItemTypes.index("sorc");
  public static final int ASSN = Riiablo.files.ItemTypes.index("assn");
  public static final int DRUI = Riiablo.files.ItemTypes.index("drui");
  public static final int H2H  = Riiablo.files.ItemTypes.index("h2h");
  public static final int ORB  = Riiablo.files.ItemTypes.index("orb");
  public static final int HEAD = Riiablo.files.ItemTypes.index("head");
  public static final int ASHD = Riiablo.files.ItemTypes.index("ashd");
  public static final int PHLM = Riiablo.files.ItemTypes.index("phlm");
  public static final int PELT = Riiablo.files.ItemTypes.index("pelt");
  public static final int CLOA = Riiablo.files.ItemTypes.index("cloa");
  public static final int RUNE = Riiablo.files.ItemTypes.index("rune");
  public static final int CIRC = Riiablo.files.ItemTypes.index("circ");
  public static final int HPOT = Riiablo.files.ItemTypes.index("hpot");
  public static final int MPOT = Riiablo.files.ItemTypes.index("mpot");
  public static final int RPOT = Riiablo.files.ItemTypes.index("rpot");
  public static final int SPOT = Riiablo.files.ItemTypes.index("spot");
  public static final int APOT = Riiablo.files.ItemTypes.index("apot");
  public static final int WPOT = Riiablo.files.ItemTypes.index("wpot");
  public static final int SCHA = Riiablo.files.ItemTypes.index("scha");
  public static final int MCHA = Riiablo.files.ItemTypes.index("mcha");
  public static final int LCHA = Riiablo.files.ItemTypes.index("lcha");
  public static final int ABOW = Riiablo.files.ItemTypes.index("abow");
  public static final int ASPE = Riiablo.files.ItemTypes.index("aspe");
  public static final int AJAV = Riiablo.files.ItemTypes.index("ajav");
  public static final int H2H2 = Riiablo.files.ItemTypes.index("h2h2");
  public static final int MBOQ = Riiablo.files.ItemTypes.index("mboq");
  public static final int MXBQ = Riiablo.files.ItemTypes.index("mxbq");
  public static final int GEM0 = Riiablo.files.ItemTypes.index("gem0");
  public static final int GEM1 = Riiablo.files.ItemTypes.index("gem1");
  public static final int GEM2 = Riiablo.files.ItemTypes.index("gem2");
  public static final int GEM3 = Riiablo.files.ItemTypes.index("gem3");
  public static final int GEM4 = Riiablo.files.ItemTypes.index("gem4");
  public static final int GEMA = Riiablo.files.ItemTypes.index("gema");
  public static final int GEMD = Riiablo.files.ItemTypes.index("gemd");
  public static final int GEME = Riiablo.files.ItemTypes.index("geme");
  public static final int GEMR = Riiablo.files.ItemTypes.index("gemr");
  public static final int GEMS = Riiablo.files.ItemTypes.index("gems");
  public static final int GEMT = Riiablo.files.ItemTypes.index("gemt");
  public static final int GEMZ = Riiablo.files.ItemTypes.index("gemz");

  static final Type GEMA0 = create(TYPES[GEMA], TYPES[GEM0]);
  static final Type GEMA1 = create(TYPES[GEMA], TYPES[GEM1]);
  static final Type GEMA2 = create(TYPES[GEMA], TYPES[GEM2]);
  static final Type GEMA3 = create(TYPES[GEMA], TYPES[GEM3]);
  static final Type GEMA4 = create(TYPES[GEMA], TYPES[GEM4]);

  static final Type GEMT0 = create(TYPES[GEMT], TYPES[GEM0]);
  static final Type GEMT1 = create(TYPES[GEMT], TYPES[GEM1]);
  static final Type GEMT2 = create(TYPES[GEMT], TYPES[GEM2]);
  static final Type GEMT3 = create(TYPES[GEMT], TYPES[GEM3]);
  static final Type GEMT4 = create(TYPES[GEMT], TYPES[GEM4]);

  static final Type GEMS0 = create(TYPES[GEMS], TYPES[GEM0]);
  static final Type GEMS1 = create(TYPES[GEMS], TYPES[GEM1]);
  static final Type GEMS2 = create(TYPES[GEMS], TYPES[GEM2]);
  static final Type GEMS3 = create(TYPES[GEMS], TYPES[GEM3]);
  static final Type GEMS4 = create(TYPES[GEMS], TYPES[GEM4]);

  static final Type GEME0 = create(TYPES[GEME], TYPES[GEM0]);
  static final Type GEME1 = create(TYPES[GEME], TYPES[GEM1]);
  static final Type GEME2 = create(TYPES[GEME], TYPES[GEM2]);
  static final Type GEME3 = create(TYPES[GEME], TYPES[GEM3]);
  static final Type GEME4 = create(TYPES[GEME], TYPES[GEM4]);

  static final Type GEMR0 = create(TYPES[GEMR], TYPES[GEM0]);
  static final Type GEMR1 = create(TYPES[GEMR], TYPES[GEM1]);
  static final Type GEMR2 = create(TYPES[GEMR], TYPES[GEM2]);
  static final Type GEMR3 = create(TYPES[GEMR], TYPES[GEM3]);
  static final Type GEMR4 = create(TYPES[GEMR], TYPES[GEM4]);

  static final Type GEMD0 = create(TYPES[GEMD], TYPES[GEM0]);
  static final Type GEMD1 = create(TYPES[GEMD], TYPES[GEM1]);
  static final Type GEMD2 = create(TYPES[GEMD], TYPES[GEM2]);
  static final Type GEMD3 = create(TYPES[GEMD], TYPES[GEM3]);
  static final Type GEMD4 = create(TYPES[GEMD], TYPES[GEM4]);

  static final Type GEMZ0 = create(TYPES[GEMZ], TYPES[GEM0]);
  static final Type GEMZ1 = create(TYPES[GEMZ], TYPES[GEM1]);
  static final Type GEMZ2 = create(TYPES[GEMZ], TYPES[GEM2]);
  static final Type GEMZ3 = create(TYPES[GEMZ], TYPES[GEM3]);
  static final Type GEMZ4 = create(TYPES[GEMZ], TYPES[GEM4]);

  private static Type create(Type type1, Type type2) {
    Type type = new Type();
    type.or(type1);
    type.or(type2);
    return type;
  }

  /**
   * Returns a pre-existing type for the cartesian products of gems types and qualities.
   */
  private static Type _get(Type type1, Type type2) {
    if (TYPES[GEMA].equals(type1)) {
      if (TYPES[GEM0].equals(type2)) return GEMA0;
      if (TYPES[GEM1].equals(type2)) return GEMA1;
      if (TYPES[GEM2].equals(type2)) return GEMA2;
      if (TYPES[GEM3].equals(type2)) return GEMA3;
      if (TYPES[GEM4].equals(type2)) return GEMA4;
    } else if (TYPES[GEMT].equals(type1)) {
      if (TYPES[GEM0].equals(type2)) return GEMT0;
      if (TYPES[GEM1].equals(type2)) return GEMT1;
      if (TYPES[GEM2].equals(type2)) return GEMT2;
      if (TYPES[GEM3].equals(type2)) return GEMT3;
      if (TYPES[GEM4].equals(type2)) return GEMT4;
    } else if (TYPES[GEMS].equals(type1)) {
      if (TYPES[GEM0].equals(type2)) return GEMS0;
      if (TYPES[GEM1].equals(type2)) return GEMS1;
      if (TYPES[GEM2].equals(type2)) return GEMS2;
      if (TYPES[GEM3].equals(type2)) return GEMS3;
      if (TYPES[GEM4].equals(type2)) return GEMS4;
    } else if (TYPES[GEME].equals(type1)) {
      if (TYPES[GEM0].equals(type2)) return GEME0;
      if (TYPES[GEM1].equals(type2)) return GEME1;
      if (TYPES[GEM2].equals(type2)) return GEME2;
      if (TYPES[GEM3].equals(type2)) return GEME3;
      if (TYPES[GEM4].equals(type2)) return GEME4;
    } else if (TYPES[GEMR].equals(type1)) {
      if (TYPES[GEM0].equals(type2)) return GEMR0;
      if (TYPES[GEM1].equals(type2)) return GEMR1;
      if (TYPES[GEM2].equals(type2)) return GEMR2;
      if (TYPES[GEM3].equals(type2)) return GEMR3;
      if (TYPES[GEM4].equals(type2)) return GEMR4;
    } else if (TYPES[GEMD].equals(type1)) {
      if (TYPES[GEM0].equals(type2)) return GEMD0;
      if (TYPES[GEM1].equals(type2)) return GEMD1;
      if (TYPES[GEM2].equals(type2)) return GEMD2;
      if (TYPES[GEM3].equals(type2)) return GEMD3;
      if (TYPES[GEM4].equals(type2)) return GEMD4;
    } else if (TYPES[GEMZ].equals(type1)) {
      if (TYPES[GEM0].equals(type2)) return GEMZ0;
      if (TYPES[GEM1].equals(type2)) return GEMZ1;
      if (TYPES[GEM2].equals(type2)) return GEMZ2;
      if (TYPES[GEM3].equals(type2)) return GEMZ3;
      if (TYPES[GEM4].equals(type2)) return GEMZ4;
    }

    // Shouldn't get this far in normal gameplay.
    // In production we don't care, but log it anyways if it happens.
    assert false : "type1 and type2 did not match expected values";
    Gdx.app.error(TAG, "Creating aggregate type of " + type1 + " and " + type2);
    assert !type1.is(NONE) : "type1 shouldn't have 'none' bit set: " + type1.getHexString();
    if (type2.is(NONE)) return type1;
    Type type = new Type();
    type.or(type1);
    type.or(type2);
    return type;
  }

  public static Type get(Type type1, Type type2) {
    if (type2.is(NONE)) return type1;
    return _get(type1, type2);
  }

  public static Type get(ItemTypes.Entry type1, ItemTypes.Entry type2) {
    return get(get(type1), get(type2));
  }
}
