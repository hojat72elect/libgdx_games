package com.gamestudio24.martianrun.actors.menu

import com.badlogic.gdx.math.Rectangle
import com.gamestudio24.martianrun.utils.AudioUtils

class MusicButton(bounds: Rectangle) : GameButton(bounds) {
    override fun getRegionName(): String? {
        return AudioUtils.instance.musicRegionName
    }

    override fun touched() {
        val music = AudioUtils.instance.music
        if (music != null) {
            if (music.isPlaying) {
                AudioUtils.instance.pauseMusic()
            }
        }
        AudioUtils.instance.toggleMusic()
        AudioUtils.instance.playMusic()
    }
}
