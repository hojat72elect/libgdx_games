

package com.shatteredpixel.shatteredpixeldungeon.sprites;


import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class NewbornElementalSprite extends MobSprite {

    public NewbornElementalSprite() {
        super();

        texture(Assets.Sprites.ELEMENTAL);

        int ofs = 21;

        TextureFilm frames = new TextureFilm(texture, 12, 14);

        idle = new MovieClip.Animation(10, true);
        idle.frames(frames, ofs, ofs + 1, ofs + 2);

        run = new MovieClip.Animation(12, true);
        run.frames(frames, ofs, ofs + 1, ofs + 3);

        attack = new MovieClip.Animation(15, false);
        attack.frames(frames, ofs + 4, ofs + 5, ofs + 6);

        die = new MovieClip.Animation(15, false);
        die.frames(frames, ofs + 7, ofs + 8, ofs + 9, ofs + 10, ofs + 11, ofs + 12, ofs + 13, ofs + 12);

        play(idle);
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        add(CharSprite.State.BURNING);
    }

    @Override
    public void die() {
        super.die();
        processStateRemoval(CharSprite.State.BURNING);
    }

    @Override
    public int blood() {
        return 0xFFFF7D13;
    }
}
