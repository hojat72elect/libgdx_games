

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class TormentedSpiritSprite extends MobSprite {

    public TormentedSpiritSprite() {
        super();

        texture(Assets.Sprites.WRAITH);

        TextureFilm frames = new TextureFilm(texture, 14, 15);

        int c = 9;

        idle = new Animation(5, true);
        idle.frames(frames, c, c + 1);

        run = new Animation(10, true);
        run.frames(frames, c, c + 1);

        attack = new Animation(10, false);
        attack.frames(frames, c, c + 2, c + 3);

        die = new Animation(8, false);
        die.frames(frames, c, c + 4, c + 5, c + 6, c + 7);

        play(idle);
    }

    @Override
    public int blood() {
        return 0x88BB0000;
    }
}
