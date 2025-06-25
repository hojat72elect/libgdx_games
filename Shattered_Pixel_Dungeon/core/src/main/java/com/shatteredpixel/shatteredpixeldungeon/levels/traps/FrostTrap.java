

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

public class FrostTrap extends Trap {

    {
        color = WHITE;
        shape = STARS;
    }

    @Override
    public void activate() {

        if (Dungeon.level.heroFOV[pos]) {
            Splash.at(pos, 0xFFB2D6FF, 5);
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
        }

        PathFinder.buildDistanceMap(pos, BArray.not(Dungeon.level.solid, null), 2);
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                GameScene.add(Blob.seed(i, 20, Freezing.class));
                if (Actor.findChar(i) instanceof Mob) {
                    Buff.prolong(Actor.findChar(i), Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
                }
            }
        }
    }
}
