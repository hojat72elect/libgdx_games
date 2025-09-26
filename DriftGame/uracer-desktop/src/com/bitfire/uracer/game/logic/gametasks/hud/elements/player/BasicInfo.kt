package com.bitfire.uracer.game.logic.gametasks.hud.elements.player

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Disposable
import com.bitfire.uracer.configuration.UserProfile
import com.bitfire.uracer.game.logic.gametasks.hud.HudLabel
import com.bitfire.uracer.resources.Art
import com.bitfire.uracer.resources.BitmapFontFactory

/**
 * Displays basic information such as player name, nation flag
 */
class BasicInfo(profile: UserProfile) : Disposable {

    private val name = HudLabel(BitmapFontFactory.FontFace.CurseRedYellowBig, profile.userName, true)
    private val flag = Art.getFlag(profile.userCountryCode)
    private val flagRegion = TextureRegion(flag)
    private val borderX  = 15F
    private val borderY = 5F
    private val w = 80F
    private val h  = 80F

    init {
        flagRegion.flip(false, true)
        name.setPosition(w + borderX * 2 + name.getWidth() / 2, 42F)
    }

    override fun dispose() {
        flag.dispose()
    }

    fun render(batch: SpriteBatch) {
        batch.draw(flagRegion, borderX, borderY, w, h)
        name.render(batch)
    }
}
