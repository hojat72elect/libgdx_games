package ca.hojat.shark_adventure.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import ca.hojat.shark_adventure.Assets
import ca.hojat.shark_adventure.SharkAdventureGame
import ca.hojat.shark_adventure.game.GameScreen
import ca.hojat.shark_adventure.screens.Screens

class PauseWindow(val currentScreen: GameScreen) : Group() {

    private var game: SharkAdventureGame?

    private var isVisible = false

    var buttonPlay: Button
    var buttonRefresh: Button
    var buttonHome: Button

    init {
        setSize(300f, 300f)
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 80f)

        game = currentScreen.game
        setBackGround()

        val titleTable = Table()
        titleTable.setSize(getWidth() - 80, 50f)
        titleTable.setPosition(getWidth() / 2f - titleTable.getWidth() / 2f, getHeight() - 30)
        titleTable.setBackground(Assets.titleBackgroundDrawable)

        val titleLabel = Label("Paused", Assets.lblStyle)

        titleTable.add<Label?>(titleLabel).fill().padBottom(10f)
        addActor(titleTable)

        buttonPlay = Button(Assets.buttonRight, Assets.buttonRightPressed)
        buttonPlay.setSize(70f, 70f)
        buttonPlay.setPosition(getWidth() / 2f - buttonPlay.getWidth() / 2f, 170f)
        buttonPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                currentScreen.setRunning(false)
            }
        })

        buttonRefresh = Button(Assets.buttonRefresh, Assets.buttonRefreshPressed)
        buttonRefresh.setSize(70f, 70f)
        buttonRefresh.setPosition(getWidth() / 2f + 25, 80f)
        buttonRefresh.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                currentScreen.game.setScreen(GameScreen(game!!, false))
            }
        })

        buttonHome = Button(Assets.buttonHome, Assets.buttonHomePressed)
        buttonHome.setSize(70f, 70f)
        buttonHome.setPosition(getWidth() / 2f - buttonHome.getWidth() - 25, 80f)
        buttonHome.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                currentScreen.game.setScreen(GameScreen(game!!, true))
            }
        })

        addActor(buttonPlay)
        addActor(buttonRefresh)
        addActor(buttonHome)
    }

    private fun setBackGround() {
        val img = Image(Assets.windowBackgroundDrawable)
        img.setSize(getWidth(), getHeight())
        addActor(img)
    }

    fun show(stage: Stage) {
        setOrigin(getWidth() / 2f, getHeight() / 2f)
        setX(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f)

        setScale(.5f)
        addAction(Actions.sequence(Actions.scaleTo(1f, 1f, ANIMATION_DURATION)))

        isVisible = true
        stage.addActor(this)
    }

    override fun isVisible(): Boolean {
        return isVisible
    }

    fun hide() {
        isVisible = false
        remove()
    }

    companion object {
        const val ANIMATION_DURATION: Float = .3f
    }
}
