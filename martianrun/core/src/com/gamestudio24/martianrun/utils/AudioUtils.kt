package com.gamestudio24.martianrun.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound

class AudioUtils private constructor() {
    val music = Companion.music

    private val preferences: Preferences
        get() = Gdx.app.getPreferences(GameManager.PREFERENCES_NAME)

    fun init() {
        Companion.music = Gdx.audio.newMusic(Gdx.files.internal(Constants.GAME_MUSIC))
        Companion.music!!.isLooping = true
        playMusic()
        Companion.jumpSound = createSound(Constants.RUNNER_JUMPING_SOUND)
        Companion.hitSound = createSound(Constants.RUNNER_HIT_SOUND)
    }

    fun createSound(soundFileName: String?): Sound {
        return Gdx.audio.newSound(Gdx.files.internal(soundFileName))
    }

    fun playMusic() {
        val musicOn = this.preferences.getBoolean(MUSIC_ON_PREFERENCE, true)
        if (musicOn) {
            Companion.music!!.play()
        }
    }

    fun playSound(sound: Sound?) {
        val soundOn = this.preferences.getBoolean(SOUND_ON_PREFERENCE, true)
        if (soundOn) {
            sound?.play()
        }
    }

    fun toggleMusic() {
        saveBoolean(MUSIC_ON_PREFERENCE, !this.preferences.getBoolean(MUSIC_ON_PREFERENCE, true))
    }

    fun toggleSound() {
        saveBoolean(SOUND_ON_PREFERENCE, !this.preferences.getBoolean(SOUND_ON_PREFERENCE, true))
    }

    private fun saveBoolean(key: String?, value: Boolean) {
        val preferences = this.preferences
        preferences.putBoolean(key, value)
        preferences.flush()
    }

    fun pauseMusic() {
        Companion.music!!.pause()
    }

    val soundRegionName: String
        get() {
            val soundOn = this.preferences.getBoolean(SOUND_ON_PREFERENCE, true)
            return if (soundOn) Constants.SOUND_ON_REGION_NAME else Constants.SOUND_OFF_REGION_NAME
        }

    val musicRegionName: String
        get() {
            val musicOn = this.preferences.getBoolean(MUSIC_ON_PREFERENCE, true)
            return if (musicOn) Constants.MUSIC_ON_REGION_NAME else Constants.MUSIC_OFF_REGION_NAME
        }

    val jumpSound = Companion.jumpSound

    val hitSound = Companion.hitSound

    companion object {
        val instance: AudioUtils = AudioUtils()
        private const val MUSIC_ON_PREFERENCE = "music_on"
        private const val SOUND_ON_PREFERENCE = "sound_on"

        private var music: Music? = null
        private var jumpSound: Sound? = null
        private var hitSound: Sound? = null

        fun dispose() {
            music!!.dispose()
            jumpSound!!.dispose()
            hitSound!!.dispose()
        }
    }
}
