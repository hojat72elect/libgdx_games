package com.nopalsoft.ninjarunner.leaderboard

import com.badlogic.gdx.Game
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.game.GameScreen
import com.nopalsoft.ninjarunner.screens.Screens

class LeaderboardScreen(_game: Game?) : Screens(_game) {
    var tableMenu: Table
    var buttonLeaderboard: Button? = null
    var buttonFacebook: Button? = null
    var buttonInviteFriend: Button? = null
    var buttonGoogle: Button? = null

    var scroll: ScrollPane
    var tableContainer: Table


    init {
        val labelShop = Label("Leaderboards", Assets.labelStyleLarge)

        val tableTitle = Table()
        tableTitle.setSize(400f, 100f)
        tableTitle.setPosition(SCREEN_WIDTH / 2f - tableTitle.getWidth() / 2f, SCREEN_HEIGHT - tableTitle.getHeight())
        tableTitle.setBackground(Assets.backgroundTitleShop)
        tableTitle.padTop(20f).padBottom(5f)

        tableTitle.row().colspan(2)
        tableTitle.add<Label?>(labelShop).expand()
        tableTitle.row()

        val imageCoin = Image(Assets.coinAnimation!!.getKeyFrame(0f))
        imageCoin.setSize(20f, 20f)


        initializeButtons()

        tableMenu = Table()
        tableMenu.defaults().size(58f).padBottom(8f)

        tableMenu.row()
        tableMenu.add<Button?>(buttonLeaderboard)

        tableMenu.row()
        tableMenu.add<Button?>(buttonFacebook)

        tableMenu.row()
        tableMenu.add<Button?>(buttonGoogle)

        tableMenu.row()
        tableMenu.add<Button?>(buttonInviteFriend)


        val tbShop = Table()
        tbShop.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT - tableTitle.getHeight())
        tbShop.setBackground(Assets.backgroundShop)
        tbShop.pad(25f, 5f, 15f, 5f)

        // Container for the leaderboard
        tableContainer = Table()
        tableContainer.defaults().expand().fill().padLeft(10f).padRight(20f).padBottom(10f)

        scroll = ScrollPane(tableContainer, ScrollPaneStyle(null, null, null, null, null))
        scroll.setFadeScrollBars(false)
        scroll.setSize(SCREEN_WIDTH - tableMenu.getWidth(), 420f)
        scroll.setPosition(tableMenu.getWidth() + 1, 0f)
        scroll.variableSizeKnobs = false

        tbShop.add<Table?>(tableMenu).expandY().width(122f)
        tbShop.add<ScrollPane?>(scroll).expand().fill()

        stage.addActor(tableTitle)
        stage.addActor(tbShop)


        updateLeaderboard()


        buttonLeaderboard!!.setChecked(true)
    }

    fun initializeButtons() {
        buttonLeaderboard = Button(Assets.buttonShop, Assets.buttonShopPress, Assets.buttonShopPress)
        buttonFacebook = Button(Assets.buttonFacebook, Assets.buttonFacebookPress, Assets.buttonFacebookPress)
        buttonGoogle = Button(Assets.buttonAchievement, Assets.buttonAchievementPress, Assets.buttonLeaderBoardPress)
        buttonInviteFriend = Button(Assets.buttonSettings, Assets.buttonSettingsPress, Assets.buttonLeaderBoardPress)

        val radioGroup = ButtonGroup<Button?>()
        radioGroup.add(buttonLeaderboard, buttonFacebook, buttonGoogle, buttonInviteFriend)
    }

    override fun draw(delta: Float) {
        Assets.cloudsParallaxBackground!!.render(0f)
    }

    override fun update(delta: Float) {
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            changeScreenWithFadeOut(GameScreen::class.java, game)
            return true
        }
        return super.keyUp(keycode)
    }

    fun updateLeaderboard() {
        tableContainer.clear()
        for (persona in game.arrayPerson) {
            val frame = LeaderBoardFrame(persona)
            tableContainer.add<LeaderBoardFrame?>(frame).expandX().fill()
            tableContainer.row()
        }
    }

    override fun hide() {
    }
}
