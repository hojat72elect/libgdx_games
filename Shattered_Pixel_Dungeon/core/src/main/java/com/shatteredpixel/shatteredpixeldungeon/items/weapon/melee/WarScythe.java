

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class WarScythe extends MeleeWeapon {

    {
        image = ItemSpriteSheet.WAR_SCYTHE;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 0.9f;

        tier = 5;
        ACC = 0.8f; //20% penalty to accuracy
    }

    @Override
    public int max(int lvl) {
        return Math.round(6.67f * (tier + 1)) +    //40 base, up from 30
                lvl * (tier + 1);                   //scaling unchanged
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //replaces damage with 30+4.5*lvl bleed, roughly 133% avg base dmg, 129% avg scaling
        int bleedAmt = augment.damageFactor(Math.round(30f + 4.5f * buffedLvl()));
        Sickle.harvestAbility(hero, target, 0f, bleedAmt, this);
    }

    @Override
    public String abilityInfo() {
        int bleedAmt = levelKnown ? Math.round(30f + 4.5f * buffedLvl()) : 30;
        if (levelKnown) {
            return Messages.get(this, "ability_desc", augment.damageFactor(bleedAmt));
        } else {
            return Messages.get(this, "typical_ability_desc", bleedAmt);
        }
    }

    @Override
    public String upgradeAbilityStat(int level) {
        return Integer.toString(augment.damageFactor(Math.round(30f + 4.5f * level)));
    }
}
