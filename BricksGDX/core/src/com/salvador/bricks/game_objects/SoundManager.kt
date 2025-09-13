package com.salvador.bricks.game_objects

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound

object SoundManager {

    lateinit var musicBackground: Music
    lateinit var brickSound: Sound

    @JvmStatic
    fun loadSounds() {
        musicBackground = ResourceManager.getMusic("music.mp3")
        brickSound = ResourceManager.getSound("sound.ogg")
    }

    @JvmStatic
    fun playMusicBackground() {
        musicBackground.isLooping = true
        musicBackground.play()
    }

    @JvmStatic
    fun stopMusicBackground() {
        musicBackground.dispose()
        musicBackground.stop()
    }

    @JvmStatic
    fun playBrickSound() {
        brickSound.play()
    }
}
