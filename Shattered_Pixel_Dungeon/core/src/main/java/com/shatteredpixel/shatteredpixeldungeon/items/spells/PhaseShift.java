

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class PhaseShift extends TargetedSpell {

    {
        image = ItemSpriteSheet.PHASE_SHIFT;

        usesTargeting = true;

        talentChance = 1 / (float) Recipe.OUT_QUANTITY;
    }

    @Override
    protected void affectTarget(Ballistica bolt, Hero hero) {
        final Char ch = Actor.findChar(bolt.collisionPos);

        if (ch != null) {
            if (ScrollOfTeleportation.teleportChar(ch)) {

                if (ch instanceof Mob) {
                    if (((Mob) ch).state == ((Mob) ch).HUNTING) ((Mob) ch).state = ((Mob) ch).WANDERING;
                    ((Mob) ch).beckon(Dungeon.level.randomDestination(ch));
                }
                if (!Char.hasProp(ch, Char.Property.BOSS) && !Char.hasProp(ch, Char.Property.MINIBOSS)) {
                    Buff.affect(ch, Paralysis.class, Paralysis.DURATION);
                }
            }
        } else {
            GLog.w(Messages.get(this, "no_target"));
        }
    }

    @Override
    public int value() {
        return (int) (60 * (quantity / (float) Recipe.OUT_QUANTITY));
    }

    @Override
    public int energyVal() {
        return (int) (12 * (quantity / (float) Recipe.OUT_QUANTITY));
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        private static final int OUT_QUANTITY = 6;

        {
            inputs = new Class[]{ScrollOfTeleportation.class};
            inQuantity = new int[]{1};

            cost = 10;

            output = PhaseShift.class;
            outQuantity = OUT_QUANTITY;
        }
    }
}
