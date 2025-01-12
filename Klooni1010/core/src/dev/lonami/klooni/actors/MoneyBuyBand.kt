package dev.lonami.klooni.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.TimeUtils
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.Theme
import kotlin.math.min

/**
 * The UI for displaying and managing the in-game currency and purchases; particularly for themes.
 */
class MoneyBuyBand(game: Klooni) : Table() {

    private var infoText = ""
    private val infoLabel: Label

    private val confirmButton = SoftButton(0, "ok_texture")
    private val cancelButton = SoftButton(3, "cancel_texture")

    // Used to interpolate between strings
    private val shownText = StringBuilder()

    private var showingTemp = false

    /**
     * The theme card that is going to be bought next. We can't
     * only save the Theme because we need to tell the ThemeCard
     * that it was bought so it can reflect the new theme status.
     */
    private var toBuy: ShopCard? = null

    // When the next text update will take place
    private var nextTextUpdate = 0L

    // When the temporary text should be reverted next
    private var nextTempRevertUpdate = 0L

    init {
        val labelStyle = Label.LabelStyle()
        labelStyle.font = game.skin.getFont("font_small")

        infoLabel = Label(infoText, labelStyle)
        infoLabel.setAlignment(Align.left)
        add(infoLabel).expandX().left().padLeft(20f)

        confirmButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                if (toBuy != null) toBuy!!.performBuy()
                showCurrentMoney()
                hideBuyButtons()
            }
        })

        add(confirmButton).pad(8f, 0f, 8f, 4f)
        confirmButton.isVisible = false

        cancelButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                showCurrentMoney()
                hideBuyButtons()
            }
        })
        add(cancelButton).pad(8f, 4f, 8f, 8f)
        cancelButton.isVisible = false

        background = TextureRegionDrawable(TextureRegion(Theme.getBlankTexture()))
        showCurrentMoney()
    }

    private fun showCurrentMoney() {
        setText("money: " + Klooni.getMoney())
    }

    private fun hideBuyButtons() {
        confirmButton.isVisible = false
        cancelButton.isVisible = false
        toBuy = null
    }

    /**
     * Set the text to which the shown text will interpolate.
     * This will remove any temporary shown text or otherwise
     * it would mess up this new text.
     */
    private fun setText(text: String) {
        infoText = text
        showingTemp = false
        nextTextUpdate = TimeUtils.millis() + SHOW_ONE_CHARACTER_EVERY
    }

    /**
     * Temporary text will always reset to the shown money
     * because it would make no sense to go back to the buy "confirm?"
     *
     * Can also be used to show a temporary notification text.
     */
    fun setTempText(text: String) {
        setText(text)
        showingTemp = true
        nextTempRevertUpdate = TimeUtils.millis() + TEMP_TEXT_DELAY
    }

    /**
     * Funky method to interpolate between the information
     * text and the currently being shown text.
     */
    private fun interpolateText() {
        // If the currently shown text does not match the information text, then that means that we need to interpolate between them.
        if (shownText.toString() != infoText) {
            // We need the pick the minimum text length limit or charAt() will throw an IndexOutOfBoundsException

            val limit = min(shownText.length, infoText.length)
            for (i in 0 until limit) {
                // As soon as we found a character which differs, we can interpolate to the new string by updating that single character
                if (shownText[i] != infoText[i]) {
                    shownText.setCharAt(i, infoText[i])
                    infoLabel.setText(shownText)
                    return
                }
            }

            // All the preceding characters matched, so now what's left is to check for the string length
            if (shownText.length > infoText.length) {
                // The old text was longer than the new one, so shorten it
                shownText.setLength(shownText.length - 1)
            } else {
                // It can't be equal length or we wouldn't be here, so avoid checking shown.length() < info.length().
                // We need to append the next character that we want to show.
                shownText.append(infoText[shownText.length])
            }
            infoLabel.setText(shownText)
        }
    }

    /**
     * Asks the user to buy the given theme or effect, or shows that they don't have enough money to buy it.
     */
    fun askBuy(toBuy: ShopCard) {
        if (toBuy.getPrice() > Klooni.getMoney()) {
            setTempText("cannot buy!")
            confirmButton.isVisible = false
            cancelButton.isVisible = false
        } else {
            this.toBuy = toBuy
            setText("confirm?")
            confirmButton.isVisible = true
            cancelButton.isVisible = true
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val now = TimeUtils.millis()
        if (now > nextTextUpdate) {
            interpolateText()
            nextTextUpdate = TimeUtils.millis() + SHOW_ONE_CHARACTER_EVERY
            if (now > nextTempRevertUpdate && showingTemp) {
                // We won't be showing temp anymore if the current money is shown
                showCurrentMoney()
            }
        }
        color = Klooni.theme.bandColor
        infoLabel.color = Klooni.theme.textColor
        super.draw(batch, parentAlpha)
    }

    companion object {
        // these times are in Milliseconds
        private const val SHOW_ONE_CHARACTER_EVERY = 30L
        private const val TEMP_TEXT_DELAY = 2_000L
    }
}