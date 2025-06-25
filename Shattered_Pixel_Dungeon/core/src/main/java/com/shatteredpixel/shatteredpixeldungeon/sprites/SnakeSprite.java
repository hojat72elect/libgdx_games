

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SnakeSprite extends MobSprite {

    public SnakeSprite() {
        super();

        texture(Assets.Sprites.SNAKE);

        TextureFilm frames = new TextureFilm(texture, 12, 11);

        //many frames here as we want the rising/falling to be slow but the tongue to be fast
        idle = new Animation(10, true);
        idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 2, 1, 1);

        run = new Animation(8, true);
        run.frames(frames, 4, 5, 6, 7);

        attack = new Animation(15, false);
        attack.frames(frames, 8, 9, 10, 9, 0);

        die = new Animation(10, false);
        die.frames(frames, 11, 12, 13);

        play(idle);
    }
}
