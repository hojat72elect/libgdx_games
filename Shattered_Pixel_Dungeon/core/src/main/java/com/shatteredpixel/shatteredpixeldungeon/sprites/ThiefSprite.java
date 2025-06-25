

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ThiefSprite extends MobSprite {

    public ThiefSprite() {
        super();

        texture(Assets.Sprites.THIEF);
        TextureFilm film = new TextureFilm(texture, 12, 13);

        idle = new Animation(1, true);
        idle.frames(film, 0, 0, 0, 1, 0, 0, 0, 0, 1);

        run = new Animation(15, true);
        run.frames(film, 0, 0, 2, 3, 3, 4);

        die = new Animation(10, false);
        die.frames(film, 5, 6, 7, 8, 9);

        attack = new Animation(12, false);
        attack.frames(film, 10, 11, 12, 0);

        idle();
    }
}
