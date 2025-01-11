package dev.lonami.klooni.actors

import com.badlogic.gdx.graphics.g2d.Batch
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.Theme
import dev.lonami.klooni.game.Cell
import dev.lonami.klooni.game.GameLayout

/**
 * Card-like actor used to display information about a given theme.
 * It allows the player to preview, select, and purchase different themes.
 */
class ThemeCard(game: Klooni, layout: GameLayout, val theme: Theme) : ShopCard(game, layout, theme.display, theme.background) {

    private val colorsUsed = arrayOf(intArrayOf(0, 7, 7), intArrayOf(8, 7, 3), intArrayOf(8, 8, 3))
    private val background = Theme.getBlankTexture()

    init {
        usedItemUpdated()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {

        batch.color = theme.background
        batch.draw(background, x, y, width, height)

        // Avoid drawing on the borders by adding +1 cell padding
        for (i in colorsUsed.indices) {
            for (j in colorsUsed[i].indices) {
                Cell.draw(
                    theme.cellTexture, theme.getCellColor(colorsUsed[i][j]), batch,
                    x + cellSize * (j + 1), y + cellSize * (i + 1), cellSize
                )
            }
        }
        super.draw(batch, parentAlpha)
    }

    override fun usedItemUpdated() {
        if (Klooni.theme.name == theme.name) priceLabel.setText("currently used")
        else if (Klooni.isThemeBought(theme)) priceLabel.setText("bought")
        else priceLabel.setText("buy for " + theme.price)
    }

    override fun use() {
        Klooni.updateTheme(theme)
        usedItemUpdated()
    }

    override fun isBought() = Klooni.isThemeBought(theme);


    override fun isUsed() = Klooni.theme.name == theme.name

    override fun getPrice() = theme.price.toFloat()

    override fun performBuy() {
        if (Klooni.buyTheme(theme))
            use()
    }

}