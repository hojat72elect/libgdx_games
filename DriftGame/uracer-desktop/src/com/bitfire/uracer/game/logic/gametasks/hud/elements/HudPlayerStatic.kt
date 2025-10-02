package com.bitfire.uracer.game.logic.gametasks.hud.elements

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.bitfire.uracer.configuration.UserProfile
import com.bitfire.uracer.game.logic.gametasks.hud.HudElement
import com.bitfire.uracer.game.logic.gametasks.hud.elements.player.BasicInfo

class HudPlayerStatic(userProfile: UserProfile) : HudElement() {
    private val basicInfo = BasicInfo(userProfile)

    override fun dispose() {
        basicInfo.dispose()
    }

    override fun onRender(batch: SpriteBatch, cameraZoom: Float) {
        if (hasPlayer) basicInfo.render(batch)
    }
}
