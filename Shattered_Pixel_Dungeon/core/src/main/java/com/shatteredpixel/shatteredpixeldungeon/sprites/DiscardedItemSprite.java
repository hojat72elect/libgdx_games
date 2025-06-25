

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;

public class DiscardedItemSprite extends ItemSprite {

    @Override
    public void drop() {
        scale.set(1);
        am = 1;
        if (emitter != null) emitter.killAndErase();

        origin.set(width / 2, height - DungeonTilemap.SIZE / 2);
        angularSpeed = 720;
    }

    @Override
    public void update() {

        super.update();

        scale.set(scale.x -= Game.elapsed);
        y += 12 * Game.elapsed;
        if ((am -= Game.elapsed) <= 0) {
            remove();
        }
    }
}
