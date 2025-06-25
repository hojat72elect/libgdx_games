

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ShieldedSprite extends MobSprite {

    public ShieldedSprite() {
        super();

        texture(Assets.Sprites.BRUTE);

        TextureFilm frames = new TextureFilm(texture, 12, 16);

        idle = new Animation(2, true);
        idle.frames(frames, 21, 21, 21, 22, 21, 21, 22, 22);

        run = new Animation(12, true);
        run.frames(frames, 25, 26, 27, 28);

        attack = new Animation(12, false);
        attack.frames(frames, 23, 24);

        die = new Animation(12, false);
        die.frames(frames, 29, 30, 31);

        play(idle);
    }
}
