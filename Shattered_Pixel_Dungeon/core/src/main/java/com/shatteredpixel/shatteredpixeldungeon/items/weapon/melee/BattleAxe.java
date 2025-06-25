

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class BattleAxe extends MeleeWeapon {

    {
        image = ItemSpriteSheet.BATTLE_AXE;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 0.9f;

        tier = 4;
        ACC = 1.24f; //24% boost to accuracy
    }

    @Override
    public int max(int lvl) {
        return 4 * (tier + 1) +    //20 base, down from 25
                lvl * (tier + 1);   //scaling unchanged
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(5+1.5*lvl) damage, roughly +40% base dmg, +50% scaling
        int dmgBoost = augment.damageFactor(5 + Math.round(1.5f * buffedLvl()));
        Mace.heavyBlowAbility(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 5 + Math.round(1.5f * buffedLvl()) : 5;
        if (levelKnown) {
            return Messages.get(this, "ability_desc", augment.damageFactor(min() + dmgBoost), augment.damageFactor(max() + dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0) + dmgBoost, max(0) + dmgBoost);
        }
    }

    public String upgradeAbilityStat(int level) {
        int dmgBoost = 5 + Math.round(1.5f * level);
        return augment.damageFactor(min(level) + dmgBoost) + "-" + augment.damageFactor(max(level) + dmgBoost);
    }
}
