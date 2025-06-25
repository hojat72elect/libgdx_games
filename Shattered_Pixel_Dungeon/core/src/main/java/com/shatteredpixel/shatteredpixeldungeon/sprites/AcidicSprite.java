

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class AcidicSprite extends ScorpioSprite {

    public AcidicSprite() {
        super();

        texture(Assets.Sprites.SCORPIO);

        TextureFilm frames = new TextureFilm(texture, 17, 17);

        idle = new Animation(12, true);
        idle.frames(frames, 15, 15, 15, 15, 15, 15, 15, 15, 16, 17, 16, 17, 16, 17);

        run = new Animation(4, true);
        run.frames(frames, 20, 21);

        attack = new Animation(15, false);
        attack.frames(frames, 15, 18, 19);

        zap = attack.clone();

        die = new Animation(12, false);
        die.frames(frames, 15, 22, 23, 24, 25);

        play(idle);
    }

    @Override
    public int blood() {
        return 0xFF66FF22;
    }
}
