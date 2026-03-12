package ca.hojat.sokoban.screens

import ca.hojat.sokoban.Assets
import ca.hojat.sokoban.SokobanGame
import ca.hojat.sokoban.scene2d.LevelSelector
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class MainMenuScreen(game: SokobanGame) : Screens(game) {
    private var levelSelector: LevelSelector = LevelSelector(this)

    private var tableMenu: Table
    private var buttonNextPage: Button
    private var buttonPreviousPage: Button = Button(Assets.buttonLeftDrawable, Assets.buttonLeftPressedDrawable)

    init {
        buttonPreviousPage.setSize(75f, 75f)
        buttonPreviousPage.setPosition(65f, 220f)
        buttonPreviousPage.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                right()
            }
        })
        buttonNextPage = Button(Assets.buttonRightDrawable, Assets.buttonRightPressedDrawable)
        buttonNextPage.setSize(75f, 75f)
        buttonNextPage.setPosition(660f, 220f)
        buttonNextPage.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                left()
            }
        })

        tableMenu = Table()
        tableMenu.defaults().size(80f).pad(7.5f)

        tableMenu.pack()
        tableMenu.setPosition(SCREEN_WIDTH / 2f - tableMenu.width / 2f, 20f)

        stage!!.addActor(levelSelector)
        stage!!.addActor(tableMenu)
        stage!!.addActor(buttonPreviousPage)
        stage!!.addActor(buttonNextPage)
    }

    override fun update(delta: Float) {
    }

    override fun draw(delta: Float) {
        Assets.background!!.render(delta)
    }

    override fun right() {
        levelSelector.previousPage()
    }

    override fun left() {
        levelSelector.nextPage()
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.LEFT, Input.Keys.A -> {
                right()
            }

            Input.Keys.RIGHT, Input.Keys.D -> {
                left()
            }

            Input.Keys.ESCAPE, Input.Keys.BACK -> {
                Gdx.app.exit()
            }
        }

        return true
    }
}
