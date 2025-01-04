package dev.lonami.klooni.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.Theme
import dev.lonami.klooni.game.Board
import dev.lonami.klooni.game.GameLayout
import dev.lonami.klooni.game.Piece
import dev.lonami.klooni.interfaces.IEffectFactory

/**
 * Card-like actor used to display information about a given theme.
 */
class EffectCard(
    game: Klooni,
    layout: GameLayout,
    val effect: IEffectFactory
) : ShopCard(
    game,
    layout,
    effect.display,
    Klooni.theme.background
) {

    // Let the board have room for 3 cells, so cellSize * 3
    private val board = Board(Rectangle(0F, 0F, cellSize * 3, cellSize * 3), 3)
    private val background = Theme.getBlankTexture()

    // We want to create an effect from the beginning
    private var needCreateEffect = true

    init {
        setRandomPiece()
        usedItemUpdated()
    }

    private fun setRandomPiece() {
        while (true) {
            val piece = Piece.random()
            if (piece.cellCols > 3 || piece.cellRows > 3) continue

            // Try to center it (max size is 3, so center is the second grid bit unless max size)
            val x = if (piece.cellCols == 3) 0 else 1
            val y = if (piece.cellRows == 3) 0 else 1
            if (board.putPiece(piece, x, y)) break // Should not fail, but if it does, don't break
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {

        batch.color = Klooni.theme.background
        batch.draw(background, x, y, width, height)


        // Avoid drawing on the borders by adding +1 cell padding
        board.pos[x + cellSize * 1] = y + cellSize * 1


        // Draw only if effects are done, i.e. not showcasing
        if (board.effectsDone()) board.draw(batch)

        super.draw(batch, parentAlpha)
    }

    override fun showcase(batch: Batch, yDisplacement: Float): Boolean {
        board.pos.y += yDisplacement


        // If no effect is running
        if (board.effectsDone()) {
            // And we want to create a new one
            if (needCreateEffect) {
                // Clear at cells[1][1], the center one
                board.clearAll(1, 1, effect)
                needCreateEffect = false
            } else {
                // Otherwise, the previous effect finished, so return false because we're done
                // We also want to draw the next time so set the flag to true
                setRandomPiece()
                needCreateEffect = true
                return false
            }
        }

        board.draw(batch)
        return true
    }

    override fun usedItemUpdated() {
        if (game.effect.name == effect.name) {
            priceLabel.setText("currently used")
        } else if (Klooni.isEffectBought(effect)) {
            priceLabel.setText("bought")
        } else {
            priceLabel.setText("buy for " + effect.price)
        }
    }

    override fun use() {
        game.updateEffect(effect)
        usedItemUpdated()
    }

    override fun isBought() = Klooni.isEffectBought(effect)

    override fun isUsed() = (game.effect.name == effect.name)

    override fun getPrice() = effect.price.toFloat()

    override fun performBuy() {
        if (Klooni.buyEffect(effect)) {
            use()
        }
    }
}