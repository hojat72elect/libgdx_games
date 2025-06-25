

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollGeomancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.MiningLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GnollRockfallTrap extends RockfallTrap {

    @Override
    public void activate() {

        ArrayList<Integer> rockCells = new ArrayList<>();

        //drop rocks in a 5x5 grid, ignoring cells next to barricades
        PathFinder.buildDistanceMap(pos, BArray.not(Dungeon.level.solid, null), 2);
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                if (Dungeon.level instanceof MiningLevel) {
                    boolean barricade = false;
                    for (int j : PathFinder.NEIGHBOURS9) {
                        if (Dungeon.level.map[i + j] == Terrain.BARRICADE) {
                            barricade = true;
                            break;
                        }
                    }
                    if (barricade) continue;
                }
                rockCells.add(i);
            }
        }

        boolean seen = false;
        for (int cell : rockCells) {

            if (Dungeon.level.heroFOV[cell]) {
                CellEmitter.get(cell - Dungeon.level.width()).start(Speck.factory(Speck.ROCK), 0.07f, 10);
                seen = true;
            }

            Char ch = Actor.findChar(cell);

            if (ch != null && ch.isAlive() && !(ch instanceof GnollGeomancer)) {
                if (ch instanceof Mob) {
                    Buff.prolong(ch, Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
                }

                //deals notably less damage than a regular rockfall trap, but ignores armor
                int damage = Random.NormalIntRange(6, 12);
                ch.damage(Math.max(damage, 0), this);

                //guards take full paralysis, otherwise just a little
                Buff.prolong(ch, Paralysis.class, ch instanceof GnollGuard ? 10 : 3);

                if (!ch.isAlive() && ch == Dungeon.hero) {
                    Dungeon.fail(this);
                    GLog.n(Messages.get(this, "ondeath"));
                    if (reclaimed) Badges.validateDeathFromFriendlyMagic();
                }
            } else if (ch == null
                    && Dungeon.level instanceof MiningLevel
                    && Dungeon.level.traps.get(cell) == null
                    && Dungeon.level.plants.get(cell) == null
                    && Random.Int(2) == 0) {
                Level.set(cell, Terrain.MINE_BOULDER);
                GameScene.updateMap(cell);
            }
        }

        if (seen) {
            PixelScene.shake(3, 0.7f);
            Sample.INSTANCE.play(Assets.Sounds.ROCKS);
        }
    }
}
