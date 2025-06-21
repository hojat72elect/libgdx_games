package com.nopalsoft.ninjarunner.leaderboard

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.leaderboard.Person.AccountType
import com.nopalsoft.ninjarunner.leaderboard.Person.DownloadImageCompleteListener

class LeaderBoardFrame(persona: Person) : Table() {
    var oPersona: Person

    /*
     * I use an image button because it can have a background and an image.
     */
    private var personImage: ImageButton? = null

    var labelName: Label?
    var labelScore: Label?

    var tableAuxiliary: Table //It is necessary because on the left side there is a photo and on the right side there are several textFields in lines.

    init {
        setBackground(Assets.backgroundItemShop)
        pad(5f)
        this.oPersona = persona


        labelName = Label(oPersona.name, Assets.labelStyleSmall)
        labelScore = Label(oPersona.scoreWithFormat, LabelStyle(Assets.smallFont, Color.RED))

        tableAuxiliary = Table()
        tableAuxiliary.left()

        tableAuxiliary.defaults().left()
        tableAuxiliary.add<Label?>(labelName).row()
        tableAuxiliary.add<Label?>(labelScore).row()

        val imRedSocial = when (oPersona.accountType) {
            AccountType.GOOGLE_PLAY -> Image(Assets.imageGoogle)
            AccountType.AMAZON -> Image(Assets.imageAmazon)
            AccountType.FACEBOOK -> Image(Assets.imageFacebook)
        }
        tableAuxiliary.add<Image?>(imRedSocial).size(25f).row()


        if (oPersona.image != null) setPicture(oPersona.image)
        else {
            oPersona.downloadImage(object : DownloadImageCompleteListener {
                override fun imageDownloaded() {
                    setPicture(oPersona.image)
                }

                override fun imageDownloadFail() {
                    setPicture(Assets.photoNA)
                }
            })
        }
        refresh() //So that it puts the information right away. If I delete it until the photo is put, the information is put in.
    }

    fun setPicture(drawable: TextureRegionDrawable?) {
        personImage = ImageButton(ImageButtonStyle(drawable, null, null, Assets.photoFrame, null, null))
        refresh()
    }

    private fun refresh() {
        clear()
        val size = 100f
        if (personImage != null) {
            personImage!!.imageCell.size(size)
            add<ImageButton?>(personImage).size(size)
        } else {
            add().size(size)
        }

        add<Table?>(tableAuxiliary).padLeft(20f).expandX().fill()
    }
}
