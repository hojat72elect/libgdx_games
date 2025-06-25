

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ImpShopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.ShopRoom;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;

import java.util.ArrayList;
import java.util.Collection;

//shops probably shouldn't extend special room, because of cases like this.
public class ImpShopRoom extends ShopRoom {

    private boolean impSpawned = false;

    //force a certain size here to guarantee enough room for 48 items, and the same center space
    @Override
    public int minWidth() {
        return 9;
    }

    public int minHeight() {
        return 9;
    }

    public int maxWidth() {
        return 9;
    }

    public int maxHeight() {
        return 9;
    }

    @Override
    public int maxConnections(int direction) {
        return 2;
    }

    @Override
    public void paint(Level level) {
        //this room isn't actually filled in until the city boss is defeated, at the earliest
        //but we want to decide the items as part of levelgen
        if (itemsToSpawn == null) {
            itemsToSpawn = generateItems();
        }
    }

    @Override
    protected void placeShopkeeper(Level level) {

        int pos = level.pointToCell(center());

        for (Point p : getPoints()) {
            if (level.map[level.pointToCell(p)] == Terrain.PEDESTAL) {
                pos = level.pointToCell(p);
                break;
            }
        }

        Mob shopkeeper = new ImpShopkeeper();
        shopkeeper.pos = pos;
        if (ShatteredPixelDungeon.scene() instanceof GameScene) {
            GameScene.add(shopkeeper);
        } else {
            level.mobs.add(shopkeeper);
        }
    }

    //fix for connections not being bundled normally
    @Override
    public Door entrance() {
        return connected.isEmpty() ? new Door((left + right) / 2 + 1, bottom - 1) : super.entrance();
    }

    public void spawnShop(Level level) {
        impSpawned = true;
        placeShopkeeper(level);
        placeItems(level);
    }

    public boolean shopSpawned() {
        return impSpawned;
    }

    private static final String IMP = "imp_spawned";
    private static final String ITEMS = "items";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(IMP, impSpawned);
        if (itemsToSpawn != null) bundle.put(ITEMS, itemsToSpawn);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        impSpawned = bundle.getBoolean(IMP);
        if (bundle.contains(ITEMS)) {
            itemsToSpawn = new ArrayList<>((Collection<Item>) ((Collection<?>) bundle.getCollection(ITEMS)));
        }
    }

    @Override
    public void onLevelLoad(Level level) {
        super.onLevelLoad(level);

        if (Imp.Quest.isCompleted() && !impSpawned) {
            spawnShop(level);
        }
    }
}
