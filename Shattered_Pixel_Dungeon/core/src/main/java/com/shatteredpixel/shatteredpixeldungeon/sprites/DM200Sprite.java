

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM200;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class DM200Sprite extends MobSprite {

    public DM200Sprite() {
        super();

        texture(Assets.Sprites.DM200);

        TextureFilm frames = new TextureFilm(texture, 21, 18);

        idle = new Animation(10, true);
        idle.frames(frames, 0, 1);

        run = new Animation(10, true);
        run.frames(frames, 2, 3);

        attack = new Animation(15, false);
        attack.frames(frames, 4, 5, 6);

        zap = new Animation(15, false);
        zap.frames(frames, 7, 8, 8, 7);

        die = new Animation(8, false);
        die.frames(frames, 9, 10, 11);

        play(idle);
    }

    public void zap(int cell) {

        super.zap(cell);

        MagicMissile.boltFromChar(parent,
                MagicMissile.SPECK + Speck.TOXIC,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((DM200) ch).onZapComplete();
                    }
                });
        Sample.INSTANCE.play(Assets.Sounds.GAS);
        GLog.w(Messages.get(DM200.class, "vent"));
    }

    @Override
    public void place(int cell) {
        if (parent != null) parent.bringToFront(this);
        super.place(cell);
    }

    @Override
    public void die() {
        emitter().burst(Speck.factory(Speck.WOOL), 8);
        super.die();
    }

    @Override
    public void onComplete(Animation anim) {
        if (anim == zap) {
            idle();
        }
        super.onComplete(anim);
    }

    @Override
    public int blood() {
        return 0xFFFFFF88;
    }
}
