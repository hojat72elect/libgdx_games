

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class BatSprite extends MobSprite {

    public BatSprite() {
        super();

        texture(Assets.Sprites.BAT);

        TextureFilm frames = new TextureFilm(texture, 15, 15);

        idle = new Animation(8, true);
        idle.frames(frames, 0, 1);

        run = new Animation(12, true);
        run.frames(frames, 0, 1);

        attack = new Animation(12, false);
        attack.frames(frames, 2, 3, 0, 1);

        die = new Animation(12, false);
        die.frames(frames, 4, 5, 6);

        play(idle);
    }
}
