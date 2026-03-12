package ca.hojat.sokoban.objects

import ca.hojat.sokoban.Assets
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion

class TargetPlatform(position: Int, color: String) : Tile(position) {
    var numColor: Int = when (color) {
        "brown" -> Box.COLOR_BROWN
        "gray" -> Box.COLOR_GRAY
        "purple" -> Box.COLOR_PURPLE
        "blue" -> Box.COLOR_BLUE
        "black" -> Box.COLOR_BLACK
        "beige" -> Box.COLOR_BEIGE
        "yellow" -> Box.COLOR_YELLOW
        "red" -> Box.COLOR_RED
        else -> {
            // this will never happen
            throw IllegalArgumentException("Invalid color: $color")
        }
    }

    private var keyFrame: AtlasRegion? = null

    init {
        setTextureColor(numColor)
    }

    private fun setTextureColor(numColor: Int) {
        keyFrame = when (numColor) {
            Box.COLOR_BEIGE -> Assets.beigeTargetPlatform
            Box.COLOR_BLACK -> Assets.blackTargetPlatform
            Box.COLOR_BLUE -> Assets.blueTargetPlatform
            Box.COLOR_BROWN -> Assets.brownTargetPlatform
            Box.COLOR_GRAY -> Assets.grayTargetPlatform
            Box.COLOR_RED -> Assets.redTargetPlatform
            Box.COLOR_YELLOW -> Assets.yellowTargetPlatform
            Box.COLOR_PURPLE -> Assets.purpleTargetPlatform
            else -> {
                // this will never happen
                throw IllegalArgumentException("Invalid color: $numColor")
            }
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(keyFrame!!, x, y, SIZE, SIZE)
    }
}