

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.watabou.glwrap.Blending;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Identification extends Group {

    private static final int[] DOTS = {
            -1, -3,
            0, -3,
            +1, -3,
            -1, -2,
            +1, -2,
            +1, -1,
            0, 0,
            +1, 0,
            0, +1,
            0, +3
    };

    public Identification(PointF p) {

        for (int i = 0; i < DOTS.length; i += 2) {
            add(new Speck(p.x, p.y, DOTS[i], DOTS[i + 1]));
            add(new Speck(p.x, p.y, DOTS[i], DOTS[i + 1]));
        }
    }

    @Override
    public void update() {
        super.update();
        if (countLiving() == 0) {
            killAndErase();
        }
    }

    @Override
    public void draw() {
        Blending.setLightMode();
        super.draw();
        Blending.setNormalMode();
    }

    public static class Speck extends PixelParticle {

        private static final int COLOR = 0x4488CC;
        private static final int SIZE = 3;

        public Speck(float x0, float y0, int mx, int my) {

            super();
            color(COLOR);

            float x1 = x0 + mx * SIZE;
            float y1 = y0 + my * SIZE;

            PointF p = new PointF().polar(Random.Float(2 * PointF.PI), 8);
            x0 += p.x;
            y0 += p.y;

            float dx = x1 - x0;
            float dy = y1 - y0;

            x = x0;
            y = y0;
            speed.set(dx, dy);
            acc.set(-dx / 4, -dy / 4);

            left = lifespan = 2f;
        }

        @Override
        public void update() {
            super.update();

            am = 1 - Math.abs(left / lifespan - 0.5f) * 2;
            am *= am;
            size(am * SIZE);
        }
    }
}
