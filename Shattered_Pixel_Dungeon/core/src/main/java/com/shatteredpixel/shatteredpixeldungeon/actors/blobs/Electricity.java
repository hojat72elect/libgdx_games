

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Electricity extends Blob {

    {
        //acts after mobs, to give them a chance to resist paralysis
        actPriority = MOB_PRIO - 1;
    }

    private boolean[] water;

    @Override
    protected void evolve() {

        water = Dungeon.level.water;
        int cell;

        //spread first..
        for (int i = area.left - 1; i <= area.right; i++) {
            for (int j = area.top - 1; j <= area.bottom; j++) {
                cell = i + j * Dungeon.level.width();

                if (cur[cell] > 0) {
                    spreadFromCell(cell, cur[cell]);
                }
            }
        }

        //..then decrement/shock
        for (int i = area.left - 1; i <= area.right; i++) {
            for (int j = area.top - 1; j <= area.bottom; j++) {
                cell = i + j * Dungeon.level.width();
                if (cur[cell] > 0) {
                    Char ch = Actor.findChar(cell);
                    if (ch != null && !ch.isImmune(this.getClass())) {
                        if (ch.buff(Paralysis.class) == null) {
                            Buff.prolong(ch, Paralysis.class, cur[cell]);
                        }
                        if (cur[cell] % 2 == 1) {
                            ch.damage(Math.round(Random.Float(2 + Dungeon.scalingDepth() / 5f)), this);
                            if (!ch.isAlive() && ch == Dungeon.hero) {
                                Dungeon.fail(this);
                                GLog.n(Messages.get(this, "ondeath"));
                            }
                        }
                    }

                    Heap h = Dungeon.level.heaps.get(cell);
                    if (h != null) {
                        Item toShock = h.peek();
                        if (toShock instanceof Wand) {
                            ((Wand) toShock).gainCharge(0.333f);
                        } else if (toShock instanceof MagesStaff) {
                            ((MagesStaff) toShock).gainCharge(0.333f);
                        }
                    }

                    off[cell] = cur[cell] - 1;
                    volume += off[cell];
                } else {
                    off[cell] = 0;
                }
            }
        }
    }

    private void spreadFromCell(int cell, int power) {
        if (cur[cell] == 0) {
            area.union(cell % Dungeon.level.width(), cell / Dungeon.level.width());
        }
        cur[cell] = Math.max(cur[cell], power);

        for (int c : PathFinder.NEIGHBOURS4) {
            if (water[cell + c] && cur[cell + c] < power) {
                spreadFromCell(cell + c, power);
            }
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.start(SparkParticle.FACTORY, 0.05f, 0);
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}
