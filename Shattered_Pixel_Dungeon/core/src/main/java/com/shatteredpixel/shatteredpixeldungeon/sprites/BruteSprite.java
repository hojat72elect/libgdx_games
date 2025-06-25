

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class BruteSprite extends MobSprite {

    public BruteSprite() {
        super();

        texture(Assets.Sprites.BRUTE);

        TextureFilm frames = new TextureFilm(texture, 12, 16);

        idle = new Animation(2, true);
        idle.frames(frames, 0, 0, 0, 1, 0, 0, 1, 1);

        run = new Animation(12, true);
        run.frames(frames, 4, 5, 6, 7);

        attack = new Animation(12, false);
        attack.frames(frames, 2, 3, 0);

        die = new Animation(12, false);
        die.frames(frames, 8, 9, 10);

        play(idle);
    }
}
