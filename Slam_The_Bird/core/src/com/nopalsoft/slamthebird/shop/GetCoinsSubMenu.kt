package com.nopalsoft.slamthebird.shop

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
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.Settings
import com.nopalsoft.slamthebird.SlamTheBirdGame

class GetCoinsSubMenu(var game: SlamTheBirdGame, tableContainer: Table) {
    var facebookLikeCoinBonus: Int = 1500

    var buttonLikeFacebook: TextButton
    private var buttonBuy5MCoins: TextButton
    private var buttonBuy15MCoins: TextButton
    private var buttonBuy30MCoins: TextButton
    private var buttonBuy50MCoins: TextButton

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
                                Settings.currentCoins += facebookLikeCoinBonus
                                buttonLikeFacebook.setText("Visit us")
                                buttonLikeFacebook.style = Assets.styleTextButtonSelected
                            })
                    )
                }
            }
        })

        buttonBuy5MCoins = TextButton("Buy", Assets.styleTextButtonBuy)
        addPressEffect(buttonBuy5MCoins)

        buttonBuy15MCoins = TextButton("Buy", Assets.styleTextButtonBuy)
        addPressEffect(buttonBuy15MCoins)

        buttonBuy30MCoins = TextButton("Buy", Assets.styleTextButtonBuy)
        addPressEffect(buttonBuy30MCoins)

        buttonBuy50MCoins = TextButton("Buy", Assets.styleTextButtonBuy)
        addPressEffect(buttonBuy50MCoins)

        tableContainer.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f)
        tableContainer.row()
        tableContainer
            .add(
                createPlayerTable(facebookLikeCoinBonus, Assets.buttonFacebook!!, ("Like us on facebook and get $facebookLikeCoinBonus coins"), buttonLikeFacebook)
            ).expandX().fill()
        tableContainer.row()

        val coinDrawable = TextureRegionDrawable(Assets.coinsRegion)

        tableContainer
            .add(
                createPlayerTable(
                    5000,
                    coinDrawable,
                    "Coin simple pack. A quick way to buy simple upgrades",
                    buttonBuy5MCoins
                )
            ).expandX().fill()
        tableContainer.row()


        tableContainer
            .add(
                createPlayerTable(
                    15000,
                    coinDrawable,
                    "Coin super pack. Get some cash for upgrades and characters",
                    buttonBuy15MCoins
                )
            ).expandX().fill()
        tableContainer.row()

        tableContainer
            .add(
                createPlayerTable(
                    30000,
                    coinDrawable,
                    "Coin mega pack. You can buy a lot of characters and upgrades",
                    buttonBuy30MCoins
                )
            ).expandX().fill()
        tableContainer.row()

        tableContainer
            .add(
                createPlayerTable(
                    50000,
                    coinDrawable,
                    "Coin super mega pack. Get this pack and you will be slamming in cash",
                    buttonBuy50MCoins
                )
            ).expandX().fill()
        tableContainer.row()
    }

    private fun createPlayerTable(
        numCoinsToGet: Int,
        imageDrawable: TextureRegionDrawable, description: String, button: TextButton
    ): Table {
        val coinImage = Image(Assets.coinsRegion)
        val playerImage = Image(imageDrawable)

        val titleBarTable = Table()
        titleBarTable
            .add(Label("Get $numCoinsToGet", Assets.smallLabelStyle))
            .left().padLeft(5f)
        titleBarTable.add(coinImage).left().expandX().padLeft(5f)

        val descriptionTable = Table()
        descriptionTable.add(playerImage).left().pad(10f).size(55f, 45f)
        val descriptionLabel = Label(description, Assets.smallLabelStyle)
        descriptionLabel.wrap = true
        descriptionTable.add(descriptionLabel).expand().fill().padLeft(5f)

        val contentTable = Table()
        contentTable.add(titleBarTable).expandX().fill().colspan(2).padTop(8f)
        contentTable.row().colspan(2)
        contentTable.add(descriptionTable).expandX().fill()
        contentTable.row().colspan(2)

        contentTable.add(button).right().padRight(10f).size(120f, 45f)

        contentTable.row().colspan(2)
        contentTable.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f).padTop(15f)

        return contentTable
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
