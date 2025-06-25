

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PitfallParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class PitfallTrap extends Trap {

    {
        color = RED;
        shape = DIAMOND;
    }

    @Override
    public void activate() {

        if (Dungeon.bossLevel() || Dungeon.depth > 25 || Dungeon.branch != 0) {
            GLog.w(Messages.get(this, "no_pit"));
            return;
        }

        DelayedPit p = Buff.append(Dungeon.hero, DelayedPit.class, 1);
        p.depth = Dungeon.depth;
        p.branch = Dungeon.branch;

        ArrayList<Integer> positions = new ArrayList<>();
        for (int i : PathFinder.NEIGHBOURS9) {
            if (!Dungeon.level.solid[pos + i] || Dungeon.level.passable[pos + i]) {
                CellEmitter.floor(pos + i).burst(PitfallParticle.FACTORY4, 8);
                positions.add(pos + i);
            }
        }
        p.setPositions(positions);

        if (pos == Dungeon.hero.pos) {
            GLog.n(Messages.get(this, "triggered_hero"));
        } else if (Dungeon.level.heroFOV[pos]) {
            GLog.n(Messages.get(this, "triggered"));
        }
    }

    public static class DelayedPit extends FlavourBuff {

        {
            revivePersists = true;
        }

        public int[] positions = new int[0];
        public int depth;
        public int branch;

        public boolean ignoreAllies = false;

        @Override
        public boolean act() {

            boolean herofell = false;
            if (depth == Dungeon.depth && branch == Dungeon.branch && positions != null) {
                for (int cell : positions) {

                    if (!Dungeon.level.insideMap(cell)
                            || (Dungeon.level.solid[cell] && !Dungeon.level.passable[cell])) {
                        continue;
                    }

                    CellEmitter.floor(cell).burst(PitfallParticle.FACTORY8, 12);

                    Char ch = Actor.findChar(cell);
                    //don't trigger on flying chars, or immovable neutral chars
                    if (ch != null && !ch.flying
                            && !(ch.alignment == Char.Alignment.NEUTRAL && Char.hasProp(ch, Char.Property.IMMOVABLE))
                            && !(ch.alignment == Char.Alignment.ALLY && ignoreAllies)) {
                        if (ch == Dungeon.hero) {
                            herofell = true;
                        } else {
                            Chasm.mobFall((Mob) ch);
                        }
                    }

                    Heap heap = Dungeon.level.heaps.get(cell);
                    if (heap != null && !ignoreAllies
                            && heap.type != Heap.Type.FOR_SALE
                            && heap.type != Heap.Type.LOCKED_CHEST
                            && heap.type != Heap.Type.CRYSTAL_CHEST) {
                        for (Item item : heap.items) {
                            Dungeon.dropToChasm(item);
                        }
                        heap.sprite.kill();
                        GameScene.discard(heap);
                        heap.sprite.drop();
                        Dungeon.level.heaps.remove(cell);
                    }
                }
            }

            //process hero falling last
            if (herofell) {
                Chasm.heroFall(Dungeon.hero.pos);
            }

            detach();
            return !herofell;
        }

        public void setPositions(ArrayList<Integer> positions) {
            this.positions = new int[positions.size()];
            for (int i = 0; i < this.positions.length; i++) {
                this.positions[i] = positions.get(i);
            }
        }

        private static final String POSITIONS = "positions";
        private static final String DEPTH = "depth";
        private static final String BRANCH = "branch";

        private static final String IGNORE_ALLIES = "ignore_allies";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(POSITIONS, positions);
            bundle.put(DEPTH, depth);
            bundle.put(BRANCH, branch);
            bundle.put(IGNORE_ALLIES, ignoreAllies);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            positions = bundle.getIntArray(POSITIONS);
            depth = bundle.getInt(DEPTH);
            branch = bundle.getInt(BRANCH);
            ignoreAllies = bundle.getBoolean(IGNORE_ALLIES);
        }
    }
}
