

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class GreatCrabSprite extends MobSprite {

    public GreatCrabSprite() {
        super();

        texture(Assets.Sprites.CRAB);

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        int c = 32;

        idle = new Animation(5, true);
        idle.frames(frames, c, 1 + c, c, 2 + c);

        run = new Animation(10, true); //slower run animation
        run.frames(frames, 3 + c, 4 + c, 5 + c, 6 + c);

        attack = new Animation(12, false);
        attack.frames(frames, 7 + c, 8 + c, 9 + c);

        die = new Animation(12, false);
        die.frames(frames, 10 + c, 11 + c, 12 + c, 13 + c);

        play(idle);
    }

    @Override
    public int blood() {
        return 0xFFFFEA80;
    }
}
