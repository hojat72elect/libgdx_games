

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.watabou.glwrap.Blending;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Degradation extends Group {

    private static final int[] WEAPON = {
            +2, -2,
            +1, -1,
            0, 0,
            -1, +1,
            -2, +2,
            -2, 0,
            0, +2
    };

    private static final int[] ARMOR = {
            -2, -1,
            -1, -1,
            +1, -1,
            +2, -1,
            -2, 0,
            -1, 0,
            0, 0,
            +1, 0,
            +2, 0,
            -1, +1,
            +1, +1,
            -1, +2,
            0, +2,
            +1, +2
    };

    private static final int[] RING = {
            0, -1,
            -1, 0,
            0, 0,
            +1, 0,
            -1, +1,
            +1, +1,
            -1, +2,
            0, +2,
            +1, +2
    };

    private static final int[] WAND = {
            +2, -2,
            +1, -1,
            0, 0,
            -1, +1,
            -2, +2,
            +1, -2,
            +2, -1
    };

    public static Degradation weapon(PointF p) {
        return new Degradation(p, WEAPON);
    }

    public static Degradation armor(PointF p) {
        return new Degradation(p, ARMOR);
    }

    public static Degradation ring(PointF p) {
        return new Degradation(p, RING);
    }

    public static Degradation wand(PointF p) {
        return new Degradation(p, WAND);
    }

    private Degradation(PointF p, int[] matrix) {

        for (int i = 0; i < matrix.length; i += 2) {
            add(new Speck(p.x, p.y, matrix[i], matrix[i + 1]));
            add(new Speck(p.x, p.y, matrix[i], matrix[i + 1]));
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

        private static final int COLOR = 0xFF4422;
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
