

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class WarHammer extends MeleeWeapon {

    {
        image = ItemSpriteSheet.WAR_HAMMER;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 5;
        ACC = 1.20f; //20% boost to accuracy
    }

    @Override
    public int max(int lvl) {
        return 4 * (tier + 1) +    //24 base, down from 30
                lvl * (tier + 1);   //scaling unchanged
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(6+1.5*lvl) damage, roughly +40% base dmg, +45% scaling
        int dmgBoost = augment.damageFactor(6 + Math.round(1.5f * buffedLvl()));
        Mace.heavyBlowAbility(hero, target, 1, dmgBoost, this);
    }

    public String upgradeAbilityStat(int level) {
        int dmgBoost = 6 + Math.round(1.5f * level);
        return augment.damageFactor(min(level) + dmgBoost) + "-" + augment.damageFactor(max(level) + dmgBoost);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 6 + Math.round(1.5f * buffedLvl()) : 6;
        if (levelKnown) {
            return Messages.get(this, "ability_desc", augment.damageFactor(min() + dmgBoost), augment.damageFactor(max() + dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0) + dmgBoost, max(0) + dmgBoost);
        }
    }
}
