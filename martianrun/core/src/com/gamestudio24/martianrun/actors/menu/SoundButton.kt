package com.gamestudio24.martianrun.actors.menu

import com.badlogic.gdx.math.Rectangle
import com.gamestudio24.martianrun.utils.AudioUtils

class SoundButton(bounds: Rectangle) : GameButton(bounds) {
    override fun getRegionName(): String? {
        return AudioUtils.instance.soundRegionName
    }

    override fun touched() {
        AudioUtils.instance.toggleSound()
    }
}
