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

class GetCoinsSubMenu(var game: MainStreet, var tableContainer: Table) {
    var coinsForFacebookLike: Int = 250


    var buttonLikeFacebook: TextButton
    var buttonBuy50MilCoins: TextButton

    init {
        tableContainer.clear()

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
                    game.stage!!.addAction(
                        Actions.sequence(
                            Actions.delay(1f),
                            Actions.run {
                                Settings.coinsTotal += coinsForFacebookLike
                                buttonLikeFacebook.setText("Visit us")
                                buttonLikeFacebook.style = Assets.styleTextButtonSelected
                            })
                    )
                }
            }
        })

        buttonBuy50MilCoins = TextButton("Buy", Assets.styleTextButtonBuy)
        addPressEffect(buttonBuy50MilCoins)


        // Facebook Like
        tableContainer.add(Image(Assets.horizontalSeparatorDrawable)).expandX().fill()
            .height(5f)
        tableContainer.row()
        tableContainer
            .add(
                addPlayerTable(
                    coinsForFacebookLike,
                    Assets.buttonFacebook!!, ("Like us on facebook and get "
                            + coinsForFacebookLike + " coins"),
                    buttonLikeFacebook
                )
            ).expandX().fill()
        tableContainer.row()

        val coinDrawable = TextureRegionDrawable(
            Assets.coinFront
        )

        tableContainer
            .add(
                addPlayerTable(
                    50000,
                    coinDrawable,
                    "Coin super mega pack. Get this pack and you will be racing in cash",
                    buttonBuy50MilCoins
                )
            ).expandX().fill()
        tableContainer.row()
    }

    private fun addPlayerTable(
        numCoinsToGet: Int,
        imageDrawable: TextureRegionDrawable, description: String, button: TextButton
    ): Table {
        val coinImage = Image(Assets.coinFront)
        val playerImage = Image(imageDrawable)

        val titleBarTable = Table()
        titleBarTable
            .add(Label("Get $numCoinsToGet", Assets.labelStyleSmall))
            .left().padLeft(5f)
        titleBarTable.add(coinImage).left().expandX().padLeft(5f)

        val descriptionTable = Table()
        descriptionTable.add(playerImage).left().pad(10f).size(55f, 55f)
        val descriptionLabel = Label(description, Assets.labelStyleSmall)
        descriptionLabel.wrap = true
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
