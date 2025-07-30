package com.nopalsoft.thetruecolor.leaderboard

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.thetruecolor.Assets

class Person(accountType: AccountType, personId: String, personName: String?, personScore: Long) : Group(), Comparable<Person> {


    enum class AccountType {
        GOOGLE_PLAY, FACEBOOK, AMAZON
    }

    var accountType: AccountType?

    val personId: String
    var personName: String?
    var personScore: Long

    var labelName: Label
    var labelScore: Label?

    init {
        setBounds(0f, 0f, WIDTH, HEIGHT)

        this.accountType = accountType
        this.personId = personId
        this.personName = personName
        this.personScore = personScore
        val accountIconDrawable: TextureRegionDrawable? = when (accountType) {
            AccountType.AMAZON -> Assets.buttonAmazonDrawable
            AccountType.FACEBOOK -> Assets.buttonFacebookDrawable
            AccountType.GOOGLE_PLAY -> Assets.buttonGoogleDrawable
        }

        val imagenCuenta = Image(accountIconDrawable)
        imagenCuenta.setSize(30f, 30f)
        imagenCuenta.setPosition(10f, HEIGHT / 2f - imagenCuenta.getHeight() / 2f)

        labelName = Label(personName, LabelStyle(Assets.fontSmall, Color.BLACK))
        labelName.setFontScale(.7f)
        labelName.setPosition(140f, 36f)

        labelScore = Label(formatScore(), LabelStyle(Assets.fontSmall, Color.RED))
        labelScore!!.setPosition(140f, 5f)

        addActor(imagenCuenta)
        addActor(labelName)
        addActor(labelScore)

        // Separator
        val image = Image(Assets.header)
        image.setPosition(0f, 0f)
        image.setSize(WIDTH, 5f)
        addActor(image)
    }

    // Taken from http://stackoverflow.com/a/15329259/3479489
    fun formatScore(): String {
        var scoreString = personScore.toString()
        val floatPos = if (scoreString.contains(".")) scoreString.length - scoreString.indexOf(".") else 0
        val nGroups = (scoreString.length - floatPos - 1 - (if (scoreString.contains("-")) 1 else 0)) / 3
        for (i in 0..<nGroups) {
            val commaPos = scoreString.length - i * 4 - 3 - floatPos
            scoreString = scoreString.substring(0, commaPos) + "," + scoreString.substring(commaPos)
        }
        return scoreString
    }

    override fun compareTo(other: Person): Int {
        return other.personScore.compareTo(personScore)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Person) {
            val objPerson = other
            return personId == objPerson.personId && accountType == objPerson.accountType
        } else return false
    }

    companion object {
        const val WIDTH = DialogRanking.WIDTH - 5F
        const val HEIGHT = 75F
    }
}
