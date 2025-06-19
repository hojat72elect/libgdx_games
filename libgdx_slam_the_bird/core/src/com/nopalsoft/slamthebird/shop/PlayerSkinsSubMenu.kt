package com.nopalsoft.slamthebird.shop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.Settings
import com.nopalsoft.slamthebird.SlamTheBirdGame

class PlayerSkinsSubMenu(var game: SlamTheBirdGame, tableContainer: Table) {


    var isRedAndroidPurchased: Boolean
    var isBlueAndroidPurchased: Boolean

    var buttonDefault: TextButton? = null
    var buttonRedAndroid: TextButton? = null
    var buttonBlueAndroid: TextButton? = null
    private var arrayButtons: Array<TextButton>? = null

    var labelPriceRedAndroid: Label
    var labelPriceBlueAndroid: Label

    init {
        tableContainer.clear()

        isRedAndroidPurchased = preferences.getBoolean("didBuyRojo", false)
        isBlueAndroidPurchased = preferences.getBoolean("didBuyAzul", false)

        labelPriceRedAndroid = Label(
            PRICE_RED_ANDROID.toString() + "",
            Assets.smallLabelStyle
        )
        labelPriceBlueAndroid = Label(
            PRICE_BLUE_ANDROID.toString() + "",
            Assets.smallLabelStyle
        )

        initializeButtons()

        tableContainer.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f)
        tableContainer.row()

        // default skin
        tableContainer
            .add(
                addPlayerTable(
                    "Green robot", null,
                    Assets.defaultPlayerSkin,
                    "Just a simple robot for slaming birds", buttonDefault
                )
            )
            .expandX().fill()
        tableContainer.row()

        // red skin
        tableContainer
            .add(
                addPlayerTable(
                    "Red robot",
                    labelPriceRedAndroid,
                    Assets.redPlayerSkin,
                    "Do you like red color. Play with this amazing red robot and slam those birds",
                    buttonRedAndroid
                )
            ).expandX().fill()
        tableContainer.row()

        // blue skin
        tableContainer
            .add(
                addPlayerTable(
                    "Blue robot",
                    labelPriceBlueAndroid,
                    Assets.bluePlayerSkin,
                    "Do you like blue color. Play with this amazing blue robot and slam those birds",
                    buttonBlueAndroid
                )
            ).expandX().fill()
        tableContainer.row()

        if (isBlueAndroidPurchased) labelPriceBlueAndroid.remove()
        if (isRedAndroidPurchased) labelPriceRedAndroid.remove()
    }

    private fun addPlayerTable(
        title: String, labelPrice: Label?,
        imageAtlasRegion: AtlasRegion, description: String, button: TextButton?
    ): Table {
        val coinImage = Image(Assets.coinsRegion)
        val playerImage = Image(imageAtlasRegion)

        if (labelPrice == null) coinImage.isVisible = false

        val tableTitleBar = Table()
        tableTitleBar.add(Label(title, Assets.smallLabelStyle)).expandX()
            .left()
        tableTitleBar.add(coinImage).right()
        tableTitleBar.add(labelPrice).right().padRight(10f)

        val tableContent = Table()
        tableContent.add(tableTitleBar).expandX().fill().colspan(2).padTop(8f)
        tableContent.row()
        tableContent.add(playerImage).left().pad(10f).size(60f)

        val labelDescription = Label(description, Assets.smallLabelStyle)
        labelDescription.wrap = true
        tableContent.add(labelDescription).expand().fill().padLeft(5f)

        tableContent.row().colspan(2)
        tableContent.add(button).expandX().right().padRight(10f).size(120f, 45f)
        tableContent.row().colspan(2)
        tableContent.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f).padTop(15f)

        return tableContent
    }

    private fun initializeButtons() {
        arrayButtons = Array()

        // default skin
        buttonDefault = TextButton("Select", Assets.styleTextButtonPurchased)
        if (Settings.selectedSkin == SKIN_DEFAULT) buttonDefault!!.isVisible = false

        addPressEffect(buttonDefault!!)
        buttonDefault!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.selectedSkin = SKIN_DEFAULT
                setSelected(buttonDefault!!)
                Assets.drawPlayer()
            }
        })

        // red Android skin
        buttonRedAndroid = if (isRedAndroidPurchased) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_RED_ANDROID) buttonRedAndroid!!.isVisible = false

        addPressEffect(buttonRedAndroid!!)
        buttonRedAndroid!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (isRedAndroidPurchased) {
                    Settings.selectedSkin = SKIN_RED_ANDROID
                    setSelected(buttonRedAndroid!!)
                    Assets.drawPlayer()
                } else if (Settings.currentCoins >= PRICE_RED_ANDROID) {
                    Settings.currentCoins -= PRICE_RED_ANDROID
                    setButtonStylePurchased(buttonRedAndroid!!)
                    isRedAndroidPurchased = true
                    labelPriceRedAndroid.remove()
                    save()
                }
            }
        })

        // blue Android skin
        buttonBlueAndroid = if (isBlueAndroidPurchased) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_BLUE_ANDROID) buttonBlueAndroid!!.isVisible = false

        addPressEffect(buttonBlueAndroid!!)
        buttonBlueAndroid!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (isBlueAndroidPurchased) {
                    Settings.selectedSkin = SKIN_BLUE_ANDROID
                    setSelected(buttonBlueAndroid!!)
                    Assets.drawPlayer()
                } else if (Settings.currentCoins >= PRICE_BLUE_ANDROID) {
                    Settings.currentCoins -= PRICE_BLUE_ANDROID
                    setButtonStylePurchased(buttonBlueAndroid!!)
                    isBlueAndroidPurchased = true
                    labelPriceBlueAndroid.remove()
                    save()
                }
            }
        })

        arrayButtons!!.add(buttonDefault)
        arrayButtons!!.add(buttonRedAndroid)
        arrayButtons!!.add(buttonBlueAndroid)
    }

    private fun save() {
        preferences.putBoolean("didBuyAzul", isBlueAndroidPurchased)
        preferences.putBoolean("didBuyRojo", isRedAndroidPurchased)
        preferences.flush()
    }

    private fun setButtonStylePurchased(button: TextButton) {
        button.style = Assets.styleTextButtonPurchased
        button.setText("Select")
    }

    private fun setSelected(selectedButton: TextButton) {
        // I make all visible and at the end the selected button invisible
        for (button in arrayButtons!!) {
            button.isVisible = true
        }
        selectedButton.isVisible = false
    }

    private fun addPressEffect(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                actor.setPosition(actor.x, actor.y - 3)
                event.stop()
                return true
            }

            override fun touchUp(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                actor.setPosition(actor.x, actor.y + 3)
            }
        })
    }

    companion object {
        const val PRICE_RED_ANDROID = 1500
        const val PRICE_BLUE_ANDROID = 2000
        const val SKIN_DEFAULT = 0
        const val SKIN_RED_ANDROID = 1
        const val SKIN_BLUE_ANDROID = 2

        private val preferences: Preferences = Gdx.app
            .getPreferences("com.nopalsoft.slamthebird.personajes")
    }
}
