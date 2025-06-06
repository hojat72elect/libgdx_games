package com.nopalsoft.ninjarunner.shop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Array
import com.nopalsoft.ninjarunner.AnimationSprite
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.NinjaRunnerGame
import com.nopalsoft.ninjarunner.Settings
import com.nopalsoft.ninjarunner.game_objects.Mascot
import com.nopalsoft.ninjarunner.scene2d.AnimatedSpriteActor

class MascotsSubMenu(container: Table, game: NinjaRunnerGame) {
    val BOMB_PRICE: Int = 5000

    var didBuyBomb: Boolean = false

    var labelBirdPrice: Label? = null
    var labelBombPrice: Label? = null

    var buttonBird: TextButton? = null
    var buttonBomb: TextButton? = null
    var arrayButtons: Array<TextButton>? = null

    val MAX_LEVEL: Int = 6
    val PRICE_LEVEL_1: Int = 350
    val PRICE_LEVEL_2: Int = 1000
    val PRICE_LEVEL_3: Int = 3000
    val PRICE_LEVEL_4: Int = 4500
    val PRICE_LEVEL_5: Int = 5000
    val PRICE_LEVEL_6: Int = 7500

    var buttonUpgradeBird: Button? = null
    var buttonUpgradeBomb: Button? = null

    var arrayBird: kotlin.Array<Image?>
    var arrayBomb: kotlin.Array<Image?>

    var languages = game.languages

    var textBuy: String?
    var textSelect: String?

    init {
        container.clear()

        loadPurchases()

        textBuy = languages!!.get("buy")
        textSelect = languages!!.get("select")

        arrayBird = arrayOfNulls<Image>(MAX_LEVEL)
        arrayBomb = arrayOfNulls<Image>(MAX_LEVEL)

        if (Settings.MASCOT_LEVEL_BIRD < MAX_LEVEL) {
            labelBirdPrice = Label(calculatePrice(Settings.MASCOT_LEVEL_BIRD).toString() + "", Assets.labelStyleSmall)
        }

        if (!didBuyBomb) {
            labelBombPrice = Label(BOMB_PRICE.toString() + "", Assets.labelStyleSmall)
        } else if (Settings.MASCOT_LEVEL_BOMB < MAX_LEVEL) {
            labelBombPrice = Label(calculatePrice(Settings.MASCOT_LEVEL_BOMB).toString() + "", Assets.labelStyleSmall)
        }

        initializeButtons()

        container.defaults().expand().fill().padLeft(10f).padRight(20f).padBottom(10f)

        container.add<Table?>(
            addMascot(
                "Chicken", labelBirdPrice, Assets.mascotBirdFlyAnimation!!, 60f, 54f, languages!!.get("pinkChikenDescription"), buttonBird, arrayBird,
                buttonUpgradeBird
            )
        ).row()
        container.add<Table?>(
            addMascot("Bomb", labelBombPrice, Assets.mascotBombFlyAnimation!!, 53f, 64f, languages!!.get("bombDescription"), buttonBomb, arrayBomb, buttonUpgradeBomb)
        )
            .row()

        setArrays()
    }

    fun addMascot(
        title: String?, labelPrice: Label?, image: AnimationSprite, imageWidth: Float, imageHeight: Float, description: String?,
        buttonBuy: TextButton?, arrayLevel: kotlin.Array<Image?>, buttonUpgrade: Button?
    ): Table {
        val coinImage = Image(Assets.coinAnimation!!.getKeyFrame(0f))
        val imagePlayer = AnimatedSpriteActor(image)

        if (labelPrice == null) coinImage.isVisible = false

        val tableTitleBar = Table()
        tableTitleBar.add<Label?>(Label(title, Assets.labelStyleSmall)).expandX().left()
        tableTitleBar.add<Image?>(coinImage).right().size(20f)
        tableTitleBar.add<Label?>(labelPrice).right().padRight(10f)

        val tbContent = Table()
        tbContent.setBackground(Assets.backgroundItemShop)
        tbContent.pad(5f)

        tbContent.add<Table?>(tableTitleBar).expandX().fill().colspan(2)
        tbContent.row()

        tbContent.add<AnimatedSpriteActor?>(imagePlayer).size(imageWidth, imageHeight)
        val labelDescription = Label(description, Assets.labelStyleSmall)
        labelDescription.setWrap(true)
        tbContent.add<Label?>(labelDescription).expand().fill()

        val auxTab = Table()
        auxTab.setBackground(Assets.backgroundUpgradeBar)
        auxTab.pad(5f)
        auxTab.defaults().padLeft(5f)
        for (i in 0..<MAX_LEVEL) {
            arrayLevel[i] = Image()
            auxTab.add<Image?>(arrayLevel[i]).size(15f)
        }

        tbContent.row()
        tbContent.add<Table?>(auxTab)
        tbContent.add<Button?>(buttonUpgrade).left().size(40f)

        tbContent.row().colspan(2)
        tbContent.add<TextButton?>(buttonBuy).expandX().right().size(120f, 45f)
        tbContent.row().colspan(2)

        return tbContent
    }

