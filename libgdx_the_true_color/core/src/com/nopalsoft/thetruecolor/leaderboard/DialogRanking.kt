package com.nopalsoft.thetruecolor.leaderboard

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.scene2d.DialogFacebook
import com.nopalsoft.thetruecolor.screens.BaseScreen
import com.nopalsoft.thetruecolor.screens.MainMenuScreen

class DialogRanking(screen: MainMenuScreen) : Group() {


    private val rankingTitle = Label(Assets.languages["ranking"], Label.LabelStyle(Assets.fontSmall, Color.WHITE))
    private val btFacebook = Button(Assets.buttonFacebook)
    private val btLoginKeyFrame = Assets.buttonGoogle
    private val btGoogle = Button(btLoginKeyFrame)
    private val dialogFacebook = DialogFacebook(screen)
    private val container = Table()


    init {
        setBounds(BaseScreen.SCREEN_WIDTH / 2f - WIDTH / 2f, 210f, WIDTH, HEIGHT)
        setBackground(Assets.dialogRanking)
        rankingTitle.setPosition(15f, 328f)

        screen.addPressEffect(btFacebook)
        btFacebook.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                dialogFacebook.show(stage)
            }
        })

        screen.addPressEffect(btGoogle)
        btGoogle.addListener(object : ClickListener() {
        })

        val tableSocial = Table()
        tableSocial.setSize(130f, 50f)
        tableSocial.setPosition(255f, 328f)
        tableSocial.defaults().expandX().size(50f).right()
        tableSocial.add(btFacebook)

        if (Gdx.app.type != ApplicationType.WebGL && Gdx.app.type != ApplicationType.iOS) {
            tableSocial.add(btGoogle)
        }

        addActor(rankingTitle)
        addActor(tableSocial)

        val scroll = ScrollPane(container)
        scroll.setSize(WIDTH, 320f)
        scroll.setPosition(0f, 0f)

        addActor(scroll)
        container.top()
    }

    private fun setBackground(dialogRanking: NinePatchDrawable) {
        val imageDialogRanking = Image(dialogRanking)
        imageDialogRanking.setSize(width, height)
        addActor(imageDialogRanking)
    }

    fun addPerson(person: Person) {
        container.add(person)
        container.row()
    }

    fun clearLeaderboard() {
        container.clear()
    }

    companion object {

        const val WIDTH = 400F
        private const val HEIGHT = 385F
    }
}