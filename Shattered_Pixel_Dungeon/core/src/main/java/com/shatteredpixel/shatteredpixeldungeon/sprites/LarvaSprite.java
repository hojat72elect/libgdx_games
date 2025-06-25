

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.watabou.noosa.TextureFilm;

public class LarvaSprite extends MobSprite {

    public LarvaSprite() {
        super();

        texture(Assets.Sprites.LARVA);

        TextureFilm frames = new TextureFilm(texture, 12, 8);

        idle = new Animation(5, true);
        idle.frames(frames, 4, 4, 4, 4, 4, 5, 5);

        run = new Animation(12, true);
        run.frames(frames, 0, 1, 2, 3);

        attack = new Animation(15, false);
        attack.frames(frames, 6, 5, 7);

        die = new Animation(10, false);
        die.frames(frames, 8);

        play(idle);
    }

    @Override
    public int blood() {
        return 0xbbcc66;
    }

    @Override
    public void die() {
        Splash.at(center(), blood(), 10);
        super.die();
    }
}
