package com.nopalsoft.dosmil.game_objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.nopalsoft.dosmil.Assets

class BoardPiece(var currentPosition: Int, worth: Int) : Actor() {
    var justChanged = false

    private val size = 110F // Final size of the tab

    private val positionsMap = LinkedHashMap<Int, Vector2>()

    // The value worth of this BoardPiece
    var worth = 0 // I made this piece private because when I change its value I also have to change the image of this piece.
        set(worth) {
            field = worth
            keyframe = when (worth) {
                2 -> Assets.piece2AtlasRegion
                4 -> Assets.piece4AtlasRegion
                8 -> Assets.piece8AtlasRegion
                16 -> Assets.piece16AtlasRegion
                32 -> Assets.piece32AtlasRegion
                64 -> Assets.piece64AtlasRegion
                128 -> Assets.piece128AtlasRegion
                256 -> Assets.piece256AtlasRegion
                512 -> Assets.piece512AtlasRegion
                1024 -> Assets.piece1024AtlasRegion
                2048 -> Assets.piece2048AtlasRegion
                0 -> Assets.emptyPieceAtlasRegion
                else -> Assets.emptyPieceAtlasRegion
            }
        }
    private var keyframe: TextureRegion? = null

    init {
        width = size
        height = size
        setOrigin(size / 2f, size / 2f)


        this.worth = worth

        if (worth != 0) {
            // If the piece is worth 0, it is a blue square that has nothing.
            setScale(.8f)
            addAction(Actions.scaleTo(1f, 1f, .25f))
            Gdx.app.log("Se creo pieza en ", currentPosition.toString())
        }

        positionsMap[0] = Vector2(20f, 350f)
        positionsMap[1] = Vector2(130f, 350f)
        positionsMap[2] = Vector2(240f, 350f)
        positionsMap[3] = Vector2(350f, 350f)
        positionsMap[4] = Vector2(20f, 240f)
        positionsMap[5] = Vector2(130f, 240f)
        positionsMap[6] = Vector2(240f, 240f)
        positionsMap[7] = Vector2(350f, 240f)
        positionsMap[8] = Vector2(20f, 130f)
        positionsMap[9] = Vector2(130f, 130f)
        positionsMap[10] = Vector2(240f, 130f)
        positionsMap[11] = Vector2(350f, 130f)
        positionsMap[12] = Vector2(20f, 20f)
        positionsMap[13] = Vector2(130f, 20f)
        positionsMap[14] = Vector2(240f, 20f)
        positionsMap[15] = Vector2(350f, 20f)

        setPosition(positionsMap[currentPosition]!!.x, positionsMap[currentPosition]!!.y)
    }

    override fun act(delta: Float) {
        justChanged = false
        super.act(delta)
    }

    fun moveToPosition(destinationPosition: Int) {
        currentPosition = destinationPosition
        Gdx.app.log("Move to ", destinationPosition.toString())
        addAction(Actions.moveTo(positionsMap[currentPosition]!!.x, positionsMap[currentPosition]!!.y, .075f))
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(keyframe!!, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
    }

}
