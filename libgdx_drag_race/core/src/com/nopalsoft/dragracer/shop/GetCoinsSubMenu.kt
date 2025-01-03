package com.nopalsoft.dragracer.shop

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.MainStreet
import com.nopalsoft.dragracer.Settings

class GetCoinsSubMenu(var game: MainStreet, container: Table) {
    // Number of coins that player will receive if they like our page on facebook
    var coinsLikeFacebook = 250
    var buttonLikeFacebook: TextButton
    private var buttonBuy50MillionCoins: TextButton

    init {
        container.clear()

        buttonLikeFacebook = TextButton("Like us", Assets.styleTextButtonBuy)
        if (Settings.didLikeFacebook) buttonLikeFacebook = TextButton(
            "Visit Us",
            Assets.styleTextButtonSelected
        )
        addPressEffect(buttonLikeFacebook)
        buttonLikeFacebook.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (!Settings.didLikeFacebook) {
                    Settings.didLikeFacebook = true
                    game.stage.addAction(
                        Actions.sequence(
                            Actions.delay(1f),
                            Actions.run {
                                Settings.coinsTotal += coinsLikeFacebook
                                buttonLikeFacebook.setText("Visit us")
                                buttonLikeFacebook.style = Assets.styleTextButtonSelected
                            })
                    )
                }
            }
        })

        buttonBuy50MillionCoins = TextButton("Buy", Assets.styleTextButtonBuy)
        addPressEffect(buttonBuy50MillionCoins)
        buttonBuy50MillionCoins.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {}
        })

        // Facebook Like
        container.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f)
        container.row()
        container
            .add(
                addCharacterTable(
                    coinsLikeFacebook,
                    Assets.buttonFacebook, "Like us on facebook and get "
                            + coinsLikeFacebook + " coins",
                    buttonLikeFacebook
                )
            ).expandX().fill()
        container.row()

        val drawableCoinFront = TextureRegionDrawable(Assets.coinFront)

        container
            .add(
                addCharacterTable(
                    50000,
                    drawableCoinFront,
                    "Coin super mega pack. Get this pack and you will be racing in cash",
                    buttonBuy50MillionCoins
                )
            ).expandX().fill()
        container.row()
    }

    private fun addCharacterTable(
        numberOfCoinsToGet: Int,
        imagen: TextureRegionDrawable, descripcion: String, boton: TextButton
    ): Table {
        val imageCoinFront = Image(Assets.coinFront)
        val imageCharacter = Image(imagen)

        val tableTitleBar = Table()
        tableTitleBar
            .add(Label("Get $numberOfCoinsToGet", Assets.labelStyleSmall))
            .left().padLeft(5f)
        tableTitleBar.add(imageCoinFront).left().expandX().padLeft(5f)

        val tableDescription = Table()
        tableDescription.add(imageCharacter).left().pad(10f).size(55f, 55f)
        val labelDescription = Label(descripcion, Assets.labelStyleSmall)
        labelDescription.wrap = true
        tableDescription.add(labelDescription).expand().fill().padLeft(5f)

        val tableContent = Table()
        tableContent.add(tableTitleBar).expandX().fill().colspan(2).padTop(8f)
        tableContent.row().colspan(2)
        tableContent.add(tableDescription).expandX().fill()
        tableContent.row().colspan(2)

        tableContent.add(boton).right().padRight(10f).size(120f, 45f)

        tableContent.row().colspan(2)
        tableContent.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f).padTop(15f)

        return tableContent
    }

    protected fun addPressEffect(actor: Actor) {
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
