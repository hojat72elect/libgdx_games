

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class TenguSprite extends MobSprite {

    public TenguSprite() {
        super();

        texture(Assets.Sprites.TENGU);

        TextureFilm frames = new TextureFilm(texture, 14, 16);

        idle = new Animation(2, true);
        idle.frames(frames, 0, 0, 0, 1);

        run = new Animation(15, false);
        run.frames(frames, 2, 3, 4, 5, 0);

        attack = new Animation(15, false);
        attack.frames(frames, 6, 7, 7, 0);

        zap = attack.clone();

        die = new Animation(8, false);
        die.frames(frames, 8, 9, 10, 10, 10, 10, 10, 10);

        play(run);
        isMoving = true;
    }

    @Override
    public void play(Animation anim) {
        if (isMoving && anim != run) {
            synchronized (this) {
                isMoving = false;
                notifyAll();
            }
        }
        super.play(anim);
    }

    @Override
    public void move(int from, int to) {

        place(to);

        play(run);
        turnTo(from, to);

        isMoving = true;

        if (Dungeon.level.water[to]) {
            GameScene.ripple(to);
        }
    }

    @Override
    public void update() {
        if (paused && !curAnim.looped) {
            updateAnimation();
        }
        super.update();
    }

    @Override
    public void attack(int cell) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            ((MissileSprite) parent.recycle(MissileSprite.class)).
                    reset(this, cell, new TenguShuriken(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    });

            zap(ch.pos);
        } else {

            super.attack(cell);
        }
    }

    @Override
    public void onComplete(Animation anim) {
        if (anim == run) {
            synchronized (this) {
                isMoving = false;
                idle();

                notifyAll();
            }
        } else {
            super.onComplete(anim);
        }
    }

    public static class TenguShuriken extends Item {
        {
            image = ItemSpriteSheet.SHURIKEN;
        }
    }
}
