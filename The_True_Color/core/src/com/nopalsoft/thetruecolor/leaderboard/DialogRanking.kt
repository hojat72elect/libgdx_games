package com.nopalsoft.thetruecolor.leaderboard

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.scene2d.DialogAmazon
import com.nopalsoft.thetruecolor.scene2d.DialogFacebook
import com.nopalsoft.thetruecolor.scene2d.DialogGoogle
import com.nopalsoft.thetruecolor.screens.BaseScreen
import com.nopalsoft.thetruecolor.screens.MainMenuScreen

class DialogRanking(menuScreen: MainMenuScreen) : Group() {

    var rankingTitle: Label?

    var buttonFacebook: Button
    var buttonGoogle: Button

    var ventanaFacebook: DialogFacebook
    var ventanaGoogle: DialogGoogle
    var ventanaAmazon: DialogAmazon?

    var contenedor: Table

    init {
        setBounds(BaseScreen.SCREEN_WIDTH / 2f - WIDTH / 2f, 210f, WIDTH, HEIGHT)
        setBackground(Assets.rankingDialogDrawable)

        rankingTitle = Label(Assets.languagesBundle!!.get("ranking"), LabelStyle(Assets.fontSmall, Color.WHITE))
        rankingTitle!!.setPosition(15f, 328f)

        ventanaFacebook = DialogFacebook(menuScreen)
        ventanaGoogle = DialogGoogle(menuScreen)
        ventanaAmazon = DialogAmazon(menuScreen)

        buttonFacebook = Button(Assets.buttonFacebookDrawable)

        menuScreen.addPressEffect(buttonFacebook)
        buttonFacebook.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                ventanaFacebook.show(stage)
            }
        })

        val btLoginKeyFrame = Assets.buttonGoogleDrawable


        buttonGoogle = Button(btLoginKeyFrame)

        menuScreen.addPressEffect(buttonGoogle)
        buttonGoogle.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                ventanaGoogle.show(stage)
            }
        })

        val tbSocial = Table()
        tbSocial.setSize(130f, 50f)
        tbSocial.setPosition(255f, 328f)
        tbSocial.defaults().expandX().size(50f).right()
        tbSocial.add<Button?>(buttonFacebook)

        if (Gdx.app.getType() != ApplicationType.WebGL && Gdx.app.getType() != ApplicationType.iOS) {
            tbSocial.add<Button?>(buttonGoogle)
        }

        addActor(rankingTitle)
        addActor(tbSocial)

        contenedor = Table()

        val scroll = ScrollPane(contenedor)
        scroll.setSize(WIDTH, 320f)
        scroll.setPosition(0f, 0f)

        addActor(scroll)

        contenedor.top()
    }

    private fun setBackground(dialogRanking: NinePatchDrawable?) {
        val img = Image(dialogRanking)
        img.setSize(getWidth(), getHeight())
        addActor(img)
    }

    fun addPerson(person: Person?) {
        contenedor.add<Person?>(person)
        contenedor.row()
    }

    fun clearLeaderboard() {
        contenedor.clear()
    }

    companion object {
        const val WIDTH: Float = 400f
        const val HEIGHT: Float = 385f
    }
}
