

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class BanditSprite extends MobSprite {

    public BanditSprite() {
        super();

        texture(Assets.Sprites.THIEF);
        TextureFilm film = new TextureFilm(texture, 12, 13);

        idle = new Animation(1, true);
        idle.frames(film, 21, 21, 21, 22, 21, 21, 21, 21, 22);

        run = new Animation(15, true);
        run.frames(film, 21, 21, 23, 24, 24, 25);

        die = new Animation(10, false);
        die.frames(film, 25, 27, 28, 29, 30);

        attack = new Animation(12, false);
        attack.frames(film, 31, 32, 33);

        idle();
    }
}
