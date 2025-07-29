package com.gamestudio24.martianrun.actors.menu

import com.badlogic.gdx.math.Rectangle
import com.gamestudio24.martianrun.utils.Constants

class ShareButton(bounds: Rectangle, private val listener: ShareButtonListener) : GameButton(bounds) {
    override fun getRegionName(): String? {
        return Constants.SHARE_REGION_NAME
    }

    override fun touched() {
        listener.onShare()
    }

    interface ShareButtonListener {
        fun onShare()
    }
}
