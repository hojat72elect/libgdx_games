

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PrismaticImage;
import com.watabou.noosa.Game;

public class PrismaticSprite extends MirrorSprite {

    public PrismaticSprite() {
        super();

        float interval = (Game.timeTotal % 9) / 3f;
        tint(interval > 2 ? interval - 2 : Math.max(0, 1 - interval),
                interval > 1 ? Math.max(0, 2 - interval) : interval,
                interval > 2 ? Math.max(0, 3 - interval) : interval - 1, 0.5f);
    }

    @Override
    public void updateArmor() {
        updateArmor(((PrismaticImage) ch).armTier);
    }

    @Override
    public void update() {
        super.update();

        if (flashTime <= 0) {
            float interval = (Game.timeTotal % 9) / 3f;
            tint(interval > 2 ? interval - 2 : Math.max(0, 1 - interval),
                    interval > 1 ? Math.max(0, 2 - interval) : interval,
                    interval > 2 ? Math.max(0, 3 - interval) : interval - 1, 0.5f);
        }
    }
}
