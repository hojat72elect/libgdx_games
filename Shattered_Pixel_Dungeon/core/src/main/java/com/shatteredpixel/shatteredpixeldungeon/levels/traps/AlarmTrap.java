

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class AlarmTrap extends Trap {

    {
        color = RED;
        shape = DOTS;
    }

    @Override
    public void activate() {

        for (Mob mob : Dungeon.level.mobs) {
            mob.beckon(pos);
        }

        if (Dungeon.level.heroFOV[pos]) {
            GLog.w(Messages.get(this, "alarm"));
            CellEmitter.center(pos).start(Speck.factory(Speck.SCREAM), 0.3f, 3);
        }

        Sample.INSTANCE.play(Assets.Sounds.ALERT);
    }
}
