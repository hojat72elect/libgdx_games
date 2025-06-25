

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.TextureFilm;

public class PiranhaSprite extends MobSprite {

    public PiranhaSprite() {
        super();

        renderShadow = false;
        perspectiveRaise = 0.2f;

        texture(Assets.Sprites.PIRANHA);

        TextureFilm frames = new TextureFilm(texture, 12, 16);

        idle = new Animation(8, true);
        idle.frames(frames, 0, 1, 2, 1);

        run = new Animation(20, true);
        run.frames(frames, 0, 1, 2, 1);

        attack = new Animation(20, false);
        attack.frames(frames, 3, 4, 5, 6, 7, 8, 9, 10, 11);

        die = new Animation(4, false);
        die.frames(frames, 12, 13, 14);

        play(idle);
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        renderShadow = false;
    }

    @Override
    public void onComplete(Animation anim) {
        super.onComplete(anim);

        if (anim == attack) {
            GameScene.ripple(ch.pos);
        }
    }
}
