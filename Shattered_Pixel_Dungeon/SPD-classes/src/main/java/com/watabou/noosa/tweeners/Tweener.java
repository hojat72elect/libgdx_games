

package com.watabou.noosa.tweeners;

import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;

abstract public class Tweener extends Gizmo {

    public Gizmo target;

    public float interval;
    public float elapsed;

    public Listener listener;

    public Tweener(Gizmo target, float interval) {
        super();

        this.target = target;
        this.interval = interval;

        elapsed = 0;
    }

    @Override
    public void update() {
        if (elapsed < 0) {
            onComplete();
            kill();
            return;
        }
        elapsed += Game.elapsed;

        //it's better to skip this frame ahead and finish one frame early
        // if doing one more frame would result in lots of overshoot
        if ((interval - elapsed) < Game.elapsed / 2f) {
            elapsed = interval;
        }

        if (elapsed >= interval) {
            updateValues(1);
            onComplete();
            kill();
        } else {
            updateValues(elapsed / interval);
        }
    }

    public void stop(boolean complete) {
        elapsed = complete ? interval : -1;
    }

    protected void onComplete() {
        if (listener != null) {
            listener.onComplete(this);
        }
    }

    abstract protected void updateValues(float progress);

    public interface Listener {
        void onComplete(Tweener tweener);
    }
}
