

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;

public class FlashingTrap extends Trap {

    {
        color = GREY;
        shape = STARS;

        disarmedByActivation = false;
        avoidsHallways = true;
    }

    @Override
    public void activate() {

        Char c = Actor.findChar(pos);

        if (c != null) {
            int damage = Math.max(0, (4 + scalingDepth() / 2) - c.drRoll() / 2);
            Buff.affect(c, Bleeding.class).set(damage);
            Buff.prolong(c, Blindness.class, Blindness.DURATION);
            Buff.prolong(c, Cripple.class, Cripple.DURATION * 2f);

            if (c instanceof Mob) {
                Buff.prolong(c, Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
                if (((Mob) c).state == ((Mob) c).HUNTING) ((Mob) c).state = ((Mob) c).WANDERING;
                ((Mob) c).beckon(Dungeon.level.randomDestination(c));
            }
        }

        if (Dungeon.level.heroFOV[pos]) {
            GameScene.flash(0x80FFFFFF);
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
        }
    }
}
