

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Crossbow;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class CleansingDart extends TippedDart {

    {
        image = ItemSpriteSheet.CLEANSING_DART;
    }

    @Override
    public int proc(Char attacker, final Char defender, int damage) {

        if (processingChargedShot && defender == attacker) {
            //do nothing to the hero when processing charged shot
        } else if (attacker.alignment == defender.alignment) {
            PotionOfCleansing.cleanse(defender, PotionOfCleansing.Cleanse.DURATION * 2f);
            return 0;
        } else {
            for (Buff b : defender.buffs()) {
                if (!(b instanceof ChampionEnemy)
                        && b.type == Buff.buffType.POSITIVE
                        && !(b instanceof Crossbow.ChargedShot)) {
                    b.detach();
                }
            }
            //for when cleansed effects were keeping defender alive (e.g. raging brutes)
            if (!defender.isAlive()) {
                defender.die(attacker);
                return super.proc(attacker, defender, damage);
            }
            if (defender instanceof Mob) {
                //need to delay this so damage from the dart doesn't break wandering
                new FlavourBuff() {
                    {
                        actPriority = VFX_PRIO;
                    }

                    public boolean act() {
                        if (((Mob) defender).state == ((Mob) defender).HUNTING || ((Mob) defender).state == ((Mob) defender).FLEEING) {
                            ((Mob) defender).state = ((Mob) defender).WANDERING;
                        }
                        ((Mob) defender).beckon(Dungeon.level.randomDestination(defender));
                        defender.sprite.showLost();
                        return super.act();
                    }
                }.attachTo(defender);
            }
        }

        return super.proc(attacker, defender, damage);
    }
}
