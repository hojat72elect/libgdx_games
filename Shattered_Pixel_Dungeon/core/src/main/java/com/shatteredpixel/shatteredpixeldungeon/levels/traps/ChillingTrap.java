

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
import com.watabou.utils.PathFinder;

public class ChillingTrap extends Trap {

    {
        color = WHITE;
        shape = DOTS;
    }

    @Override
    public void activate() {
        if (Dungeon.level.heroFOV[pos]) {
            Splash.at(pos, 0xFFB2D6FF, 5);
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
        }

        for (int i : PathFinder.NEIGHBOURS9) {
            if (!Dungeon.level.solid[pos + i]) {
                GameScene.add(Blob.seed(pos + i, 10, Freezing.class));
                if (Actor.findChar(pos + i) instanceof Mob) {
                    Buff.prolong(Actor.findChar(pos + i), Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
                }
            }
        }
    }
}