    private fun initializeButtons() {
        arrayButtons = Array<TextButton>()

        run {
            // DEFAULT
            run {
                // BUY
                buttonBird = TextButton(textSelect, Assets.styleTextButtonPurchased)
                if (Settings.selectedMascot == Mascot.MascotType.PINK_BIRD) buttonBird!!.isVisible = false
                buttonBird!!.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        Settings.selectedMascot = Mascot.MascotType.PINK_BIRD
                        setSelected(buttonBird!!)
                    }
                })
            }
            run {
                // UPGRADE
                buttonUpgradeBird = Button(Assets.styleButtonUpgrade)
                if (Settings.MASCOT_LEVEL_BIRD == MAX_LEVEL) buttonUpgradeBird!!.isVisible = false
                buttonUpgradeBird!!.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        if (Settings.totalCoins >= calculatePrice(Settings.MASCOT_LEVEL_BIRD)) {
                            Settings.totalCoins -= calculatePrice(Settings.MASCOT_LEVEL_BIRD)
                            Settings.MASCOT_LEVEL_BIRD++
                            updateLabelPriceAndButton(Settings.MASCOT_LEVEL_BIRD, labelBirdPrice!!, buttonUpgradeBird!!)
                            setArrays()
                        }
                    }
                })
            }
        }

        run {
            // MASCOT
            run {
                // BUY
                buttonBomb = if (didBuyBomb) TextButton(textSelect, Assets.styleTextButtonPurchased)
                else TextButton(textBuy, Assets.styleTextButtonBuy)

                if (Settings.selectedMascot == Mascot.MascotType.BOMB) buttonBomb!!.isVisible = false
                buttonBomb!!.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        if (didBuyBomb) {
                            Settings.selectedMascot = Mascot.MascotType.BOMB
                            setSelected(buttonBomb!!)
                        } else if (Settings.totalCoins >= BOMB_PRICE) {
                            Settings.totalCoins -= BOMB_PRICE
                            setButtonStylePurchased(buttonBomb!!)
                            didBuyBomb = true
                            buttonUpgradeBomb!!.isVisible = true
                            updateLabelPriceAndButton(Settings.MASCOT_LEVEL_BOMB, labelBombPrice!!, buttonUpgradeBomb!!)
                        }
                        savePurchases()
                    }
                })
            }
            run {
                // UPGRADE
                buttonUpgradeBomb = Button(Assets.styleButtonUpgrade)
                if (Settings.MASCOT_LEVEL_BOMB == MAX_LEVEL || !didBuyBomb) buttonUpgradeBomb!!.isVisible = false
                buttonUpgradeBomb!!.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        if (Settings.totalCoins >= calculatePrice(Settings.MASCOT_LEVEL_BOMB)) {
                            Settings.totalCoins -= calculatePrice(Settings.MASCOT_LEVEL_BOMB)
                            Settings.MASCOT_LEVEL_BOMB++
                            updateLabelPriceAndButton(Settings.MASCOT_LEVEL_BOMB, labelBombPrice!!, buttonUpgradeBomb!!)
                            setArrays()
                        }
                    }
                })
            }
        }

        arrayButtons!!.add(buttonBird)
        arrayButtons!!.add(buttonBomb)
    }

    private fun loadPurchases() {
        didBuyBomb = preferences.getBoolean("didBuyBomb", false)
    }

    private fun savePurchases() {
        preferences.putBoolean("didBuyBomb", didBuyBomb)
        preferences.flush()
    }

    private fun setButtonStylePurchased(button: TextButton) {
        button.setStyle(Assets.styleTextButtonPurchased)
        button.setText(textSelect)
    }

    private fun setSelected(button: TextButton) {
        // I make all visible and at the end the selected button invisible
        for (arrayButton in arrayButtons!!) {
            arrayButton.isVisible = true
        }
        button.isVisible = false
    }

    private fun calculatePrice(level: Int): Int {
        return when (level) {
            0 -> PRICE_LEVEL_1
            1 -> PRICE_LEVEL_2
            2 -> PRICE_LEVEL_3
            3 -> PRICE_LEVEL_4
            4 -> PRICE_LEVEL_5
            else -> PRICE_LEVEL_6
        }
    }

    private fun updateLabelPriceAndButton(level: Int, label: Label, button: Button) {
        if (level < MAX_LEVEL) {
            label.setText(calculatePrice(level).toString() + "")
        } else {
            label.isVisible = false
            button.isVisible = false
        }
    }

    private fun setArrays() {
        for (i in 0..<Settings.MASCOT_LEVEL_BIRD) {
            arrayBird[i]!!.setDrawable(TextureRegionDrawable(Assets.buttonShare))
        }

        for (i in 0..<Settings.MASCOT_LEVEL_BOMB) {
            arrayBomb[i]!!.setDrawable(TextureRegionDrawable(Assets.buttonShare))
        }
    }

    companion object {
        private val preferences: Preferences = Gdx.app.getPreferences("com.tiar.shantirunner.shop")
    }
}
