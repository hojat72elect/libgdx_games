

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Shaman;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public abstract class ShamanSprite extends MobSprite {

    protected int boltType;

    protected abstract int texOffset();

    public ShamanSprite() {
        super();

        int c = texOffset();

        texture(Assets.Sprites.SHAMAN);

        TextureFilm frames = new TextureFilm(texture, 12, 15);

        idle = new Animation(2, true);
        idle.frames(frames, c, c, c, c + 1, c, c, c + 1, c + 1);

        run = new Animation(12, true);
        run.frames(frames, c + 4, c + 5, c + 6, c + 7);

        attack = new Animation(12, false);
        attack.frames(frames, c + 2, c + 3, c);

        zap = attack.clone();

        die = new Animation(12, false);
        die.frames(frames, c + 8, c + 9, c + 10);

        play(idle);
    }

    public void zap(int cell) {

        super.zap(cell);

        MagicMissile.boltFromChar(parent,
                boltType,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((Shaman) ch).onZapComplete();
                    }
                });
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    @Override
    public void onComplete(Animation anim) {
        if (anim == zap) {
            idle();
        }
        super.onComplete(anim);
    }

    public static class Red extends ShamanSprite {
        {
            boltType = MagicMissile.SHAMAN_RED;
        }

        @Override
        protected int texOffset() {
            return 0;
        }
    }

    public static class Blue extends ShamanSprite {
        {
            boltType = MagicMissile.SHAMAN_BLUE;
        }

        @Override
        protected int texOffset() {
            return 21;
        }
    }

    public static class Purple extends ShamanSprite {
        {
            boltType = MagicMissile.SHAMAN_PURPLE;
        }

        @Override
        protected int texOffset() {
            return 42;
        }
    }
}
