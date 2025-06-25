

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Longsword extends MeleeWeapon {

    {
        image = ItemSpriteSheet.LONGSWORD;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1f;

        tier = 4;
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target) {
        if (hero.buff(Sword.CleaveTracker.class) != null) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(6+lvl) damage, roughly +40% base dmg, +33% scaling
        int dmgBoost = augment.damageFactor(6 + buffedLvl());
        Sword.cleaveAbility(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 6 + buffedLvl() : 6;
        if (levelKnown) {
            return Messages.get(this, "ability_desc", augment.damageFactor(min() + dmgBoost), augment.damageFactor(max() + dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0) + dmgBoost, max(0) + dmgBoost);
        }
    }

    public String upgradeAbilityStat(int level) {
        int dmgBoost = 6 + level;
        return augment.damageFactor(min(level) + dmgBoost) + "-" + augment.damageFactor(max(level) + dmgBoost);
    }
}
