

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Glaive extends MeleeWeapon {

    {
        image = ItemSpriteSheet.GLAIVE;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 0.8f;

        tier = 5;
        DLY = 1.5f; //0.67x speed
        RCH = 2;    //extra reach
    }

    @Override
    public int max(int lvl) {
        return Math.round(6.67f * (tier + 1)) +    //40 base, up from 30
                lvl * Math.round(1.33f * (tier + 1)); //+8 per level, up from +6
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(12+2.5*lvl) damage, roughly +55% base damage, +55% scaling
        int dmgBoost = augment.damageFactor(12 + Math.round(2.5f * buffedLvl()));
        Spear.spikeAbility(hero, target, 1, dmgBoost, this);
    }

    public String upgradeAbilityStat(int level) {
        int dmgBoost = 12 + Math.round(2.5f * level);
        return augment.damageFactor(min(level) + dmgBoost) + "-" + augment.damageFactor(max(level) + dmgBoost);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 12 + Math.round(2.5f * buffedLvl()) : 12;
        if (levelKnown) {
            return Messages.get(this, "ability_desc", augment.damageFactor(min() + dmgBoost), augment.damageFactor(max() + dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0) + dmgBoost, max(0) + dmgBoost);
        }
    }
}
