

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.watabou.noosa.TextureFilm;

public class MimicSprite extends MobSprite {

    protected Animation advancedHiding;

    protected Animation hiding;

    {
        //adjust shadow slightly to account for 1 empty bottom pixel (used for border while hiding)
        perspectiveRaise = 5 / 16f; //5 pixels
        shadowWidth = 1f;
        shadowOffset = -0.4f;
    }

    protected int texOffset() {
        return 0;
    }

    public MimicSprite() {
        super();

        int c = texOffset();

        texture(Assets.Sprites.MIMIC);

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        advancedHiding = new Animation(1, true);
        advancedHiding.frames(frames, c);

        hiding = new Animation(1, true);
        hiding.frames(frames, 1 + c, 1 + c, 1 + c, 1 + c, 1 + c, 2 + c);

        idle = new Animation(5, true);
        idle.frames(frames, 3 + c, 3 + c, 3 + c, 4 + c, 4 + c);

        run = new Animation(10, true);
        run.frames(frames, 3 + c, 4 + c, 5 + c, 6 + c, 6 + c, 5 + c, 4 + c);

        attack = new Animation(10, false);
        attack.frames(frames, 3 + c, 7 + c, 8 + c, 9 + c);

        die = new Animation(5, false);
        die.frames(frames, 10 + c, 11 + c, 12 + c);

        play(idle);
    }

    @Override
    public void linkVisuals(Char ch) {
        super.linkVisuals(ch);
        if (ch.alignment == Char.Alignment.NEUTRAL) {
            hideMimic(ch);
        }
    }

    public void hideMimic(Char ch) {
        if (ch instanceof Mimic && ((Mimic) ch).stealthy()) {
            play(advancedHiding);
        } else {
            play(hiding);
        }
        hideSleep();
    }

    @Override
    public void showSleep() {
        if (curAnim == hiding || curAnim == advancedHiding) {
            return;
        }
        super.showSleep();
    }

    public static class Golden extends MimicSprite {
        @Override
        protected int texOffset() {
            return 16;
        }
    }

    public static class Crystal extends MimicSprite {
        @Override
        protected int texOffset() {
            return 32;
        }
    }

    public static class Ebony extends MimicSprite {
        @Override
        protected int texOffset() {
            return 48;
        }

        @Override
        public void hideMimic(Char ch) {
            super.hideMimic(ch);
            alpha(0.2f);
        }

        @Override
        public void resetColor() {
            super.resetColor();
            if (advancedHiding != null && curAnim == advancedHiding) {
                alpha(0.2f);
            }
        }

        @Override
        public void play(Animation anim) {
            if (curAnim == advancedHiding && anim != advancedHiding) {
                alpha(1f);
            }
            super.play(anim);
        }
    }
}
