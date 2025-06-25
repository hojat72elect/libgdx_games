

package com.shatteredpixel.shatteredpixeldungeon.levels.features;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;

public class Door {

    public static void enter(int pos) {
        Level.set(pos, Terrain.OPEN_DOOR);
        GameScene.updateMap(pos);

        if (Dungeon.level.heroFOV[pos]) {
            Dungeon.observe();
            Sample.INSTANCE.play(Assets.Sounds.OPEN);
        }
    }

    public static void leave(int pos) {
        int chars = 0;

        for (Char ch : Actor.chars()) {
            if (ch.pos == pos) chars++;
        }

        //door does not shut if anything else is also on it
        if (Dungeon.level.heaps.get(pos) == null && chars <= 1) {
            Level.set(pos, Terrain.DOOR);
            GameScene.updateMap(pos);
            if (Dungeon.level.heroFOV[pos])
                Dungeon.observe();
        }
    }
}
