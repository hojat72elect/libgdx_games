package aurelienribon.tweenengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A TweenManager updates all your tweens and timelines at once. Its main interest is that it handles the tween/timeline
 * life-cycles for you, as well as the pooling constraints (if object pooling is enabled).
 * <p/>
 * <p>
 * Just give it a bunch of tweens or timelines and call update() periodically, you don't need to care for anything else! Relax and
 * enjoy your animations.
 *
 * @see Tween
 * @see Timeline
 */
public class TweenManager {

    private final ArrayList<BaseTween<?>> objects = new ArrayList<>(20);
    private boolean isPaused = false;

    /**
     * Adds a tween or timeline to the manager and starts or restarts it.
     *
     * @return The manager, for instruction chaining.
     */
    public TweenManager add(BaseTween<?> object) {
        if (!objects.contains(object)) objects.add(object);
        if (object.isAutoStartEnabled) object.start();
        return this;
    }

    /**
     * Kills every managed tweens and timelines.
     */
    public void killAll() {
        for (int i = 0, n = objects.size(); i < n; i++) {
            BaseTween<?> obj = objects.get(i);
            obj.kill();
        }
    }

    /**
     * Kills every tweens associated to the given target. Will also kill every timelines containing a tween associated to the given
     * target.
     */
    public void killTarget(Object target) {
        for (int i = 0, n = objects.size(); i < n; i++) {
            BaseTween<?> obj = objects.get(i);
            obj.killTarget(target);
        }
    }

    /**
     * Pauses the manager. Further update calls won't have any effect.
     */
    public void pause() {
        isPaused = true;
    }

    /**
     * Resumes the manager, if paused.
     */
    public void resume() {
        isPaused = false;
    }

    /**
     * Updates every tweens with a delta time ang handles the tween life-cycles automatically. If a tween is finished, it will be
     * removed from the manager. The delta time represents the elapsed time between now and the last update call. Each tween or
     * timeline manages its local time, and adds this delta to its local time to update itself.
     * Slow motion, fast motion and backward play can be easily achieved by tweaking this delta time. Multiply it by -1 to play the
     * animation backward, or by 0.5 to play it twice slower than its normal speed.
     */
    public void update(float delta) {
        for (int i = objects.size() - 1; i >= 0; i--) {
            BaseTween<?> obj = objects.get(i);
            if (obj.isFinished() && obj.isAutoRemoveEnabled) {
                objects.remove(i);
                obj.free();
            }
        }

        if (!isPaused) {
            if (delta >= 0) {
                for (int i = 0, n = objects.size(); i < n; i++)
                    objects.get(i).update(delta);
            } else {
                for (int i = objects.size() - 1; i >= 0; i--)
                    objects.get(i).update(delta);
            }
        }
    }

    /**
     * Gets the number of managed objects. An object may be a tween or a timeline. Note that a timeline only counts for 1 object,
     * since it manages its children itself.
     */
    public int size() {
        return objects.size();
    }

    /**
     * Gets an immutable list of every managed object.
     * <p/>
     * <b>Provided for debug purpose only.</b>
     */
    public List<BaseTween<?>> getObjects() {
        return Collections.unmodifiableList(objects);
    }
}
