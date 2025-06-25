

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.watabou.noosa.Game;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.PointF;

public class SpawnerSprite extends MobSprite {

    public SpawnerSprite() {
        super();

        texture(Assets.Sprites.SPAWNER);

        perspectiveRaise = 8 / 16f;
        shadowOffset = 1.25f;
        shadowHeight = 0.4f;
        shadowWidth = 1f;

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        idle = new Animation(8, true);
        idle.frames(frames, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);

        run = idle.clone();

        attack = idle.clone();

        die = idle.clone();

        play(idle);
    }

    private float baseY = Float.NaN;

    @Override
    public void place(int cell) {
        super.place(cell);
        baseY = y;
    }

    @Override
    public void update() {
        super.update();
        if (!paused) {
            if (Float.isNaN(baseY)) baseY = y;
            y = baseY + (float) (Math.sin(Game.timeTotal) / 3f);
            shadowOffset = 1.25f - 0.6f * (float) (Math.sin(Game.timeTotal) / 3f);
        }
    }

    @Override
    public void die() {
        Splash.around(this, blood(), 100);
        killAndErase();
    }

    @Override
    public void bloodBurstA(PointF from, int damage) {
        if (alive) {
            super.bloodBurstA(from, damage);
        }
    }
}
