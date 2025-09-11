package aurelienribon.tweenengine

import java.util.Collections

/**
 * A TweenManager updates all your tweens and timelines at once. Its main interest is that it handles the tween/timeline
 * life-cycles for you, as well as the pooling constraints (if object pooling is enabled).
 * Just give it a bunch of tweens or timelines and call update() periodically, you don't need to care for anything else! Relax and
 * enjoy your animations.
 * @see Tween
 * @see Timeline
 */
class TweenManager {
    private val objects = ArrayList<BaseTween<*>>(20)
    private var isPaused = false

    /**
     * Adds a tween or timeline to the manager and starts or restarts it.
     * @return The manager, for instruction chaining.
     */
    fun add(newTween: BaseTween<*>): TweenManager {
        if (!objects.contains(newTween)) objects.add(newTween)
        if (newTween.isAutoStartEnabled) newTween.start()
        return this
    }

    /**
     * Kills every managed tweens and timelines.
     */
    fun killAll() {
        for (obj in objects) obj.kill()
    }

    /**
     * Kills every tweens associated with the given target. Will also kill every timelines containing a tween associated with the given
     * target.
     */
    fun killTarget(target: Any) {
        for (obj in objects) obj.killTarget(target)
    }

    /**
     * Pauses the manager. Further update calls won't have any effect.
     */
    fun pause() {
        isPaused = true
    }

    /**
     * Resumes the manager, if paused.
     */
    fun resume() {
        isPaused = false
    }

    /**
     * Updates every tweens with a delta time ang handles the tween life-cycles automatically. If a tween is finished, it will be
     * removed from the manager. The delta time represents the elapsed time between now and the last update call. Each tween or
     * timeline manages its local time, and adds this delta to its local time to update itself.
     * Slow motion, fast motion and backward play can be easily achieved by tweaking this delta time. Multiply it by -1 to play the
     * animation backward, or by 0.5 to play it twice slower than its normal speed.
     */
    fun update(delta: Float) {
        for (i in objects.indices.reversed()) {
            val obj = objects[i]
            if (obj.isFinished && obj.isAutoRemoveEnabled) {
                objects.removeAt(i)
                obj.free()
            }
        }

        if (isPaused.not()) {
            if (delta >= 0)
                objects.forEach { it.update(delta) }
            else
                objects.asReversed().forEach { it.update(delta) }
        }
    }

    /**
     * Gets the number of managed objects. An object may be a tween or a timeline. Note that a timeline only counts for 1 object,
     * since it manages its children itself.
     */
    fun size() = objects.size

    /**
     * Gets an immutable list of every managed object.
     * **Provided for debug purpose only.**
     */
    fun getObjects(): List<BaseTween<*>> = Collections.unmodifiableList(objects)
}
