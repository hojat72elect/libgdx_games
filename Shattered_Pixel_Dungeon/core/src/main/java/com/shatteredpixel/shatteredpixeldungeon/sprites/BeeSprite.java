

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class BeeSprite extends MobSprite {

    public BeeSprite() {
        super();

        texture(Assets.Sprites.BEE);

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        idle = new Animation(12, true);
        idle.frames(frames, 0, 1, 1, 0, 2, 2);

        run = new Animation(15, true);
        run.frames(frames, 0, 1, 1, 0, 2, 2);

        attack = new Animation(20, false);
        attack.frames(frames, 3, 4, 5, 6);

        die = new Animation(20, false);
        die.frames(frames, 7, 8, 9, 10);

        play(idle);
    }

    @Override
    public int blood() {
        return 0xffd500;
    }
}
