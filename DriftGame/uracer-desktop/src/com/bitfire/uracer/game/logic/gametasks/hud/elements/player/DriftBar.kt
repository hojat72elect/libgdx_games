package com.bitfire.uracer.game.logic.gametasks.hud.elements.player

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.WindowedMean
import com.bitfire.uracer.URacer
import com.bitfire.uracer.game.logic.gametasks.hud.HudLabel
import com.bitfire.uracer.game.logic.gametasks.hud.Positionable
import com.bitfire.uracer.resources.Art
import com.bitfire.uracer.resources.BitmapFontFactory
import com.bitfire.uracer.utils.AlgebraMath.isZero
import com.bitfire.uracer.utils.paletteRYG
import com.bitfire.utils.ShaderLoader.fromFile

class DriftBar : Positionable() {

    private val driftStrength: WindowedMean = WindowedMean(4)
    private val texHalfMask = Art.texCircleProgressHalfMask
    private val shDriftSecs  = fromFile("progress", "progress")
    private val sprDriftSecs: Sprite
    private val sprDriftStrength: Sprite
    private val offX: Float
    private val offY: Float
    private val labelSeconds = HudLabel(BitmapFontFactory.FontFace.CurseRedYellowNew, "s", false)
    private var seconds = 0F

    init {
        labelSeconds.setAlpha(0F)

        val texHalf = Art.texCircleProgressHalf

        val w = texHalf.width.toFloat()
        val h = texHalf.height.toFloat()
        offX = w / 2
        offY = h / 2

        // drift seconds
        sprDriftSecs = Sprite(texHalf)
        sprDriftSecs.flip(false, true)
        sprDriftStrength = Sprite(texHalf)
    }

    fun reset() {
        driftStrength.clear()
    }

    override fun getWidth() = 0F
    override fun getHeight() = 0F

    fun setSeconds(seconds: Float) {
        this.seconds = MathUtils.clamp(seconds, 0F, MAX_SECONDS)
    }

    fun setDriftStrength(strength: Float) {
        driftStrength.addValue(strength)
    }

    fun showSecondsLabel() {
        labelSeconds.fadeIn(300)
    }

    fun hideSecondsLabel() {
        labelSeconds.fadeOut(800)
    }

    fun render(batch: SpriteBatch, cameraZoom: Float) {
        val timeFactor = URacer.Game.getTimeModFactor() * 0.3F
        var s = 0.55F + (timeFactor * 0.5F)
        var scl = cameraZoom * s

        labelSeconds.setScale(scl)
        labelSeconds.setString(String.format("%.02f", seconds) + "s", true)
        labelSeconds.setPosition(position.x, position.y + (90) * cameraZoom + (105) * timeFactor * cameraZoom)
        labelSeconds.render(batch)

        // circle progress for slow-mo accumulated time
        s = 0.8f + timeFactor
        scl = cameraZoom * s
        val px = position.x - offX
        var py = position.y - offY + (32 * cameraZoom * s)

        batch.setShader(shDriftSecs)
        texHalfMask.bind(1)
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0)
        shDriftSecs.setUniformi("u_texture1", 1)

        val alpha = 1F

        // player's earned drift seconds for performing time dilation
        val ratio: Float = seconds / MAX_SECONDS
        shDriftSecs.setUniformf("progress", ratio)
        sprDriftSecs.color = paletteRYG(ratio, 1F)
        sprDriftSecs.setScale(scl)
        sprDriftSecs.setPosition(px, py)
        sprDriftSecs.draw(batch, alpha)
        batch.flush()

        // player's drift strength
        val amount = driftStrength.getMean()
        if (!isZero(amount)) {
            py = position.y - offY - (32 * cameraZoom * s)
            shDriftSecs.setUniformf("progress", MathUtils.clamp(amount, 0f, 1f))

            sprDriftStrength.setColor(1f, 1f, 1f, 1f)
            sprDriftStrength.setScale(scl)
            sprDriftStrength.setPosition(px, py)
            sprDriftStrength.draw(batch, alpha)
            batch.flush()
        }

        batch.setShader(null)
    }

    companion object {
        const val MAX_SECONDS = 10F
    }
}
