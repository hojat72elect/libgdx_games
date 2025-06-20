package com.nopalsoft.sokoban.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.Settings
import com.nopalsoft.sokoban.Settings.save
import com.nopalsoft.sokoban.game.GameScreen
import com.nopalsoft.sokoban.screens.MainMenuScreen
import com.nopalsoft.sokoban.screens.Screens

class WindowGroupPause(currentScreen: Screens) : WindowGroup(currentScreen, 350f, 300f, 100f) {
    private var buttonHome: Button = Button(Assets.homeButtonDrawable, Assets.homeButtonPressedDrawable)
    private var buttonRefresh: Button = Button(Assets.buttonRefreshDrawable, Assets.buttonRefreshPressedDrawable)
    private var tableAnimations: Table = Table()

    init {
        setCloseButton()
        setTitle("Paused", 1f)

        val tableMenu = Table()
        tableMenu.setFillParent(true)

        buttonHome.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(MainMenuScreen::class.java, screen.game)
            }
        })

        buttonRefresh.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(GameScreen::class.java, (screen as GameScreen).level, screen.game)
            }
        })

        val buttonAnimation = Button(Assets.buttonOffDrawable, Assets.buttonOnDrawable, Assets.buttonOnDrawable)
        buttonAnimation.isChecked = Settings.animationWalkIsON

        tableAnimations.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.animationWalkIsON = !Settings.animationWalkIsON
                buttonAnimation.isChecked = Settings.animationWalkIsON
                save()
            }
        })

        tableMenu.defaults().expandX()
        tableMenu.pad(30f).padTop(55f)
        tableMenu.add(buttonHome)
        tableMenu.add(buttonRefresh)
        tableMenu.row()

        val labelAnimations = Label("Animations", LabelStyle(Assets.fontRed, Color.WHITE))
        tableAnimations.add(labelAnimations)
        tableAnimations.add(buttonAnimation).padLeft(15f)

        tableMenu.add(tableAnimations).colspan(2).padTop(10f)

        addActor(tableMenu)
    }
}
