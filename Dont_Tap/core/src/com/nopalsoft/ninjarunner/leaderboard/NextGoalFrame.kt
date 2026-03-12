package com.nopalsoft.ninjarunner.leaderboard

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.leaderboard.Person.DownloadImageCompleteListener

class NextGoalFrame(x: Float, y: Float) : Group() {
    var person: Person? = null

    var labelName: Label
    var labelPlayerScore: Label
    var labelRemainingPointsToOvercome: Label


    init {
        setBounds(x, y, WIDTH, HEIGHT)

        labelName = Label("", Assets.labelStyleSmall)
        labelName.setFontScale(.5f)
        labelName.setPosition(60f, 60f)

        labelPlayerScore = Label("", Assets.labelStyleSmall)
        labelPlayerScore.setFontScale(.5f)
        labelPlayerScore.setPosition(60f, 40f)

        labelRemainingPointsToOvercome = Label("", Assets.labelStyleSmall)
        labelRemainingPointsToOvercome.setFontScale(.5f)
        labelRemainingPointsToOvercome.setPosition(60f, 20f)

        addActor(labelName)
        addActor(labelPlayerScore)
        addActor(labelRemainingPointsToOvercome)


        debug()
    }

    /**
     * Puts a new person in the frame.
     */
    fun updatePersona(person: Person) {
        this.person = person

        labelName.setText(this.person!!.name)
        labelPlayerScore.setText(this.person!!.scoreWithFormat)


        if (this.person!!.image != null) setPicture(this.person!!.image)
        else {
            this.person!!.downloadImage(object : DownloadImageCompleteListener {
                override fun imageDownloaded() {
                    setPicture(this@NextGoalFrame.person!!.image)
                }

                override fun imageDownloadFail() {
                    setPicture(Assets.photoNA)
                }
            })
        }
    }

    private fun setPicture(drawable: TextureRegionDrawable?) {
        // I use an image button because it can have a background and an image.
        val personImage = ImageButton(ImageButtonStyle(drawable, null, null, Assets.photoFrame, null, null))
        personImage.setSize(50f, 50f)
        personImage.imageCell.size(50f)
        personImage.setPosition(5f, HEIGHT / 2f - personImage.getHeight() / 2f)
        addActor(personImage)
    }

    companion object {
        const val WIDTH: Float = 170f
        const val HEIGHT: Float = 80f
    }
}
