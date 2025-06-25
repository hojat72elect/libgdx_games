

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.audio.Sample;

public class IceBlock extends Gizmo {

    private float phase;

    private final CharSprite target;

    public IceBlock(CharSprite target) {
        super();

        this.target = target;
        phase = 0;
    }

    @Override
    public void update() {
        super.update();

        if ((phase += Game.elapsed * 2) < 1) {
            target.tint(0.83f, 1.17f, 1.33f, phase * 0.6f);
        } else {
            target.tint(0.83f, 1.17f, 1.33f, 0.6f);
        }
    }

    public void melt() {

        target.resetColor();
        killAndErase();

        if (visible) {
            Splash.at(target.center(), 0xFFB2D6FF, 5);
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
        }
    }

    public static IceBlock freeze(CharSprite sprite) {

        IceBlock iceBlock = new IceBlock(sprite);
        if (sprite.parent != null)
            sprite.parent.add(iceBlock);

        return iceBlock;
    }
}
