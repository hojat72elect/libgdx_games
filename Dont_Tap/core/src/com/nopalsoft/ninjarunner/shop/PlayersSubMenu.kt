package com.nopalsoft.ninjarunner.shop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.nopalsoft.ninjarunner.AnimationSprite
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.NinjaRunnerGame
import com.nopalsoft.ninjarunner.Settings
import com.nopalsoft.ninjarunner.game_objects.Player
import com.nopalsoft.ninjarunner.scene2d.AnimatedSpriteActor

class PlayersSubMenu(container: Table, game: NinjaRunnerGame) {
    val PRICE_NINJA: Int = 1000

    var didBuyNinja: Boolean = false

    var labelNinjaPrice: Label? = null

    var buttonSelectShanti: TextButton? = null
    var buttonBuyNinja: TextButton? = null
    var arrayButtons: Array<TextButton>? = null

    var languages = game.languages

    var textBuy: String?
    var textSelect: String?

    init {
        container.clear()
        preferences.clear()
        preferences.flush()

        loadPurchases()

        textBuy = languages!!.get("buy")
        textSelect = languages!!.get("select")

        if (!didBuyNinja) labelNinjaPrice = Label(PRICE_NINJA.toString() + "", Assets.labelStyleSmall)

        initializeButtons()

        container.defaults().expand().fill().padLeft(10f).padRight(20f).padBottom(10f)

        container.add<Table?>(addPlayer("Runner girl", null, Assets.girlRunAnimation!!, languages!!.get("bombDescription"), buttonSelectShanti)).row()
        container.add<Table?>(addPlayer("Runner Ninja", labelNinjaPrice, Assets.ninjaRunAnimation!!, languages!!.get("bombDescription"), buttonBuyNinja)).row()
    }

    fun addPlayer(title: String?, labelPrice: Label?, image: AnimationSprite, description: String?, button: TextButton?): Table {
        val coin = Image(Assets.coinAnimation!!.getKeyFrame(0f))
        val imagePlayer = AnimatedSpriteActor(image)

        if (labelPrice == null) coin.isVisible = false

        val tableTitleBar = Table()
        tableTitleBar.add<Label?>(Label(title, Assets.labelStyleSmall)).expandX().left()
        tableTitleBar.add<Image?>(coin).right().size(20f)
        tableTitleBar.add<Label?>(labelPrice).right().padRight(10f)

        val tableContent = Table()
        tableContent.setBackground(Assets.backgroundItemShop)
        tableContent.pad(5f) // Ninepatch adds padding by default.

        tableContent.defaults().padLeft(20f).padRight(20f)
        tableContent.add<Table?>(tableTitleBar).expandX().fill().colspan(2)
        tableContent.row()

        tableContent.add<AnimatedSpriteActor?>(imagePlayer).size(120f, 99f)
        val labelDescription = Label(description, Assets.labelStyleSmall)
        labelDescription.setWrap(true)
        tableContent.add<Label?>(labelDescription).expand().fill()

        tableContent.row().colspan(2)
        tableContent.add<TextButton?>(button).expandX().right().size(120f, 45f)
        tableContent.row().colspan(2)

        tableContent.debugAll()
        return tableContent
    }

    private fun initializeButtons() {
        arrayButtons = Array<TextButton>()

        // DEFAULT
        buttonSelectShanti = TextButton(textSelect, Assets.styleTextButtonPurchased)
        if (Settings.selectedSkin == Player.TYPE_GIRL) buttonSelectShanti!!.isVisible = false

        buttonSelectShanti!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.selectedSkin = Player.TYPE_GIRL
                setSelected(buttonSelectShanti!!)
            }
        })

        // SKIN_NINJA
        buttonBuyNinja = if (didBuyNinja) TextButton(textSelect, Assets.styleTextButtonPurchased)
        else TextButton(textBuy, Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == Player.TYPE_NINJA) buttonBuyNinja!!.isVisible = false

        buttonBuyNinja!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (didBuyNinja) {
                    Settings.selectedSkin = Player.TYPE_NINJA
                    setSelected(buttonBuyNinja!!)
                } else if (Settings.totalCoins >= PRICE_NINJA) {
                    Settings.totalCoins -= PRICE_NINJA
                    setButtonStylePurchased(buttonBuyNinja!!)
                    labelNinjaPrice!!.remove()
                    didBuyNinja = true
                }
                savePurchases()
            }
        })

        arrayButtons!!.add(buttonSelectShanti)
        arrayButtons!!.add(buttonBuyNinja)
    }

    private fun loadPurchases() {
        didBuyNinja = preferences.getBoolean("didBuyNinja", false)
    }

    private fun savePurchases() {
        preferences.putBoolean("didBuyNinja", didBuyNinja)
        preferences.flush()
    }

    private fun setButtonStylePurchased(button: TextButton) {
        button.setStyle(Assets.styleTextButtonPurchased)
        button.setText(textSelect)
    }

    private fun setSelected(selectedButton: TextButton) {
        // I make all other buttons visible and at the end, the selected button invisible.
        for (button in arrayButtons!!) {
            button.isVisible = true
        }
        selectedButton.isVisible = false
    }

    companion object {
        private val preferences: Preferences = Gdx.app.getPreferences("com.tiar.shantirunner.shop")
    }
}
