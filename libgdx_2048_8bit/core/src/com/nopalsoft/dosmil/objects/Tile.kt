package com.nopalsoft.dosmil.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.nopalsoft.dosmil.Assets

/**
 * Each one of the tiles in the game's board.
 */
class Tile(var position: Int, worth: Int) : Actor() {


    var justChanged = false
    private var keyframe: TextureRegion? = null
    var worth: Int = 0 // I made this piece private because when I change its value I also have to change the image of this piece.
        set(worth) {
            field = worth
            keyframe = when (worth) {
                0 -> Assets.emptyTile
                2 -> Assets.tile2
                4 -> Assets.tile4
                8 -> Assets.tile8
                16 -> Assets.tile16
                32 -> Assets.tile32
                64 -> Assets.tile64
                128 -> Assets.tile128
                256 -> Assets.tile256
                512 -> Assets.tile512
                1024 -> Assets.tile1024
                2048 -> Assets.tile2048
                else -> Assets.emptyTile
            }
        }

    init {
        width = SIZE
        height = SIZE
        setOrigin(SIZE / 2f, SIZE / 2f)

        setPosition(mapPositions[position]!!.x, mapPositions[position]!!.y)
        this.worth = worth

        if (worth != 0) { // If the piece is worth 0, it is a blue square that has nothing
            setScale(.8f)
            addAction(Actions.scaleTo(1f, 1f, .25f))
            Gdx.app.log("Se creo pieza en ", position.toString())
        }
    }

    override fun act(delta: Float) {
        justChanged = false
        super.act(delta)
    }

    fun moveToPosition(pos: Int) {
        this.position = pos
        Gdx.app.log("Move to ", pos.toString())
        addAction(Actions.moveTo(mapPositions[position]!!.x, mapPositions[position]!!.y, .075f))
    }

    override fun draw(batch: Batch, parentAlpha: Float) = batch.draw(keyframe, x, y, originX, originY, width, height, scaleX, scaleY, rotation)


    companion object {
        // Positions start counting from left to right from top to bottom.
        private val mapPositions: LinkedHashMap<Int, Vector2> = hashMapOf(
            Pair(0, Vector2(20f, 350f)),
            Pair(1, Vector2(130f, 350f)),
            Pair(2, Vector2(240f, 350f)),
            Pair(3, Vector2(350f, 350f)),
            Pair(4, Vector2(20f, 240f)),
            Pair(5, Vector2(130f, 240f)),
            Pair(6, Vector2(240f, 240f)),
            Pair(7, Vector2(350f, 240f)),
            Pair(8, Vector2(20f, 130f)),
            Pair(9, Vector2(130f, 130f)),
            Pair(10, Vector2(240f, 130f)),
            Pair(11, Vector2(350f, 130f)),
            Pair(12, Vector2(20f, 20f)),
            Pair(13, Vector2(130f, 20f)),
            Pair(14, Vector2(240f, 20f)),
            Pair(15, Vector2(350f, 20f)),
        ).toMap(LinkedHashMap())


        // size of the tile
        private const val SIZE = 110f
    }
}
