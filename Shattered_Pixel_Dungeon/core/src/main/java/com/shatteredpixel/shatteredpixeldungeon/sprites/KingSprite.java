

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class KingSprite extends MobSprite {

    public KingSprite() {
        super();

        texture(Assets.Sprites.KING);

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        idle = new Animation(12, true);
        idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2);

        run = new Animation(15, true);
        run.frames(frames, 3, 4, 5, 6, 7, 8);

        attack = new Animation(15, false);
        attack.frames(frames, 9, 10, 11);

        die = new Animation(8, false);
        die.frames(frames, 12, 13, 14, 15);

        play(idle);
    }
}
