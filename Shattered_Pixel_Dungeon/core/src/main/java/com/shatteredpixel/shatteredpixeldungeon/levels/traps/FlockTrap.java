

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class FlockTrap extends Trap {

    {
        color = WHITE;
        shape = WAVES;
    }

    @Override
    public void activate() {
        PathFinder.buildDistanceMap(pos, BArray.not(Dungeon.level.solid, null), 2);
        ArrayList<Integer> spawnPoints = new ArrayList<>();
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                spawnPoints.add(i);
            }
        }

        for (int i : spawnPoints) {
            Trap t;
            if (Dungeon.level.insideMap(i)
                    && Actor.findChar(i) == null
                    && !(Dungeon.level.pit[i])) {
                Sheep sheep = new Sheep();
                sheep.initialize(6);
                sheep.pos = i;
                GameScene.add(sheep);
                CellEmitter.get(i).burst(Speck.factory(Speck.WOOL), 4);
                //before the tile is pressed, directly trigger traps to avoid sfx spam
                if ((t = Dungeon.level.traps.get(i)) != null && t.active) {
                    if (t.disarmedByActivation) t.disarm();
                    t.reveal();
                    Bestiary.setSeen(t.getClass());
                    Bestiary.countEncounter(t.getClass());
                    t.activate();
                }
                Dungeon.level.occupyCell(sheep);
            } else if (Actor.findChar(i) instanceof Mob) {
                Buff.prolong(Actor.findChar(i), Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.PUFF);
        Sample.INSTANCE.play(Assets.Sounds.SHEEP);
    }
}
