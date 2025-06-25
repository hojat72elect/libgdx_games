

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class Fireball extends MovieClip {

    private static boolean second = false;

    public Fireball() {
        this(second);
        second = !second;
    }

    public Fireball(boolean second) {

        if (PixelScene.landscape()) {
            texture("effects/fireball-tall.png");
            TextureFilm frames = new TextureFilm(texture, 61, 61);
            MovieClip.Animation anim = new MovieClip.Animation(24, true);
            anim.frames(frames, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
            play(anim);
        } else {
            texture("effects/fireball-short.png");
            TextureFilm frames = new TextureFilm(texture, 47, 47);
            MovieClip.Animation anim = new MovieClip.Animation(24, true);
            anim.frames(frames, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
            play(anim);
        }

        //second fireball is flipped and has its animation offset
        if (second) {
            flipHorizontal = true;
            curFrame = 12;
            frame(curAnim.frames[curFrame]);
        }
    }
}
