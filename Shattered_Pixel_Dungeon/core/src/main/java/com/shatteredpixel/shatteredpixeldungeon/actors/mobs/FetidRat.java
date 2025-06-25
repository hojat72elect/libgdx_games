

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FetidRatSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class FetidRat extends Rat {

    {
        spriteClass = FetidRatSprite.class;

        HP = HT = 20;
        defenseSkill = 5;

        EXP = 4;

        WANDERING = new Wandering();
        state = WANDERING;

        properties.add(Property.MINIBOSS);
        properties.add(Property.DEMONIC);
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 2);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Ooze.class).set(Ooze.DURATION);
            //score loss is on-hit instead of on-attack because it's tied to ooze
            if (enemy == Dungeon.hero && !Dungeon.level.water[enemy.pos]) {
                Statistics.questScores[0] -= 50;
            }
        }

        return damage;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {

        GameScene.add(Blob.seed(pos, 20, StenchGas.class));

        return super.defenseProc(enemy, damage);
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

    {
        immunities.add(StenchGas.class);
    }
}