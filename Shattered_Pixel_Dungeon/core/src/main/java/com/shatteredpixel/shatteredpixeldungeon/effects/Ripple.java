

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

public class Ripple extends Image {

    private static final float TIME_TO_FADE = 0.5f;

    private float time;

    public Ripple() {
        super(Effects.get(Effects.Type.RIPPLE));
    }

    public void reset(int p) {
        revive();

        x = (p % Dungeon.level.width()) * DungeonTilemap.SIZE;
        y = (p / Dungeon.level.width()) * DungeonTilemap.SIZE;

        origin.set(width / 2, height / 2);
        scale.set(0);

        time = TIME_TO_FADE;
    }

    @Override
    public void update() {
        super.update();

        if ((time -= Game.elapsed) <= 0) {
            kill();
        } else {
            float p = time / TIME_TO_FADE;
            scale.set(1 - p);
            alpha(p);
        }
    }
}
