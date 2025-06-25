

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class BlacksmithSprite extends MobSprite {

    private Emitter emitter;

    public BlacksmithSprite() {
        super();

        texture(Assets.Sprites.TROLL);

        TextureFilm frames = new TextureFilm(texture, 13, 16);

        idle = new Animation(15, true);
        idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 3);

        run = new Animation(20, true);
        run.frames(frames, 0);

        die = new Animation(20, false);
        die.frames(frames, 0);

        play(idle);
    }

    @Override
    public void link(Char ch) {
        super.link(ch);

        emitter = new Emitter();
        emitter.autoKill = false;
        emitter.pos(x + 7, y + 12);
        parent.add(emitter);
    }

    @Override
    public void update() {
        super.update();

        if (emitter != null) {
            emitter.visible = visible;
        }
    }

    @Override
    public void onComplete(Animation anim) {
        super.onComplete(anim);

        if (visible && emitter != null && anim == idle) {
            emitter.burst(Speck.factory(Speck.FORGE), 3);
            if (!Music.INSTANCE.paused()) {
                float volume = 0.2f / (Dungeon.level.distance(ch.pos, Dungeon.hero.pos));
                Sample.INSTANCE.play(Assets.Sounds.EVOKE, volume, volume, 0.8f);
            }
        }
    }
}
