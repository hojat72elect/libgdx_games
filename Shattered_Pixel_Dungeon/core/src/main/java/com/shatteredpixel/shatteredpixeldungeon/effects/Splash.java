

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Visual;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.HashMap;

public class Splash {

    public static void at(int cell, final int color, int n) {
        at(DungeonTilemap.tileCenterToWorld(cell), color, n);
    }

    public static void at(PointF p, final int color, int n) {

        if (n <= 0) {
            return;
        }

        Emitter emitter = GameScene.emitter();
        if (emitter == null) return;
        emitter.pos(p);

        if (!FACTORIES.containsKey(color)) {
            FACTORIES.put(color, new SplashFactory());
        }
        SplashFactory fact = FACTORIES.get(color);
        fact.color = color;
        fact.dir = -3.1415926f / 2;
        fact.cone = 3.1415926f;
        emitter.burst(fact, n);
    }

    public static void at(PointF p, final float dir, final float cone, final int color, int n) {

        if (n <= 0) {
            return;
        }

        Emitter emitter = GameScene.emitter();
        if (emitter == null) return;
        emitter.pos(p);

        if (!FACTORIES.containsKey(color)) {
            FACTORIES.put(color, new SplashFactory());
        }
        SplashFactory fact = FACTORIES.get(color);
        fact.color = color;
        fact.dir = dir;
        fact.cone = cone;
        emitter.burst(fact, n);
    }

    public static void around(Visual v, final int color, int n) {
        if (n <= 0) {
            return;
        }

        Emitter emitter = GameScene.emitter();
        if (emitter == null) return;
        emitter.pos(v);

        if (!FACTORIES.containsKey(color)) {
            FACTORIES.put(color, new SplashFactory());
        }
        SplashFactory fact = FACTORIES.get(color);
        fact.color = color;
        fact.dir = -3.1415926f / 2;
        fact.cone = 3.1415926f;
        emitter.burst(fact, n);
    }

    public static void at(PointF p, final float dir, final float cone, final int color, int n, float interval) {

        if (n <= 0) {
            return;
        }

        Emitter emitter = GameScene.emitter();
        if (emitter == null) return;
        emitter.pos(p);

        if (!FACTORIES.containsKey(color)) {
            FACTORIES.put(color, new SplashFactory());
        }
        SplashFactory fact = FACTORIES.get(color);
        fact.color = color;
        fact.dir = dir;
        fact.cone = cone;
        emitter.start(fact, interval, n);
    }

    //each color has its own factory, let's multiple splash effects occur at once
    private static final HashMap<Integer, SplashFactory> FACTORIES = new HashMap<>();

    private static class SplashFactory extends Emitter.Factory {

        public int color;
        public float dir;
        public float cone;

        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            PixelParticle p = (PixelParticle) emitter.recycle(PixelParticle.Shrinking.class);

            p.reset(x, y, color, 4, Random.Float(0.5f, 1.0f));
            p.speed.polar(Random.Float(dir - cone / 2, dir + cone / 2), Random.Float(40, 80));
            p.acc.set(0, +100);
        }
    }
}
