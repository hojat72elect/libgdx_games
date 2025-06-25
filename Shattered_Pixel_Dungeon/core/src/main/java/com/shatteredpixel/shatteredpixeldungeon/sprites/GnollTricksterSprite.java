

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ParalyticDart;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class GnollTricksterSprite extends MobSprite {

    private final Animation cast;

    public GnollTricksterSprite() {
        super();

        texture(Assets.Sprites.GNOLL);

        TextureFilm frames = new TextureFilm(texture, 12, 15);

        int c = 42;

        idle = new MovieClip.Animation(2, true);
        idle.frames(frames, c, c, c, 1 + c, c, c, 1 + c, 1 + c);

        run = new MovieClip.Animation(12, true);
        run.frames(frames, 4 + c, 5 + c, 6 + c, 7 + c);

        attack = new MovieClip.Animation(12, false);
        attack.frames(frames, 2 + c, 3 + c, c);

        cast = attack.clone();

        die = new Animation(12, false);
        die.frames(frames, 8 + c, 9 + c, 10 + c);

        play(idle);
    }

    @Override
    public void attack(int cell) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            ((MissileSprite) parent.recycle(MissileSprite.class)).
                    reset(this, cell, new ParalyticDart(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    });

            play(cast);
            turnTo(ch.pos, cell);
        } else {

            super.attack(cell);
        }
    }
}
