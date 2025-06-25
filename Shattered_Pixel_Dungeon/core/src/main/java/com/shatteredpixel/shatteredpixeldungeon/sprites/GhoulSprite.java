

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class GhoulSprite extends MobSprite {

    private final Animation crumple;

    public GhoulSprite() {
        super();

        texture(Assets.Sprites.GHOUL);

        TextureFilm frames = new TextureFilm(texture, 12, 14);

        idle = new Animation(2, true);
        idle.frames(frames, 0, 0, 0, 1);

        run = new Animation(12, true);
        run.frames(frames, 2, 3, 4, 5, 6, 7);

        attack = new Animation(12, false);
        attack.frames(frames, 0, 8, 9);

        crumple = new Animation(15, false);
        crumple.frames(frames, 0, 10, 11, 12);

        die = new Animation(15, false);
        die.frames(frames, 0, 10, 11, 12, 13);

        play(idle);
    }

    public void crumple() {
        hideEmo();
        processStateRemoval(State.PARALYSED);
        play(crumple);
    }

    @Override
    public void die() {
        if (curAnim == crumple) {
            //causes the sprite to not rise then fall again when dieing.
            die.frames[0] = die.frames[1] = die.frames[2] = die.frames[3];
        }
        super.die();
    }
}
