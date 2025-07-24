package dev.lonami.klooni.actors

import com.badlogic.gdx.graphics.g2d.Batch
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.Klooni.Companion.buyTheme
import dev.lonami.klooni.Klooni.Companion.isThemeBought
import dev.lonami.klooni.Klooni.Companion.updateTheme
import dev.lonami.klooni.Theme
import dev.lonami.klooni.Theme.Companion.getBlankTexture
import dev.lonami.klooni.game.Cell
import dev.lonami.klooni.game.GameLayout

/**
 * Card-like actor used to display information about a given theme
 */
class ThemeCard(
    game: Klooni,
    layout: GameLayout,
    private val theme: Theme
) : ShopCard(game, layout, theme.displayName, theme.background!!) {
    private val background = getBlankTexture()

    init {
        usedItemUpdated()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val x = getX()
        val y = getY()

        batch?.color = theme.background
        batch?.draw(background, x, y, getWidth(), getHeight())

        // Avoid drawing on the borders by adding +1 cell padding
        for (i in colorsUsed.indices) {
            for (j in colorsUsed[i]!!.indices) {
                Cell.draw(
                    theme.cellTexture, theme.getCellColor(colorsUsed[i]!![j]), batch,
                    x + cellSize * (j + 1), y + cellSize * (i + 1), cellSize
                )
            }
        }

        super.draw(batch, parentAlpha)
    }

    override fun usedItemUpdated() {
        if (Klooni.theme!!.name == theme.name) priceLabel.setText("currently used")
        else if (isThemeBought(theme)) priceLabel.setText("bought")
        else priceLabel.setText("buy for " + theme.price)
    }

    override fun use() {
        updateTheme(theme)
        usedItemUpdated()
    }

    override val isBought = isThemeBought(theme)


    override val isUsed = Klooni.theme!!.name == theme.name


    override val price = theme.price.toFloat()

    override fun performBuy() {
        if (buyTheme(theme)) {
            use()
        }
    }

    companion object {
        private val colorsUsed = arrayOf<IntArray?>(
            intArrayOf(0, 7, 7),
            intArrayOf(8, 7, 3),
            intArrayOf(8, 8, 3)
        )
    }
}
