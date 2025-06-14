package com.nopalsoft.dragracer.shop

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.MainStreet
import com.nopalsoft.dragracer.Settings

class NoAdsSubMenu(var game: MainStreet, containerTable: Table) {
    var priceNoAds: Int = 20000

    var buttonNoAds: TextButton
    var labelNoAds: Label? = null

    init {
        containerTable.clear()

        if (!Settings.didBuyNoAds) labelNoAds = Label(priceNoAds.toString() + "", Assets.labelStyleSmall)

        buttonNoAds = TextButton("Buy", Assets.styleTextButtonBuy)
        if (Settings.didBuyNoAds) buttonNoAds.isVisible = false
        addPressEffect(buttonNoAds)
        buttonNoAds.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (Settings.coinsTotal >= priceNoAds) {
                    Settings.coinsTotal -= priceNoAds
                    Settings.didBuyNoAds = true
                    labelNoAds!!.isVisible = false
                    buttonNoAds.isVisible = false
                }
            }
        })

        // Upgrade BoostTime
        containerTable.add(Image(Assets.horizontalSeparatorDrawable)).expandX().fill()
            .height(5f)
        containerTable.row()
        containerTable
            .add(
                addPlayerTable(
                    labelNoAds,
                    Assets.buttonNoAds!!,
                    buttonNoAds
                )
            )
            .expandX().fill()
        containerTable.row()
    }

    private fun addPlayerTable(
        labelPrice: Label?,
        imageDrawable: TextureRegionDrawable, button: TextButton
    ): Table {
        val coinImage = Image(Assets.coinFront)
        val playerImage = Image(imageDrawable)

        if (labelPrice == null) coinImage.isVisible = false

        val titleBarTable = Table()
        titleBarTable.add(Label("No more ads", Assets.labelStyleSmall)).expandX()
            .left().padLeft(5f)
        titleBarTable.add(coinImage).right()
        titleBarTable.add(labelPrice).right().padRight(10f)

        val descriptionTable = Table()
        descriptionTable.add(playerImage).left().pad(10f).size(55f, 45f)
        val descriptionLabel = Label("Buy it and no more ads will appear in the app", Assets.labelStyleSmall)
        descriptionLabel.wrap = true
        descriptionLabel.setFontScale(.85f)
        descriptionTable.add(descriptionLabel).expand().fill().padLeft(5f)

        val tbContent = Table()
        tbContent.add(titleBarTable).expandX().fill().colspan(2).padTop(8f)
        tbContent.row().colspan(2)
        tbContent.add(descriptionTable).expandX().fill()
        tbContent.row().colspan(2)

        tbContent.add(button).right().padRight(10f).size(120f, 45f)

        tbContent.row().colspan(2)
        tbContent.add(Image(Assets.horizontalSeparatorDrawable)).expandX().fill()
            .height(5f).padTop(15f)

        return tbContent
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
}
