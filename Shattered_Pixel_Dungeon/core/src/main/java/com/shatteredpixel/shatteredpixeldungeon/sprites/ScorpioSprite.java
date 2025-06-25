

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class ScorpioSprite extends MobSprite {

    private int cellToAttack;

    public ScorpioSprite() {
        super();

        texture(Assets.Sprites.SCORPIO);

        TextureFilm frames = new TextureFilm(texture, 17, 17);

        idle = new Animation(12, true);
        idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 2, 1, 2);

        run = new Animation(8, true);
        run.frames(frames, 5, 5, 6, 6);

        attack = new Animation(15, false);
        attack.frames(frames, 0, 3, 4);

        zap = attack.clone();

        die = new Animation(12, false);
        die.frames(frames, 0, 7, 8, 9, 10);

        play(idle);
    }

    @Override
    public int blood() {
        return 0xFF44FF22;
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

            ((MissileSprite) parent.recycle(MissileSprite.class)).
                    reset(this, cellToAttack, new ScorpioShot(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    });
        } else {
            super.onComplete(anim);
        }
    }

    public class ScorpioShot extends Item {
        {
            image = ItemSpriteSheet.FISHING_SPEAR;
        }
    }
}
