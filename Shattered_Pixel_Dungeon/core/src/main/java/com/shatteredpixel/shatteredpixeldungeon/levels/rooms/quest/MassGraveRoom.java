

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Image;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MassGraveRoom extends SpecialRoom {

    @Override
    public int minWidth() {
        return 7;
    }

    @Override
    public int minHeight() {
        return 7;
    }

    public void paint(Level level) {

        Door entrance = entrance();
        entrance.set(Door.Type.BARRICADE);
        level.addItemToSpawn(new PotionOfLiquidFlame());

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.CUSTOM_DECO_EMPTY);

        Bones b = new Bones();

        b.setRect(left + 1, top, width() - 2, height() - 1);
        level.customTiles.add(b);

        //50% 1 skeleton, 50% 2 skeletons
        for (int i = 0; i <= Random.Int(2); i++) {
            Skeleton skele = new Skeleton();

            int pos;
            do {
                pos = level.pointToCell(random());
            } while (level.map[pos] != Terrain.CUSTOM_DECO_EMPTY || level.findMob(pos) != null);
            skele.pos = pos;
            level.mobs.add(skele);
        }

        ArrayList<Item> items = new ArrayList<>();
        //100% corpse dust, 2x100% 1 coin, 2x30% coins, 1x60% random item, 1x30% armor
        items.add(new CorpseDust());
        items.add(new Gold(1));
        items.add(new Gold(1));
        if (Random.Float() <= 0.3f) items.add(new Gold());
        if (Random.Float() <= 0.3f) items.add(new Gold());
        if (Random.Float() <= 0.6f) items.add(Generator.random());
        if (Random.Float() <= 0.3f) items.add(Generator.randomArmor());

        for (Item item : items) {
            int pos;
            do {
                pos = level.pointToCell(random());
            } while (level.map[pos] != Terrain.CUSTOM_DECO_EMPTY || level.heaps.get(pos) != null);
            Heap h = level.drop(item, pos);
            h.setHauntedIfCursed();
            h.type = Heap.Type.SKELETON;
        }
    }

    @Override
    public boolean canConnect(Room r) {
        if (r.isEntrance()) {
            return false;
        }

        //must have at least 3 rooms between it and the entrance room
        for (Room r1 : r.connected.keySet()) {
            if (r1.isEntrance()) {
                return false;
            }
            for (Room r2 : r1.connected.keySet()) {
                if (r2.isEntrance()) {
                    return false;
                }
                for (Room r3 : r2.connected.keySet()) {
                    if (r3.isEntrance()) {
                        return false;
                    }
                }
            }
        }

        return super.canConnect(r);
    }

    public static class Bones extends CustomTilemap {

        private static final int WALL_OVERLAP = 3;
        private static final int FLOOR = 7;

        {
            texture = Assets.Environment.PRISON_QUEST;
        }

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            int[] data = new int[tileW * tileH];
            for (int i = 0; i < data.length; i++) {
                if (i < tileW) data[i] = WALL_OVERLAP;
                else data[i] = FLOOR;
            }
            v.map(data, tileW);
            return v;
        }

        @Override
        public Image image(int tileX, int tileY) {
            if (tileY == 0) return null;
            else return super.image(tileX, tileY);
        }

        @Override
        public String name(int tileX, int tileY) {
            return Messages.get(this, "name");
        }

        @Override
        public String desc(int tileX, int tileY) {
            return Messages.get(this, "desc");
        }
    }
}
