

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class CausticSlimeSprite extends MobSprite {

    public CausticSlimeSprite() {
        super();

        texture(Assets.Sprites.SLIME);

        TextureFilm frames = new TextureFilm(texture, 14, 12);

        int c = 9;

        idle = new Animation(3, true);
        idle.frames(frames, c, c + 1, c + 1, c);

        run = new Animation(10, true);
        run.frames(frames, c, c + 2, c + 3, c + 3, c + 2, c);

        attack = new Animation(15, false);
        attack.frames(frames, c + 2, c + 3, c + 4, c + 6, c + 5);

        die = new Animation(10, false);
        die.frames(frames, c, c + 5, c + 6, c + 7);

        play(idle);
    }
}
