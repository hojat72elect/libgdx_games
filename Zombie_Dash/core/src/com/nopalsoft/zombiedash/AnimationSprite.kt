package com.nopalsoft.zombiedash

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Array
import kotlin.math.max
import kotlin.math.min

class AnimationSprite(
    val frameDuration: Float,
    keyFrames: Array<out Sprite?>
) {
    val animationDuration: Float
    val spriteFrames: kotlin.Array<Sprite?>
    private var playMode: PlayMode

    /**
     * Constructor, storing the frame duration and key frames.
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames     the [TextureRegion]s representing the frames.
     */
    init {
        this.animationDuration = keyFrames.size * frameDuration
        this.spriteFrames = arrayOfNulls<Sprite>(keyFrames.size)
        var i = 0
        val n = keyFrames.size
        while (i < n) {
            this.spriteFrames[i] = keyFrames.get(i)
            i++
        }

        this.playMode = PlayMode.NORMAL
    }

    /**
     * Returns a [TextureRegion] based on the so called state time. This is the amount of seconds an object has spent in the state this Animation instance represents, e.g. running, jumping and
     * so on. The mode specifies whether the animation is looping or not.
     *
     * @param stateTime the time spent in the state represented by this animation.
     * @param looping   whether the animation is looping or not.
     * @return the TextureRegion representing the frame of animation for the given state time.
     */
    fun getKeyFrame(stateTime: Float, looping: Boolean): Sprite? {
        // we set the play mode by overriding the previous mode based on looping
        // parameter value
        val oldPlayMode = playMode
        if (looping
            && (playMode == PlayMode.NORMAL || playMode == PlayMode.REVERSED)
        ) {
            if (playMode == PlayMode.NORMAL) playMode = PlayMode.LOOP
            else playMode = PlayMode.LOOP_REVERSED
        } else if (!looping
            && !(playMode == PlayMode.NORMAL || playMode == PlayMode.REVERSED)
        ) {
            if (playMode == PlayMode.LOOP_REVERSED) playMode = PlayMode.REVERSED
            else playMode = PlayMode.LOOP
        }

        val frame = getKeyFrame(stateTime)
        playMode = oldPlayMode
        return frame
    }

    /**
     * Returns a [TextureRegion] based on the so called state time. This is the amount of seconds an object has spent in the state .
     *
     * @return the TextureRegion representing the frame of animation for the given state time.
     */
    fun getKeyFrame(stateTime: Float): Sprite? {
        val frameNumber = getKeyFrameIndex(stateTime)
        return spriteFrames[frameNumber]
    }

    /**
     * Returns the current frame number.
     *
     * @return current frame number
     */
    fun getKeyFrameIndex(stateTime: Float): Int {
        if (spriteFrames.size == 1) return 0

        var frameNumber = (stateTime / frameDuration).toInt()
        when (playMode) {
            PlayMode.NORMAL -> frameNumber = min(spriteFrames.size - 1, frameNumber)
            PlayMode.LOOP -> frameNumber = frameNumber % spriteFrames.size
            PlayMode.LOOP_PINGPONG -> {
                frameNumber = frameNumber % ((spriteFrames.size * 2) - 2)
                if (frameNumber >= spriteFrames.size) frameNumber = (spriteFrames.size - 2
                        - (frameNumber - spriteFrames.size))
            }

            PlayMode.LOOP_RANDOM -> frameNumber = MathUtils.random(spriteFrames.size - 1)
            PlayMode.REVERSED -> frameNumber = max(spriteFrames.size - frameNumber - 1, 0)
            PlayMode.LOOP_REVERSED -> {
                frameNumber = frameNumber % spriteFrames.size
                frameNumber = spriteFrames.size - frameNumber - 1
            }
        }

        return frameNumber
    }

    /**
     * Defines possible playback modes for an [Animation].
     */
    enum class PlayMode {
        NORMAL, REVERSED, LOOP, LOOP_REVERSED, LOOP_PINGPONG, LOOP_RANDOM,
    }
}
