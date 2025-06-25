

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM201;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class DM201Sprite extends MobSprite {

    public DM201Sprite() {
        super();

        texture(Assets.Sprites.DM200);

        TextureFilm frames = new TextureFilm(texture, 21, 18);

        int c = 12;

        idle = new Animation(2, true);
        idle.frames(frames, c, c + 1);

        run = idle.clone();

        attack = new Animation(15, false);
        attack.frames(frames, c + 4, c + 5, c + 6);

        zap = new Animation(15, false);
        zap.frames(frames, c + 7, c + 8, c + 8, c + 7);

        die = new Animation(8, false);
        die.frames(frames, c + 9, c + 10, c + 11);

        play(idle);
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

    public void zap(int cell) {

        super.zap(cell);

        MagicMissile.boltFromChar(parent,
                MagicMissile.CORROSION,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        Sample.INSTANCE.play(Assets.Sounds.GAS);
                        ((DM201) ch).onZapComplete();
                    }
                });
        Sample.INSTANCE.play(Assets.Sounds.MISS, 1f, 1.5f);
        GLog.w(Messages.get(DM201.class, "vent"));
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
