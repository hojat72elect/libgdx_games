

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class FungalSentrySprite extends MobSprite {

    private int cellToAttack;

    public FungalSentrySprite() {
        super();

        texture(Assets.Sprites.FUNGAL_SENTRY);

        TextureFilm frames = new TextureFilm(texture, 18, 18);

        idle = new Animation(0, true);
        idle.frames(frames, 0);

        run = new Animation(0, true);
        run.frames(frames, 0);

        attack = new Animation(24, false);
        attack.frames(frames, 0);

        zap = attack.clone();

        die = new Animation(12, false);
        die.frames(frames, 0);

        play(idle);
    }

    @Override
    public void attack(int cell) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            cellToAttack = cell;
            zap(cell);
        } else {

            super.attack(cell);
        }
    }

    @Override
    public void onComplete(Animation anim) {
        if (anim == zap) {
            idle();

            MagicMissile.boltFromChar(parent, MagicMissile.POISON, this, cellToAttack, new Callback() {
                @Override
                public void call() {
                    ch.onAttackComplete();
                }
            });
        } else {
            super.onComplete(anim);
        }
    }

    @Override
    public void turnTo(int from, int to) {
        //do nothing
    }

    @Override
    public int blood() {
        return 0xFF88CC44;
    }
}
