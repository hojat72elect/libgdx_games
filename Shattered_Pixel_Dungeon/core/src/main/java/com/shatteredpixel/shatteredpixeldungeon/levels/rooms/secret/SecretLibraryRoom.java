

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret;

import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ExoticCrystals;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.HashMap;

public class SecretLibraryRoom extends SecretRoom {

    @Override
    public int minWidth() {
        return Math.max(7, super.minWidth());
    }

    @Override
    public int minHeight() {
        return Math.max(7, super.minHeight());
    }

    private static final HashMap<Class<? extends Scroll>, Float> scrollChances = new HashMap<>();

    static {
        scrollChances.put(ScrollOfIdentify.class, 1f);
        scrollChances.put(ScrollOfRemoveCurse.class, 2f);
        scrollChances.put(ScrollOfMirrorImage.class, 3f);
        scrollChances.put(ScrollOfRecharging.class, 3f);
        scrollChances.put(ScrollOfTeleportation.class, 3f);
        scrollChances.put(ScrollOfLullaby.class, 4f);
        scrollChances.put(ScrollOfMagicMapping.class, 4f);
        scrollChances.put(ScrollOfRage.class, 4f);
        scrollChances.put(ScrollOfRetribution.class, 4f);
        scrollChances.put(ScrollOfTerror.class, 4f);
        scrollChances.put(ScrollOfTransmutation.class, 6f);
    }

    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.BOOKSHELF);

        Painter.fillEllipse(level, this, 2, Terrain.EMPTY_SP);

        Door entrance = entrance();
        if (entrance.x == left || entrance.x == right) {
            Painter.drawInside(level, this, entrance, (width() - 3) / 2, Terrain.EMPTY_SP);
        } else {
            Painter.drawInside(level, this, entrance, (height() - 3) / 2, Terrain.EMPTY_SP);
        }
        entrance.set(Door.Type.HIDDEN);

        int n = Random.IntRange(2, 3);
        HashMap<Class<? extends Scroll>, Float> chances = new HashMap<>(scrollChances);
        for (int i = 0; i < n; i++) {
            int pos;
            do {
                pos = level.pointToCell(random());
            } while (level.map[pos] != Terrain.EMPTY_SP || level.heaps.get(pos) != null);

            Class<? extends Scroll> scrollCls = Random.chances(chances);
            chances.put(scrollCls, 0f);

            if (ExoticScroll.regToExo.containsKey(scrollCls)) {
                if (Random.Float() < ExoticCrystals.consumableExoticChance()) {
                    scrollCls = ExoticScroll.regToExo.get(scrollCls);
                }
            }

            level.drop(Reflection.newInstance(scrollCls), pos);
        }
    }
}
