package dev.lonami.klooni.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.Theme
import dev.lonami.klooni.game.GameLayout

/**
 * The base  for all item cards in the shop inside our game.
 */
abstract class ShopCard(val game: Klooni, layout: GameLayout, itemName: String, backgroundColor: Color) : Actor() {

    @JvmField
    var cellSize = 0F

    @JvmField
    protected val priceLabel: Label
    private val nameLabel: Label

    @JvmField
    val nameBounds = Rectangle()

    @JvmField
    val priceBounds = Rectangle()

    init {
        val labelStyle = Label.LabelStyle()
        labelStyle.font = game.skin.getFont("font_small")

        priceLabel = Label("", labelStyle)
        nameLabel = Label(itemName, labelStyle)

        val labelColor = if (Theme.shouldUseWhite(backgroundColor)) Color.WHITE else Color.BLACK
        priceLabel.color = labelColor
        nameLabel.color = labelColor

        layout.update(this)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {

        super.draw(batch, parentAlpha)

        nameLabel.setBounds(x + nameBounds.x, y + nameBounds.y, nameBounds.width, nameBounds.height)
        nameLabel.draw(batch, parentAlpha)

        priceLabel.setBounds(x + priceBounds.x, y + priceBounds.y, priceBounds.width, priceBounds.height)
        priceLabel.draw(batch, parentAlpha)

    }

    /**
     * Showcases the current effect (the shop will be showcasing them, one by one)
     * This method should be called on the same card as long as it returns true.
     * It should return false once it's done so that the next card can be showcased.
     */
    open fun showcase(batch: Batch, yDisplacement: Float) = false

    abstract fun usedItemUpdated()

    abstract fun use()

    abstract fun isBought(): Boolean

    abstract fun isUsed(): Boolean

    abstract fun getPrice(): Float

    abstract fun performBuy()
}