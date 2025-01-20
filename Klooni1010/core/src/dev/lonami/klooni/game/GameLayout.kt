package dev.lonami.klooni.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import dev.lonami.klooni.actors.Band
import dev.lonami.klooni.actors.ShopCard
import kotlin.math.min

/**
 * Helper class to calculate the size of each element.
 *
 * In the future, perhaps this could handle landscape mode differently.
 * For example, the boardHeight on the left and the piece holder on the right.
 */
class GameLayout {

    private val screenWidth = Gdx.graphics.width
    private val marginWidth = screenWidth * 0.05F
    private val availableWidth = screenWidth - marginWidth * 2
    private val screenHeight = Gdx.graphics.height
    private val logoHeight = screenHeight * 0.10F
    private val scoreHeight = screenHeight * 0.15F
    private val boardHeight = screenHeight * 0.50F
    private val pieceHolderHeight = screenHeight * 0.25F
    private val shopCardHeight = screenHeight * 0.15F

    /**
     * These methods take any of the custom objects used in the game
     * and positions them accordingly on the screen, by using relative
     * coordinates. Since these objects are not actors and we cannot
     * add them to a table (and that would probably be harder), this approach
     * was used. Note that all these are using Y-up coordinates.
     */
    fun update(scorer: BaseScorer) {
        val cupSize = min(scoreHeight, scorer.cupTexture!!.height.toFloat())
        val area = Rectangle(marginWidth, pieceHolderHeight + boardHeight, availableWidth, scoreHeight)

        scorer.cupArea.set(area.x + area.width * 0.5f - cupSize * 0.5f, area.y, cupSize, cupSize)
        scorer.currentScoreLabel.setBounds(area.x, area.y, area.width * 0.5f - cupSize * 0.5f, area.height)
        scorer.highScoreLabel.setBounds(area.x + area.width * 0.5f + cupSize * 0.5f, area.y, area.width * 0.5f - cupSize * 0.5f, area.height)
    }

    /**
     * This is a special case, because we want to position the label on top of the cup.
     */
    fun updateTimeLeftLabel(timeLeftLabel: Label) {
        timeLeftLabel.setBounds(0f, screenHeight - logoHeight, screenWidth.toFloat(), logoHeight)
    }

    fun update(board: Board) {
        // We can't leave our area, so pick the minimum between available height and width to determine an appropriated cell size.
        val boardSize = min(availableWidth, boardHeight)
        board.cellSize = boardSize / board.cellCount

        // Now that we know the board size, we can center the board on the screen
        board.pos.set(screenWidth * 0.5f - boardSize * 0.5f, pieceHolderHeight)
    }

    fun update(holder: PieceHolder): Rectangle = holder.area.set(marginWidth, 0f, availableWidth, pieceHolderHeight)

    fun update(band: Band) {
        val area = Rectangle(0f, pieceHolderHeight + boardHeight, screenWidth.toFloat(), scoreHeight)
        band.setBounds(area.x, area.y, area.width, area.height)

        /*
         * Let the band have the following shape:
         * 10% (100) padding
         * 35% (90%) score label
         * 10% (55%) padding
         * 35% (45%) info label
         * 10% (10%) padding
         */
        band.scoreBounds.set(area.x, area.y + area.height * 0.55f, area.width, area.height * 0.35f)
        band.infoBounds.set(area.x, area.y + area.height * 0.10f, area.width, area.height * 0.35f)
    }

    fun update(card: ShopCard) {
        card.setSize(availableWidth - marginWidth, shopCardHeight)
        card.cellSize = shopCardHeight * 0.2f

        // X offset from the cells (5 cells = shopCardHeight)
        card.nameBounds.set(shopCardHeight, card.cellSize, availableWidth - shopCardHeight, shopCardHeight)
        card.priceBounds.set(shopCardHeight, -card.cellSize, availableWidth - shopCardHeight, shopCardHeight)
    }
}