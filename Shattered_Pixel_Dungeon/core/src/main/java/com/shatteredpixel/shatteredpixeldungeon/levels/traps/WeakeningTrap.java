

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;

public class WeakeningTrap extends Trap {

    {
        color = GREEN;
        shape = WAVES;
    }

    @Override
    public void activate() {
        if (Dungeon.level.heroFOV[pos]) {
            CellEmitter.get(pos).burst(ShadowParticle.UP, 5);
        }

        Char ch = Actor.findChar(pos);
        if (ch != null) {
            if (ch.properties().contains(Char.Property.BOSS)
                    || ch.properties().contains(Char.Property.MINIBOSS)) {
                Buff.prolong(ch, Weakness.class, Weakness.DURATION / 2f);
            }
            Buff.prolong(ch, Weakness.class, Weakness.DURATION * 3f);
            if (ch instanceof Mob) {
                Buff.prolong(ch, Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
            }
        }
    }
}
