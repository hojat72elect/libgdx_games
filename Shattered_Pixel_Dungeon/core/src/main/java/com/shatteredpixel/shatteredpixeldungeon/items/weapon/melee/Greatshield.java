

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Greatshield extends MeleeWeapon {

    {
        image = ItemSpriteSheet.GREATSHIELD;

        tier = 5;
    }

    @Override
    public int max(int lvl) {
        return Math.round(3f * (tier + 1)) +   //18 base, down from 20
                lvl * (tier - 1);               //+3 per level, down from +6
    }

    @Override
    public int defenseFactor(Char owner) {
        return DRMax();
    }

    public int DRMax() {
        return DRMax(buffedLvl());
    }

    //6 extra defence, plus 2 per level
    public int DRMax(int lvl) {
        return 6 + 2 * lvl;
    }

    public String statsInfo() {
        if (isIdentified()) {
            return Messages.get(this, "stats_desc", 6 + 2 * buffedLvl());
        } else {
            return Messages.get(this, "typical_stats_desc", 6);
        }
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        RoundShield.guardAbility(hero, 3 + buffedLvl(), this);
    }

    @Override
    public String abilityInfo() {
        if (levelKnown) {
            return Messages.get(this, "ability_desc", 3 + buffedLvl());
        } else {
            return Messages.get(this, "typical_ability_desc", 3);
        }
    }

    @Override
    public String upgradeAbilityStat(int level) {
        return Integer.toString(3 + level);
    }
}
