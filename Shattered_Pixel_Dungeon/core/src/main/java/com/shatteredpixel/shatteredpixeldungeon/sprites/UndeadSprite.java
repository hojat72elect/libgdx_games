

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;

public class UndeadSprite extends MobSprite {

    public UndeadSprite() {
        super();

        texture(Assets.Sprites.UNDEAD);

        TextureFilm frames = new TextureFilm(texture, 12, 16);

        idle = new Animation(12, true);
        idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3);

        run = new Animation(15, true);
        run.frames(frames, 4, 5, 6, 7, 8, 9);

        attack = new Animation(15, false);
        attack.frames(frames, 14, 15, 16);

        die = new Animation(12, false);
        die.frames(frames, 10, 11, 12, 13);

        play(idle);
    }

    @Override
    public void die() {
        super.die();
        if (Dungeon.level.heroFOV[ch.pos]) {
            emitter().burst(Speck.factory(Speck.BONE), 3);
        }
    }

    @Override
    public int blood() {
        return 0xFFcccccc;
    }
}
