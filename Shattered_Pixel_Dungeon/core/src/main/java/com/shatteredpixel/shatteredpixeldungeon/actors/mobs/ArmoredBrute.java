

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShieldedSprite;
import com.watabou.utils.Random;

public class ArmoredBrute extends Brute {

    {
        spriteClass = ShieldedSprite.class;

        //see rollToDropLoot
        loot = Generator.Category.ARMOR;
        lootChance = 1f;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + 4; //4-12 DR total
    }

    @Override
    protected void triggerEnrage() {
        Buff.affect(this, ArmoredRage.class).setShield(HT / 2 + 1);
        sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(HT / 2 + 1), FloatingText.SHIELDING);
        if (Dungeon.level.heroFOV[pos]) {
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "enraged"));
        }
        spend(TICK);
        hasRaged = true;
    }

    @Override
    public Item createLoot() {
        if (Random.Int(4) == 0) {
            return new PlateArmor().random();
        }
        return new ScaleArmor().random();
    }

    //similar to regular brute rate, but deteriorates much slower. 60 turns to death total.
    public static class ArmoredRage extends Brute.BruteRage {

        @Override
        public boolean act() {

            if (target.HP > 0) {
                detach();
                return true;
            }

            absorbDamage(Math.round(AscensionChallenge.statModifier(target)));

            if (shielding() <= 0) {
                target.die(null);
            }

            spend(3 * TICK);

            return true;
        }
    }
}
