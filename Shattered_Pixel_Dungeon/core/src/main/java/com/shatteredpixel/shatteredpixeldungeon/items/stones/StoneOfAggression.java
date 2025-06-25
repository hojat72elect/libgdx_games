

package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;

public class StoneOfAggression extends Runestone {

    {
        image = ItemSpriteSheet.STONE_AGGRESSION;
    }

    @Override
    protected void activate(int cell) {

        Char ch = Actor.findChar(cell);

        if (ch != null) {
            if (Char.hasProp(ch, Char.Property.BOSS) || Char.hasProp(ch, Char.Property.MINIBOSS)) {
                Buff.prolong(ch, Aggression.class, Aggression.DURATION / 4f);
            } else {
                Buff.prolong(ch, Aggression.class, Aggression.DURATION);
            }
        }

        CellEmitter.center(cell).start(Speck.factory(Speck.SCREAM), 0.3f, 3);
        Sample.INSTANCE.play(Assets.Sounds.READ);
    }

    public static class Aggression extends FlavourBuff {

        public static final float DURATION = 20f;

        {
            type = buffType.NEGATIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.TARGETED;
        }

        @Override
        public float iconFadePercent() {
            if (Char.hasProp(target, Char.Property.BOSS) || Char.hasProp(target, Char.Property.MINIBOSS)) {
                return Math.max(0, (DURATION / 4f - visualcooldown()) / (DURATION / 4f));
            } else {
                return Math.max(0, (DURATION - visualcooldown()) / DURATION);
            }
        }

        @Override
        public void detach() {
            //if our target is an enemy, reset any enemy-to-enemy aggro involving it
            if (target.isAlive()) {
                if (target.alignment == Char.Alignment.ENEMY) {
                    for (Mob m : Dungeon.level.mobs) {
                        if (m.alignment == Char.Alignment.ENEMY && m.isTargeting(target)) {
                            m.aggro(null);
                        }
                        if (target instanceof Mob && ((Mob) target).isTargeting(m)) {
                            ((Mob) target).aggro(null);
                        }
                    }
                }
            }
            super.detach();
        }
    }
}
