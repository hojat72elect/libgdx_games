

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GreatCrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class GreatCrab extends Crab {

    {
        spriteClass = GreatCrabSprite.class;

        HP = HT = 25;
        defenseSkill = 0; //see damage()
        baseSpeed = 1f;

        EXP = 6;

        WANDERING = new Wandering();
        state = WANDERING;

        loot = new MysteryMeat().quantity(2);
        lootChance = 1f;

        properties.add(Property.MINIBOSS);
    }

    private int moving = 0;

    @Override
    protected boolean getCloser(int target) {
        //this is used so that the crab remains slower, but still detects the player at the expected rate.
        moving++;
        if (moving < 3) {
            return super.getCloser(target);
        } else {
            moving = 0;
            return true;
        }
    }

    @Override
    public void damage(int dmg, Object src) {
        //crab blocks all wand damage from the hero if it sees them.
        //Direct damage is negated, but add-on effects and environmental effects go through as normal.
        if (enemySeen
                && state != SLEEPING
                && paralysed == 0
                && (src instanceof Wand || src instanceof ClericSpell)
                && enemy == Dungeon.hero
                && enemy.invisible == 0) {
            GLog.n(Messages.get(this, "noticed"));
            sprite.showStatus(CharSprite.NEUTRAL, Messages.get(this, "def_verb"));
            Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
            Statistics.questScores[0] -= 50;
        } else {
            super.damage(dmg, src);
        }
    }

    @Override
    public int defenseSkill(Char enemy) {
        //crab blocks all melee attacks from its current target
        if (enemySeen
                && state != SLEEPING
                && paralysed == 0
                && enemy == this.enemy
                && enemy.invisible == 0) {
            if (sprite != null && sprite.visible) {
                Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
                GLog.n(Messages.get(this, "noticed"));
            }
            if (enemy == Dungeon.hero) {
                Statistics.questScores[0] -= 50;
            }
            return INFINITE_EVASION;
        }
        return super.defenseSkill(enemy);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        Ghost.Quest.process();
    }

    protected class Wandering extends Mob.Wandering {
        @Override
        protected int randomDestination() {
            //of two potential wander positions, picks the one closest to the hero
            int pos1 = super.randomDestination();
            int pos2 = super.randomDestination();
            PathFinder.buildDistanceMap(Dungeon.hero.pos, Dungeon.level.passable);
            if (PathFinder.distance[pos2] < PathFinder.distance[pos1]) {
                return pos2;
            } else {
                return pos1;
            }
        }
    }
}
