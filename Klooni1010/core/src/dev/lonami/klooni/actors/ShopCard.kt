package dev.lonami.klooni.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.Theme.Companion.shouldUseWhite
import dev.lonami.klooni.game.GameLayout

abstract class ShopCard internal constructor(
    game: Klooni, layout: GameLayout,
    itemName: String?, backgroundColor: Color
) : Actor() {
    @JvmField
    val nameBounds: Rectangle

    @JvmField
    val priceBounds: Rectangle

    @JvmField
    val game: Klooni?

    @JvmField
    val priceLabel: Label
    private val nameLabel: Label

    @JvmField
    var cellSize: Float = 0f

    init {
        this.game = game
        val labelStyle = LabelStyle()
        labelStyle.font = game.skin!!.getFont("font_small")

        priceLabel = Label("", labelStyle)
        nameLabel = Label(itemName, labelStyle)

        val labelColor = if (shouldUseWhite(backgroundColor)) Color.WHITE else Color.BLACK
        priceLabel.setColor(labelColor)
        nameLabel.setColor(labelColor)

        priceBounds = Rectangle()
        nameBounds = Rectangle()

        layout.update(this)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

        val x = getX()
        val y = getY()
        nameLabel.setBounds(x + nameBounds.x, y + nameBounds.y, nameBounds.width, nameBounds.height)
        nameLabel.draw(batch, parentAlpha)

        priceLabel.setBounds(x + priceBounds.x, y + priceBounds.y, priceBounds.width, priceBounds.height)
        priceLabel.draw(batch, parentAlpha)
    }

    // Showcases the current effect (the shop will be showcasing them, one by one)
    // This method should be called on the same card as long as it returns true.
    // It should return false once it's done so that the next card can be showcased.
    open fun showcase(batch: Batch?, yDisplacement: Float): Boolean {
        return false
    }

    abstract fun usedItemUpdated()

    abstract fun use()

    abstract val isBought: Boolean

    abstract val isUsed: Boolean

    abstract val price: Float

    abstract fun performBuy()
}
