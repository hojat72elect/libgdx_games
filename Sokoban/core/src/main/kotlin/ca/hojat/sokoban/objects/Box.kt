package ca.hojat.sokoban.objects

import ca.hojat.sokoban.Assets
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import kotlin.math.abs

class Box(position: Int, color: String) : Tile(position) {

    private var numColor = 0

    var isInRightEndPoint = false
    private var keyFrame: AtlasRegion? = null

    init {
        when (color) {
            "brown" -> numColor = COLOR_BROWN
            "gray" -> numColor = COLOR_GRAY
            "purple" -> numColor = COLOR_PURPLE
            "blue" -> numColor = COLOR_BLUE
            "black" -> numColor = COLOR_BLACK
            "beige" -> numColor = COLOR_BEIGE
            "yellow" -> numColor = COLOR_YELLOW
            "red" -> numColor = COLOR_RED
        }

        setTextureColor(numColor)
    }

    private fun setTextureColor(numColor: Int) {
        when (numColor) {
            COLOR_BEIGE -> keyFrame = Assets.beigeBox
            COLOR_DARK_BEIGE -> keyFrame = Assets.darkBeigeBox
            COLOR_BLACK -> keyFrame = Assets.blackBox
            COLOR_DARK_BLACK -> keyFrame = Assets.darkBlackBox
            COLOR_BLUE -> keyFrame = Assets.blueBox
            COLOR_DARK_BLUE -> keyFrame = Assets.darkBlueBox
            COLOR_BROWN -> keyFrame = Assets.brownBox
            COLOR_DARK_BROWN -> keyFrame = Assets.darkBrownBox
            COLOR_GRAY -> keyFrame = Assets.grayBox
            COLOR_DARK_GRAY -> keyFrame = Assets.darkGrayBox
            COLOR_RED -> keyFrame = Assets.redBox
            COLOR_DARK_RED -> keyFrame = Assets.darkRedBox
            COLOR_YELLOW -> keyFrame = Assets.yellowBox
            COLOR_DARK_YELLOW -> keyFrame = Assets.darkYellowBox
            COLOR_PURPLE -> keyFrame = Assets.purpleBox
            COLOR_DARK_PURPLE -> keyFrame = Assets.darkPurpleBox
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch?.draw(keyFrame!!, x, y, SIZE, SIZE)
    }

    fun setIsInEndPoint(endPoint: TargetPlatform?) {
        numColor = abs(numColor)
        isInRightEndPoint = false
        if (endPoint != null && endPoint.numColor == numColor) {
            numColor = -numColor
            isInRightEndPoint = true
        }
        setTextureColor(numColor)
    }

    companion object {
        const val COLOR_BEIGE = 1
        const val COLOR_DARK_BEIGE = -1
        const val COLOR_BLACK = 2
        const val COLOR_DARK_BLACK = -2
        const val COLOR_BLUE = 3
        const val COLOR_DARK_BLUE = -3
        const val COLOR_BROWN = 4
        const val COLOR_DARK_BROWN = -4
        const val COLOR_GRAY = 5
        const val COLOR_DARK_GRAY = -5
        const val COLOR_RED = 6
        const val COLOR_DARK_RED = -6
        const val COLOR_YELLOW = 7
        const val COLOR_DARK_YELLOW = -7
        const val COLOR_PURPLE = 8
        const val COLOR_DARK_PURPLE = -8
    }
}