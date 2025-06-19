package com.nopalsoft.slamthebird.shop

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.Settings
import com.nopalsoft.slamthebird.SlamTheBirdGame

class NoAdsSubMenu(var game: SlamTheBirdGame, tableContainer: Table) {
    var priceNoAds: Int = 35000

    var buttonNoAds: TextButton
    var labelNoAds: Label? = null

    init {
        tableContainer.clear()

        if (!Settings.didBuyNoAds) labelNoAds = Label(priceNoAds.toString() + "", Assets.smallLabelStyle)

        buttonNoAds = TextButton("Buy", Assets.styleTextButtonBuy)
        if (Settings.didBuyNoAds) buttonNoAds.isVisible = false
        addPressEffect(buttonNoAds)
        buttonNoAds.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (Settings.currentCoins >= priceNoAds) {
                    Settings.currentCoins -= priceNoAds
                    Settings.didBuyNoAds = true
                    labelNoAds!!.isVisible = false
                    buttonNoAds.isVisible = false
                }
            }
        })

        tableContainer.add(Image(Assets.horizontalSeparator)).expandX().fill().height(5f)
        tableContainer.row()
        tableContainer
            .add(
                createPlayerTable(
                    labelNoAds, Assets.buttonNoAds,
                    buttonNoAds
                )
            ).expandX().fill()
        tableContainer.row()
    }

    private fun createPlayerTable(
        labelPrice: Label?, imageDrawable: TextureRegionDrawable,
        button: TextButton
    ): Table {
        val coinImage = Image(Assets.coinsRegion)
        val playerImage = Image(imageDrawable)

        if (labelPrice == null) coinImage.isVisible = false

        val tableTitleBar = Table()
        tableTitleBar.add(Label("No more ads", Assets.smallLabelStyle)).expandX().left().padLeft(5f)
        tableTitleBar.add(coinImage).right()
        tableTitleBar.add(labelPrice).right().padRight(10f)

        val tableDescription = Table()
        tableDescription.add(playerImage).left().pad(10f).size(55f, 45f)
        val labelDescription = Label("Buy it and no more ads will apper in the app", Assets.smallLabelStyle)
        labelDescription.wrap = true
        tableDescription.add(labelDescription).expand().fill().padLeft(5f)

        val tableContent = Table()
        tableContent.add(tableTitleBar).expandX().fill().colspan(2).padTop(8f)
        tableContent.row().colspan(2)
        tableContent.add(tableDescription).expandX().fill()
        tableContent.row().colspan(2)

        tableContent.add(button).right().padRight(10f).size(120f, 45f)

        tableContent.row().colspan(2)
        tableContent.add(Image(Assets.horizontalSeparator)).expandX().fill().height(5f).padTop(15f)

        return tableContent
    }

    private fun addPressEffect(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                actor.setPosition(actor.x, actor.y - 3)
                event.stop()
                return true
            }

            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                actor.setPosition(actor.x, actor.y + 3)
            }
        })
    }
}
