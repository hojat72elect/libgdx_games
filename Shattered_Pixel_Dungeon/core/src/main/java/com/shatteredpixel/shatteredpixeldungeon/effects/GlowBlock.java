

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;

public class GlowBlock extends Gizmo {

    private final CharSprite target;

    public GlowBlock(CharSprite target) {
        super();

        this.target = target;
    }

    @Override
    public void update() {
        super.update();

        //wavers between 0.4f and 0.6f once per second
        target.tint(1.33f, 1.33f, 0.83f, 0.5f + 0.1f * (float) Math.cos(Math.PI * 2 * Game.timeTotal));
    }

    public void darken() {

        target.resetColor();
        killAndErase();
    }

    public static GlowBlock lighten(CharSprite sprite) {

        GlowBlock glowBlock = new GlowBlock(sprite);
        if (sprite.parent != null) {
            sprite.parent.add(glowBlock);
        }

        return glowBlock;
    }
}
