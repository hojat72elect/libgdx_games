package com.bitfire.uracer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.TimeUtils
import com.bitfire.uracer.configuration.GraphicsUtils

/**
 * Encapsulates a buffered input state object that can be queried to know the individual key/button/pointer states.
 */
class Input(val viewport: Rectangle, keyFirstRepetitionDelayMs: Int, keyRepetitionSpeedMs: Int) : Disposable {

    private val buttons = IntArray(256)
    private val times = LongArray(256)
    private val repeated = BooleanArray(256)
    private val first_repeat = BooleanArray(256)
    private var anyKeyButton = 0
    private var repeatns = 0L
    private var firstrepeatns = 0L
    private var pointer: Pointer? = Pointer()

    init {
        releaseAllKeys()
        setKeyRepetitionMs(keyFirstRepetitionDelayMs, keyRepetitionSpeedMs)
        Gdx.input.setCatchBackKey(true)
    }

    override fun dispose() {
        pointer = null
    }

    fun releaseAllKeys() {
        anyKeyButton = 0
        for (i in buttons.indices) {
            buttons[i] = 0
            times[i] = 0
            repeated[i] = false
            first_repeat[i] = true
        }

        pointer!!.reset()
    }

    fun isTouching(button: MouseButton) = pointer!!.isTouching(button)
    fun isTouched(button: MouseButton) = pointer!!.isTouched(button)
    fun isTouchedInBounds(button: MouseButton) = pointer!!.isTouchedInBounds(button)
    fun isUntouched(button: MouseButton) = pointer!!.isUntouched(button)
    val isTouching = pointer!!.isTouching(MouseButton.Left)
    val x = pointer!!.touchCoords.x.toInt()
    val y = pointer!!.touchCoords.y.toInt()
    val xY = pointer!!.touchCoords

    fun setKeyRepetitionMs(firstDelayMs: Int, repetitionSpeedMs: Int) {
        repeatns = repetitionSpeedMs * 1000000L
        firstrepeatns = firstDelayMs * 1000000L
    }
    fun isRepeatedOn(keycode: Int) = repeated[keycode]
    fun isOn(keycode: Int) = (buttons[keycode] and FLAG_CUR_ON) > 0
    fun isPressed(keycode: Int) = ((buttons[keycode] and FLAG_CUR_ON) > 0) && ((buttons[keycode] and FLAG_LAST_ON) <= 0)
    fun isReleased(keycode: Int) = ((buttons[keycode] and FLAG_CUR_ON) <= 0) && ((buttons[keycode] and FLAG_LAST_ON) > 0)

    private fun updateKeyState() {
        var flag: Long
        var is_any_key_on = false
        var isKeyPressed: Boolean

        for (i in buttons.indices) {

            // acquire input
            isKeyPressed = Gdx.input.isKeyPressed(i)

            // update flags
            if (isKeyPressed)
                buttons[i] = buttons[i] or (FLAG_REAL_ON or FLAG_DELAY_ON)
            else
                buttons[i] = buttons[i] and FLAG_REAL_ON.inv()

            flag = buttons[i].toLong()

            if ((flag and FLAG_CUR_ON.toLong()) > 0)
                buttons[i] = buttons[i] or FLAG_LAST_ON
            else
                buttons[i] = buttons[i] and FLAG_LAST_ON.inv()

            if ((flag and (FLAG_DELAY_ON or FLAG_REAL_ON).toLong()) > 0) {
                buttons[i] = buttons[i] or FLAG_CUR_ON
                is_any_key_on = true
            } else
                buttons[i] = buttons[i] and FLAG_CUR_ON.inv()


            buttons[i] = buttons[i] and FLAG_DELAY_ON.inv()
        }

        flag = anyKeyButton.toLong()

        anyKeyButton = if ((flag and FLAG_CUR_ON.toLong()) > 0)
            anyKeyButton or FLAG_LAST_ON
        else
            anyKeyButton and FLAG_LAST_ON.inv()


        anyKeyButton = if (is_any_key_on)
            anyKeyButton or FLAG_CUR_ON
        else
            anyKeyButton and FLAG_LAST_ON.inv()
    }

    private fun updatePointerState() {
        val ptr = pointer

        val px = Gdx.input.x - viewport.x.toInt()
        val py = Gdx.input.y - viewport.y.toInt()
        val pointerInBounds = (px >= 0 && py >= 0 && px < viewport.width && py < viewport.height)

        val npx = px.toFloat() / viewport.width
        val npy = py.toFloat() / viewport.height

        for (b in MouseButton.entries)
            ptr!!.setTouching(b, Gdx.input.isButtonPressed(b.order), pointerInBounds)


        // update coords even if not touched
        val tx = (npx * GraphicsUtils.REFERENCE_SCREEN_WIDTH).toInt()
        val ty = (npy * GraphicsUtils.REFERENCE_SCREEN_HEIGHT).toInt()
        ptr!!.touchCoords.set(tx.toFloat(), ty.toFloat())
    }

    private fun updateRepeated() {
        for (i in buttons.indices) {
            if (isOn(i)) {
                val now = TimeUtils.nanoTime()
                if (!repeated[i]) {
                    val comparens = if (first_repeat[i]) firstrepeatns else repeatns
                    if (times[i] == -1L) times[i] = now
                    if (now - times[i] > comparens) {
                        repeated[i] = true
                        times[i] = now
                        first_repeat[i] = false
                    }
                } else {
                    repeated[i] = false
                }
            } else {
                repeated[i] = false
                times[i] = -1
                first_repeat[i] = true
            }
        }
    }

    fun tick() {
        updateKeyState()
        updatePointerState()
        updateRepeated()
    }

    /**
     * Encapsulates mouse buttons.
     */
    enum class MouseButton(val order: Int) {
        Left(Buttons.LEFT),
        Right(Buttons.RIGHT),
        Middle(Buttons.MIDDLE);
    }

    /**
     * Encapsulates the touch state for a pointer.
     */
    class Pointer {
        val touchCoords = Vector2(-1F, -1F)
        private val is_touching = BooleanArray(MouseButton.entries.size)
        private val was_touching = BooleanArray(MouseButton.entries.size)
        private val touched_in_bounds = BooleanArray(MouseButton.entries.size)

        fun reset() {
            for (b in MouseButton.entries) {
                is_touching[b.order] = false
                was_touching[b.order] = false
                touched_in_bounds[b.order] = false
            }
        }

        fun setTouching(button: MouseButton, touching: Boolean, inBounds: Boolean) {
            val i = button.order
            was_touching[i] = is_touching[i]
            is_touching[i] = touching
            if (isTouched(button)) touched_in_bounds[i] = inBounds
        }

        /**
         * Returns whether this pointer wasn't touching AND now it is.
         */
        fun isTouched(button: MouseButton) = !was_touching[button.order] && is_touching[button.order]
        fun isTouchedInBounds(button: MouseButton) = touched_in_bounds[button.order]
        fun isTouching(button: MouseButton) = is_touching[button.order]

        /**
         * Returns whether this pointer was touching AND now it is not (this is a callback for when touch is finished).
         */
        fun isUntouched(button: MouseButton) = was_touching[button.order] && !is_touching[button.order]
    }

    companion object {
        private const val FLAG_REAL_ON = 1
        private const val FLAG_DELAY_ON = 2
        private const val FLAG_CUR_ON = 4
        private const val FLAG_LAST_ON = 8
    }
}
