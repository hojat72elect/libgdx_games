

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.GameMath;

public class StatueSprite extends MobSprite {

    public StatueSprite() {
        super();

        texture(Assets.Sprites.STATUE);

        TextureFilm frames = new TextureFilm(texture, 12, 15);

        idle = new Animation(2, true);
        idle.frames(frames, 0, 0, 0, 0, 0, 1, 1);

        run = new Animation(15, true);
        run.frames(frames, 2, 3, 4, 5, 6, 7);

        attack = new Animation(12, false);
        attack.frames(frames, 8, 9, 10);

        die = new Animation(5, false);
        die.frames(frames, 11, 12, 13, 14, 15, 15);

        play(idle);
    }

    private static final int[] tierFrames = {0, 21, 32, 43, 54, 65};

    public void setArmor(int tier) {
        int c = tierFrames[(int) GameMath.gate(0, tier, 5)];

        TextureFilm frames = new TextureFilm(texture, 12, 15);

        idle.frames(frames, c, c, c, c, c, 1 + c, 1 + c);
        run.frames(frames, 2 + c, 3 + c, 4 + c, 5 + c, 6 + c, 7 + c);
        attack.frames(frames, 8 + c, 9 + c, 10 + c);
        //death animation is always armorless

        play(idle, true);
    }

    @Override
    public int blood() {
        return 0xFFcdcdb7;
    }
}
