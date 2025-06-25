

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class BurningTrap extends Trap {

    {
        color = ORANGE;
        shape = DOTS;
    }

    @Override
    public void activate() {

        for (int i : PathFinder.NEIGHBOURS9) {
            if (!Dungeon.level.solid[pos + i]) {
                GameScene.add(Blob.seed(pos + i, 2, Fire.class));
                CellEmitter.get(pos + i).burst(FlameParticle.FACTORY, 5);
                if (Actor.findChar(pos + i) instanceof Mob) {
                    Buff.prolong(Actor.findChar(pos + i), Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
                }
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.BURNING);
    }
}
