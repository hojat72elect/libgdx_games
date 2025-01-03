package com.nopalsoft.zombiewars

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import kotlin.math.max
import kotlin.math.min

class AnimationSprite(
    val frameDuration: Float,
    keyFrames: com.badlogic.gdx.utils.Array<out Sprite?>
) {
    val animationDuration: Float = keyFrames.size * frameDuration
    val spriteFrames: Array<Sprite?> = arrayOfNulls(keyFrames.size)
    private var playMode: PlayMode

    /**
     * Constructor, storing the frame duration and key frames.
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames     the [TextureRegion]s representing the frames.
     */
    init {
        var i = 0
        val n = keyFrames.size
        while (i < n) {
            spriteFrames[i] = keyFrames[i]
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
            playMode = if (playMode == PlayMode.NORMAL) PlayMode.LOOP
            else PlayMode.LOOP_REVERSED
        } else if (!looping
            && !(playMode == PlayMode.NORMAL || playMode == PlayMode.REVERSED)
        ) {
            playMode = if (playMode == PlayMode.LOOP_REVERSED) PlayMode.REVERSED
            else PlayMode.LOOP
        }

        val frame = getKeyFrame(stateTime)
        playMode = oldPlayMode
        return frame
    }

    /**
     * Returns a [TextureRegion] based on the so called state time. This is the amount of seconds an object has spent in the state this Animation instance represents, e.g. running, jumping and
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
            PlayMode.NORMAL -> frameNumber = min((spriteFrames.size - 1).toDouble(), frameNumber.toDouble()).toInt()
            PlayMode.LOOP -> frameNumber = frameNumber % spriteFrames.size
            PlayMode.LOOP_PINGPONG -> {
                frameNumber = frameNumber % ((spriteFrames.size * 2) - 2)
                if (frameNumber >= spriteFrames.size) frameNumber = (spriteFrames.size - 2
                        - (frameNumber - spriteFrames.size))
            }

            PlayMode.LOOP_RANDOM -> frameNumber = MathUtils.random(spriteFrames.size - 1)
            PlayMode.REVERSED -> frameNumber = max((spriteFrames.size - frameNumber - 1).toDouble(), 0.0).toInt()
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
