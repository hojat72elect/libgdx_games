

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Gauntlet extends MeleeWeapon {

    {
        image = ItemSpriteSheet.GAUNTLETS;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1.2f;

        tier = 5;
        DLY = 0.5f; //2x speed
    }

    @Override
    public int max(int lvl) {
        return Math.round(2.5f * (tier + 1)) +     //15 base, down from 30
                lvl * Math.round(0.5f * (tier + 1));  //+3 per level, down from +6
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(5+lvl) damage, roughly +50% base damage, +50% scaling
        int dmgBoost = augment.damageFactor(5 + buffedLvl());
        Sai.comboStrikeAbility(hero, target, 0, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 5 + buffedLvl() : 5;
        if (levelKnown) {
            return Messages.get(this, "ability_desc", augment.damageFactor(dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", augment.damageFactor(dmgBoost));
        }
    }

    public String upgradeAbilityStat(int level) {
        return "+" + augment.damageFactor(5 + level);
    }
}
