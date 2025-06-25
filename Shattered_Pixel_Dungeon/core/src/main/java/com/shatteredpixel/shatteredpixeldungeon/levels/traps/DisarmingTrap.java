

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class DisarmingTrap extends Trap {

    {
        color = RED;
        shape = LARGE_DOT;
    }

    @Override
    public void activate() {
        Heap heap = Dungeon.level.heaps.get(pos);

        if (heap != null && heap.type == Heap.Type.HEAP) {

            int cell;
            do {
                cell = Dungeon.level.randomRespawnCell(null);
            } while (cell != -1 && Dungeon.level.heaps.get(pos) != null
                    && Dungeon.level.heaps.get(pos).type != Heap.Type.HEAP);

            if (cell != -1) {
                Item item = heap.pickUp();
                Heap dropped = Dungeon.level.drop(item, cell);
                dropped.seen = true;
                if (item instanceof Honeypot.ShatteredPot) {
                    ((Honeypot.ShatteredPot) item).movePot(pos, cell);
                }
                for (int i : PathFinder.NEIGHBOURS9) Dungeon.level.visited[cell + i] = true;
                GameScene.updateFog();
                Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
                CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 4);
            }
        }

        if (Actor.findChar(pos) instanceof Statue) {
            Actor.findChar(pos).die(this);
            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
            CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 4);
        }

        if (Dungeon.hero.pos == pos && !Dungeon.hero.flying) {
            Hero hero = Dungeon.hero;
            KindOfWeapon weapon = hero.belongings.weapon;

            if (weapon != null && !weapon.cursed) {

                int cell;
                int tries = 50;
                do {
                    cell = Dungeon.level.randomRespawnCell(null);
                    if (tries-- < 0 && cell != -1) break;

                    PathFinder.buildDistanceMap(pos, Dungeon.level.passable);
                } while (cell == -1 || PathFinder.distance[cell] < 10 || PathFinder.distance[cell] > 20);

                if (tries < 0) {
                    return;
                }

                hero.belongings.weapon = null;
                Dungeon.quickslot.clearItem(weapon);
                Item.updateQuickslot();

                Dungeon.level.drop(weapon, cell).seen = true;
                for (int i : PathFinder.NEIGHBOURS9) {
                    Dungeon.level.mapped[cell + i] = true;
                }
                GameScene.updateFog(cell, 1);

                GLog.w(Messages.get(this, "disarm"));

                Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
                CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 4);
            }
        }
    }
}
