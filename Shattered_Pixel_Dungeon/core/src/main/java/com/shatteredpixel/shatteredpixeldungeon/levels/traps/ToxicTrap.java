

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class ToxicTrap extends Trap {

    {
        color = GREEN;
        shape = GRILL;
    }

    @Override
    public void activate() {

        GameScene.add(Blob.seed(pos, 300 + 20 * scalingDepth(), ToxicGas.class));
        Sample.INSTANCE.play(Assets.Sounds.GAS);

        for (int i : PathFinder.NEIGHBOURS9) {
            if (Actor.findChar(pos + i) instanceof Mob) {
                Buff.prolong(Actor.findChar(pos + i), Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
            }
        }
    }
}
