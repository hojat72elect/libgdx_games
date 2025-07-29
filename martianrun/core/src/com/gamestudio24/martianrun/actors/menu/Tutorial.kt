package com.gamestudio24.martianrun.actors.menu

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.gamestudio24.martianrun.enums.GameState
import com.gamestudio24.martianrun.utils.AssetsManager.getSmallestFont
import com.gamestudio24.martianrun.utils.AssetsManager.getTextureRegion
import com.gamestudio24.martianrun.utils.GameManager

class Tutorial(private val bounds: Rectangle, assetsId: String?, private val text: String?) : Actor() {
    private val textureRegion: TextureRegion?
    private val font: BitmapFont

    init {
        textureRegion = getTextureRegion(assetsId)
        val sequenceAction = SequenceAction()
        sequenceAction.addAction(Actions.delay(4f))
        sequenceAction.addAction(Actions.removeActor())
        addAction(sequenceAction)
        font = getSmallestFont()
        setWidth(bounds.width)
        setHeight(bounds.height)
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (GameManager.instance.gameState == GameState.OVER) {
            remove()
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(textureRegion, bounds.x, bounds.y, bounds.width, bounds.height)
        font.drawWrapped(
            batch, text, bounds.x, bounds.y, bounds.width,
            BitmapFont.HAlignment.CENTER
        )
    }
}
