package com.riiablo.attributes;

import com.riiablo.Riiablo;
import com.riiablo.codec.excel.Gems;
import com.riiablo.logger.MDC;

public class GemGenerator {
  protected PropertiesGenerator generator;

  public GemGenerator() {}

  public GemGenerator(PropertiesGenerator generator) {
    this.generator = generator;
  }

  public Attributes set(Attributes attrs, String code) {
    return set(attrs, Riiablo.files.Gems.get(code));
  }

  public Attributes set(Attributes attrs, Gems.Entry gem) {
    assert attrs.isType(Attributes.Type.COMPACT) : "attrs(" + attrs + ") is not COMPACT(" + attrs.type() + ")";
    final StatList stats = attrs.list();
    try {
      int list;
      MDC.put("propList", StatListFlags.gemToString(StatListFlags.GEM_WEAPON_LIST));
      list = generator
          .add(stats.buildList(), gem.weaponModCode, gem.weaponModParam, gem.weaponModMin, gem.weaponModMax)
          .listIndex();
      assert list == StatListFlags.GEM_WEAPON_LIST : "list(" + list + ") != GEM_WEAPON_LIST(" + StatListFlags.GEM_WEAPON_LIST + ")";
      MDC.put("propList", StatListFlags.gemToString(StatListFlags.GEM_ARMOR_LIST));
      list = generator
          .add(stats.buildList(), gem.helmModCode, gem.helmModParam, gem.helmModMin, gem.helmModMax)
          .listIndex();
      assert list == StatListFlags.GEM_ARMOR_LIST : "list(" + list + ") != GEM_ARMOR_LIST(" + StatListFlags.GEM_ARMOR_LIST + ")";
      MDC.put("propList", StatListFlags.gemToString(StatListFlags.GEM_SHIELD_LIST));
      list = generator
          .add(stats.buildList(), gem.shieldModCode, gem.shieldModParam, gem.shieldModMin, gem.shieldModMax)
          .listIndex();
      assert list == StatListFlags.GEM_SHIELD_LIST : "list(" + list + ") != GEM_SHIELD_LIST(" + StatListFlags.GEM_SHIELD_LIST + ")";
    } finally {
      MDC.remove("propList");
    }
    stats.freeze();
    return attrs;
  }
}
