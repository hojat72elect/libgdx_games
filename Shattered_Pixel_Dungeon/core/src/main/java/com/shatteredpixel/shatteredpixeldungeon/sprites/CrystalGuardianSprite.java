

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalGuardian;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public abstract class CrystalGuardianSprite extends MobSprite {

    private final Animation crumple;

    public CrystalGuardianSprite() {
        super();

        texture(Assets.Sprites.CRYSTAL_GUARDIAN);

        TextureFilm frames = new TextureFilm(texture, 12, 15);

        int c = texOffset();

        idle = new MovieClip.Animation(2, true);
        idle.frames(frames, c, c, c, c, c, 1 + c, 1 + c);

        run = new MovieClip.Animation(15, true);
        run.frames(frames, 2 + c, 3 + c, 4 + c, 5 + c, 6 + c, 7 + c);

        attack = new MovieClip.Animation(12, false);
        attack.frames(frames, 8 + c, 9 + c, 10 + c);

        die = new MovieClip.Animation(5, false);
        die.frames(frames, 11 + c, 12 + c, 13 + c, 14 + c, 15 + c, 15 + c);

        crumple = die.clone();

        //this is temporary, as ideally the sprite itself should be scaled to 15x19 or so
        scale.set(1.25f);

        play(idle);
    }

    public void crumple() {
        play(crumple);
    }

    public void endCrumple() {
        if (curAnim == crumple) {
            idle();
        }
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        if (ch instanceof CrystalGuardian && ((CrystalGuardian) ch).recovering()) {
            crumple();
        }
    }

    protected abstract int texOffset();

    public static class Blue extends CrystalGuardianSprite {
        @Override
        protected int texOffset() {
            return 0;
        }

        @Override
        public int blood() {
            return 0xFF8EE3FF;
        }
    }

    public static class Green extends CrystalGuardianSprite {
        @Override
        protected int texOffset() {
            return 21;
        }

        @Override
        public int blood() {
            return 0xFF85FFC8;
        }
    }

    public static class Red extends CrystalGuardianSprite {
        @Override
        protected int texOffset() {
            return 42;
        }

        @Override
        public int blood() {
            return 0xFFFFBB33;
        }
    }
}
