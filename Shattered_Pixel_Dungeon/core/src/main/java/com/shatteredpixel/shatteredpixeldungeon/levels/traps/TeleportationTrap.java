

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class TeleportationTrap extends Trap {

    {
        color = TEAL;
        shape = DOTS;
    }

    @Override
    public void activate() {

        for (int i : PathFinder.NEIGHBOURS9) {
            Char ch = Actor.findChar(pos + i);
            if (ch != null) {
                if (ScrollOfTeleportation.teleportChar(ch)) {
                    if (ch instanceof Mob && ((Mob) ch).state == ((Mob) ch).HUNTING) {
                        ((Mob) ch).state = ((Mob) ch).WANDERING;
                        Buff.prolong(ch, Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
                    }
                }
            }
            Heap heap = Dungeon.level.heaps.get(pos + i);
            if (heap != null && heap.type == Heap.Type.HEAP) {
                int cell = Dungeon.level.randomRespawnCell(null);

                Item item = heap.pickUp();

                if (cell != -1) {
                    Dungeon.level.drop(item, cell);
                    if (item instanceof Honeypot.ShatteredPot) {
                        ((Honeypot.ShatteredPot) item).movePot(pos, cell);
                    }
                    Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
                    CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 4);
                }
            }
        }
    }
}
