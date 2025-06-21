package com.nopalsoft.slamthebird.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.screens.BaseScreen

class DialogRate(currentScreen: BaseScreen) : Dialog(currentScreen) {
    init {
        setSize(390f, 260f)
        y = 300f
        setBackGround()

        val labelTitle = Label("Support this game", Assets.smallLabelStyle)
        labelTitle.setPosition(width / 2f - labelTitle.width / 2f, 210f)

        val labelDescription = Label(
            "Hello, thank you for playing Slam the Bird.\nHelp us to support this game. Just rate us at the app store.",
            Assets.smallLabelStyle
        )
        labelDescription.setSize(width - 20, 170f)
        labelDescription.setPosition(
            width / 2f - labelDescription.width / 2f,
            50f
        )
        labelDescription.wrap = true

        val buttonRate = TextButton("Rate", Assets.styleTextButtonPurchased)
        screen.addPressEffect(buttonRate)
        buttonRate.label.wrap = true
        buttonRate.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
            }
        })

        val buttonNotNow = TextButton("Not now", Assets.styleTextButtonSelected)
        screen.addPressEffect(buttonNotNow)
        buttonNotNow.label.wrap = true
        buttonNotNow.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
            }
        })

        val tableButtons = Table()
        tableButtons.setSize(width - 20, 60f)
        tableButtons.setPosition(width / 2f - tableButtons.width / 2f, 10f)

        tableButtons.defaults().uniform().expand().center().fill().pad(10f)
        tableButtons.add(buttonRate)
        tableButtons.add(buttonNotNow)

        addActor(labelDescription)
        addActor(tableButtons)
        addActor(labelTitle)
    }
}
