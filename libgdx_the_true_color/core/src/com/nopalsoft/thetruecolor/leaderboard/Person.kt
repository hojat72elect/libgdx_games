package com.nopalsoft.thetruecolor.leaderboard

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.thetruecolor.Assets

class Person(val accountType: AccountType, val id: String, name: String, var score: Long) : Group(),
    Comparable<Person> {

    private val labelName = Label(name, Label.LabelStyle(Assets.fontSmall, Color.BLACK))
    private val labelScore = Label(formatScore(), Label.LabelStyle(Assets.fontSmall, Color.RED))


    init {

        setBounds(0f, 0f, WIDTH, HEIGHT)
        val imageAccount = getImage(accountType)
        labelName.setFontScale(.7f)
        labelName.setPosition(140f, 36f)
        labelScore.setPosition(140f, 5f)

        addActor(imageAccount)
        addActor(labelName)
        addActor(labelScore)


        // Separator
        val imageHeader = Image(Assets.header)
        imageHeader.setPosition(0f, 0f)
        imageHeader.setSize(WIDTH, 5f)
        addActor(imageHeader)
    }


    private fun getImage(accountType: AccountType): Image {
        val accountKey = when (accountType) {
            AccountType.AMAZON -> Assets.buttonAmazon
            AccountType.FACEBOOK -> Assets.buttonFacebook
            AccountType.GOOGLE_PLAY -> Assets.buttonGoogle
            else -> Assets.buttonGoogle
        }
        val imageAccount = Image(accountKey)
        imageAccount.setSize(30f, 30f)
        imageAccount.setPosition(10f, HEIGHT / 2f - imageAccount.height / 2f)
        return imageAccount
    }

    fun setPicture(drawable: TextureRegionDrawable) {

        // I use an image button because it can have a background and an image.
        val imageButtonCharacter =
            ImageButton(ImageButtonStyle(drawable, null, null, Assets.photoFrame, null, null))
        imageButtonCharacter.setSize(60f, 60f)
        imageButtonCharacter.imageCell.size(60f)
        imageButtonCharacter.setPosition(60f, HEIGHT / 2f - imageButtonCharacter.height / 2f)
        addActor(imageButtonCharacter)
    }

    fun formatScore(): String {
        var score = score.toString()
        val floatPos = if (score.contains(".")) score.length - score.indexOf(".") else 0
        val nGroups = (score.length - floatPos - 1 - (if (score.contains("-")) 1 else 0)) / 3
        for (i in 0 until nGroups) {
            val commaPos = score.length - i * 4 - 3 - floatPos
            score = score.substring(0, commaPos) + "," + score.substring(commaPos)
        }
        return score
    }


    override fun compareTo(other: Person) = other.score.compareTo(score)

    override fun equals(other: Any?): Boolean {
        return if (other is Person) {
            id == other.id && accountType == other.accountType
        } else false
    }

    fun updateData(_name: String, _score: Long) {
        name = _name
        score = _score
        labelName.setText(name)
        labelScore.setText(formatScore())
    }

    override fun hashCode(): Int {
        var result = accountType.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + score.hashCode()
        result = 31 * result + labelName.hashCode()
        result = 31 * result + labelScore.hashCode()
        return result
    }

    enum class AccountType {
        GOOGLE_PLAY, FACEBOOK, AMAZON
    }

    companion object {
        private const val WIDTH = DialogRanking.WIDTH - 5
        private const val HEIGHT = 75f
    }
}