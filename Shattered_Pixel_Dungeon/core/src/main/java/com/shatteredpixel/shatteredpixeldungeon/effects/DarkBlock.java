

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.Gizmo;

public class DarkBlock extends Gizmo {

    private final CharSprite target;

    public DarkBlock(CharSprite target) {
        super();

        this.target = target;
    }

    @Override
    public void update() {
        super.update();

        target.brightness(0.4f);
    }

    public void lighten() {

        target.resetColor();
        killAndErase();
    }

    public static DarkBlock darken(CharSprite sprite) {

        DarkBlock darkBlock = new DarkBlock(sprite);
        if (sprite.parent != null)
            sprite.parent.add(darkBlock);

        return darkBlock;
    }
}
