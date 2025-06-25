

package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Blindweed extends Plant {

    {
        image = 11;
        seedClass = Seed.class;
    }

    @Override
    public void activate(Char ch) {

        if (ch != null) {
            if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN) {
                Buff.affect(ch, Invisibility.class, Invisibility.DURATION / 2f);
            } else {
                Buff.prolong(ch, Blindness.class, Blindness.DURATION);
                Buff.prolong(ch, Cripple.class, Cripple.DURATION);
                if (ch instanceof Mob) {
                    Buff.prolong(ch, Trap.HazardAssistTracker.class, Trap.HazardAssistTracker.DURATION);
                    if (((Mob) ch).state == ((Mob) ch).HUNTING) ((Mob) ch).state = ((Mob) ch).WANDERING;
                    ((Mob) ch).beckon(Dungeon.level.randomDestination(ch));
                }
            }
        }

        if (Dungeon.level.heroFOV[pos]) {
            CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 4);
        }
    }

    public static class Seed extends Plant.Seed {
        {
            image = ItemSpriteSheet.SEED_BLINDWEED;

            plantClass = Blindweed.class;
        }
    }
}
