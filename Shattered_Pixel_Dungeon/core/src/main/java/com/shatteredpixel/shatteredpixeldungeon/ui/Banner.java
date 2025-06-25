

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

public class Banner extends Image {

    private enum State {
        FADE_IN, STATIC, FADE_OUT
    }

    private State state;

    private float time;

    private int color;
    private float fadeTime;
    private float showTime;

    public Banner(Image sample) {
        super();
        copy(sample);
        alpha(0);
    }

    public Banner(Object tx) {
        super(tx);
        alpha(0);
    }

    public void show(int color, float fadeTime, float showTime) {

        this.color = color;
        this.fadeTime = fadeTime;
        this.showTime = showTime;

        state = State.FADE_IN;

        time = fadeTime;
    }

    public void show(int color, float fadeTime) {
        show(color, fadeTime, Float.MAX_VALUE);
    }

    @Override
    public void update() {
        super.update();

        time -= Game.elapsed;
        if (time >= 0) {

            float p = time / fadeTime;

            switch (state) {
                case FADE_IN:
                    tint(color, p);
                    alpha(1 - p);
                    break;
                case STATIC:
                    resetColor();
                    break;
                case FADE_OUT:
                    resetColor();
                    alpha(p);
                    break;
            }
        } else {

            switch (state) {
                case FADE_IN:
                    time = showTime;
                    state = State.STATIC;
                    break;
                case STATIC:
                    time = fadeTime;
                    state = State.FADE_OUT;
                    break;
                case FADE_OUT:
                    killAndErase();
                    break;
            }
        }
    }
}
