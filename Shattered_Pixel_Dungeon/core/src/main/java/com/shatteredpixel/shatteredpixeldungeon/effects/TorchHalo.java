

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Halo;

public class TorchHalo extends Halo {

    private final CharSprite target;

    private float phase = 0;

    public TorchHalo(CharSprite sprite) {
        super(20, 0xFFDDCC, 0.2f);
        target = sprite;
        am = 0;
    }

    @Override
    public void update() {
        super.update();

        if (phase < 0) {
            if ((phase += Game.elapsed) >= 0) {
                killAndErase();
            } else {
                scale.set((2 + phase) * radius / RADIUS);
                am = -phase * brightness;
            }
        } else if (phase < 1) {
            if ((phase += 2 * Game.elapsed) >= 1) {
                phase = 1;
            }
            scale.set(phase * radius / RADIUS);
            am = phase * brightness;
        }

        point(target.x + target.width / 2, target.y + target.height / 2);
    }

    @Override
    public void draw() {
        Blending.setLightMode();
        super.draw();
        Blending.setNormalMode();
    }

    public void putOut() {
        phase = -1;
    }
}
