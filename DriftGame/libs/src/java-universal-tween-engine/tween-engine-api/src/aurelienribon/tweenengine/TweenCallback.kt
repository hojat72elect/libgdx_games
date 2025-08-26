package aurelienribon.tweenengine

/**
 * TweenCallbacks are used to trigger actions at some specific times. They are used in both Tweens and Timelines. The moment when
 * the callback is triggered depends on its registered triggers:
 *
 * **BEGIN**: right after the delay (if any)
 *
 * **START**: at each iteration beginning
 *
 * **END**: at each iteration ending, before the repeat delay
 *
 * **COMPLETE**: at last END event
 *
 * **BACK_BEGIN**: at the beginning of the first backward iteration
 *
 * **BACK_START**: at each backward iteration beginning, after the repeat delay
 *
 * **BACK_END**: at each backward iteration ending
 *
 * **BACK_COMPLETE**: at last BACK_END event
 *
 * @see Tween
 * @see Timeline
 */
interface TweenCallback {
    fun onEvent(type: Int, source: BaseTween<*>)

    companion object {
        const val BEGIN = 0x01
        const val START = 0x02
        const val END = 0x04
        const val COMPLETE = 0x08
        const val BACK_BEGIN = 0x10
        const val BACK_START = 0x20
        const val BACK_END = 0x40
        const val BACK_COMPLETE = 0x80
    }
}
