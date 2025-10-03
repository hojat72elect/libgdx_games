package com.bitfire.uracer.game.logic.gametasks.sounds

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.utils.Disposable
import com.bitfire.uracer.URacer
import com.bitfire.uracer.game.logic.gametasks.PlayerClient

abstract class SoundEffect : PlayerClient(), Disposable {

    @JvmField
    protected var isPaused = false

    open fun stop() {}
    open fun gameRestart() {}
    open fun gameReset() {}
    open fun tick() {}
    open fun gamePause() {
        isPaused = true
    }

    open fun gameResume() {
        isPaused = false
    }

    companion object {
        private const val WaitLimit = 1_000

        // implements a workaround for Android, need to async-wait
        // for sound loaded but libgdx doesn't expose anything for this!
        private const val ThrottleMs = 100

        @JvmStatic
        fun play(sound: Sound, volume: Float): Long {
            if (URacer.Game.isDesktop()) {
                return sound.play(volume)
            } else {
                var waitCounter = 0
                var soundId: Long = 0

                var ready = false
                while (!ready && waitCounter < WaitLimit) {
                    soundId = sound.play(volume)
                    ready = (soundId != 0L)
                    waitCounter++
                    try {
                        Thread.sleep(ThrottleMs.toLong())
                    } catch (_: InterruptedException) {
                    }
                }

                return soundId
            }
        }

        @JvmStatic
        fun loop(sound: Sound, volume: Float): Long {
            if (URacer.Game.isDesktop()) {
                return sound.loop(volume)
            } else {
                var waitCounter = 0
                var soundId = 0L

                var ready = false
                while (!ready && waitCounter < WaitLimit) {
                    soundId = sound.loop(volume)
                    ready = (soundId != 0L)
                    waitCounter++
                    try {
                        Thread.sleep(ThrottleMs.toLong())
                    } catch (_: InterruptedException) {
                    }
                }

                return soundId
            }
        }
    }
}
