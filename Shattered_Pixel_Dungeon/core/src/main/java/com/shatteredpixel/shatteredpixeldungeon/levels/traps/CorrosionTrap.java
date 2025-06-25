

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class CorrosionTrap extends Trap {

    {
        color = GREY;
        shape = GRILL;
    }

    @Override
    public void activate() {

        CorrosiveGas corrosiveGas = Blob.seed(pos, 80 + 5 * scalingDepth(), CorrosiveGas.class);
        Sample.INSTANCE.play(Assets.Sounds.GAS);

        corrosiveGas.setStrength(1 + scalingDepth() / 4);

        for (int i : PathFinder.NEIGHBOURS9) {
            if (Actor.findChar(pos + i) instanceof Mob) {
                Buff.prolong(Actor.findChar(pos + i), Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
            }
        }

        GameScene.add(corrosiveGas);
    }
}
