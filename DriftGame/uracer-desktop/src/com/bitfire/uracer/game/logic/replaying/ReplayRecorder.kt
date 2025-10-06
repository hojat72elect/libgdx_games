package com.bitfire.uracer.game.logic.replaying

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Disposable
import com.bitfire.uracer.game.Time
import com.bitfire.uracer.game.actors.Car
import com.bitfire.uracer.game.actors.CarForces

class ReplayRecorder : Disposable {

     var isRecording = false
    private val time = Time()
    private val recording = Replay()
     val elapsedTicks= time.elapsed().ticks.toInt()

    override fun dispose() {
        reset()
        time.dispose()
    }

    fun reset() {
        isRecording = false
        time.stop()
        recording.reset()
    }

    fun resetTimer() {
        time.reset()
    }

    fun beginRecording(car: Car, trackId: String, userId: String) {
        isRecording = true
        recording.begin(trackId, userId, car)
        time.start()
    }

    fun add(f: CarForces): RecorderError {
        if (!isRecording)
            return RecorderError.RecordingNotEnabled

        if (!recording.add(f))
            return RecorderError.ReplayMemoryLimitReached

        return RecorderError.NoError
    }

    fun endRecording(): Replay? {
        if (!isRecording) {
            Gdx.app.log("Recorder", "Cannot end a recording that never began.")
            return null
        }
        time.stop()
        recording.end((time.elapsed().ticks).toInt())
        isRecording = false
        return recording
    }

    enum class RecorderError {
        NoError,
        RecordingNotEnabled,
        ReplayMemoryLimitReached
    }
}
