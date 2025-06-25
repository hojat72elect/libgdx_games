

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollGuard;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class GnollGuardSprite extends MobSprite {

    private Emitter earthArmor;

    public GnollGuardSprite() {
        super();

        texture(Assets.Sprites.GNOLL_GUARD);

        TextureFilm frames = new TextureFilm(texture, 12, 16);

        idle = new Animation(2, true);
        idle.frames(frames, 0, 0, 0, 1, 0, 0, 1, 1);

        run = new Animation(12, true);
        run.frames(frames, 4, 5, 6, 7);

        attack = new Animation(12, false);
        attack.frames(frames, 2, 3, 0);

        die = new Animation(12, false);
        die.frames(frames, 8, 9, 10);

        play(idle);
    }

    @Override
    public void link(Char ch) {
        super.link(ch);

        if (ch instanceof GnollGuard && ((GnollGuard) ch).hasSapper()) {
            setupArmor();
        }
    }

    public void setupArmor() {
        if (earthArmor == null) {
            earthArmor = emitter();
            earthArmor.fillTarget = false;
            earthArmor.y = height() / 2f;
            earthArmor.x = (2 * scale.x);
            earthArmor.width = width() - (4 * scale.x);
            earthArmor.height = height() - (10 * scale.y);
            earthArmor.pour(EarthParticle.SMALL, 0.15f);
        }
    }

    public void loseArmor() {
        if (earthArmor != null) {
            earthArmor.on = false;
            earthArmor = null;
        }
    }

    @Override
    public void update() {
        super.update();

        if (earthArmor != null) {
            earthArmor.visible = visible;
        }
    }

    @Override
    public void die() {
        super.die();
        if (earthArmor != null) {
            earthArmor.on = false;
            earthArmor = null;
        }
    }

    @Override
    public void kill() {
        super.kill();
        if (earthArmor != null) {
            earthArmor.on = false;
            earthArmor = null;
        }
    }
}
