package com.nopalsoft.thetruecolor.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.Assets.loadAssetsWithSettings
import com.nopalsoft.thetruecolor.Settings
import com.nopalsoft.thetruecolor.Settings.save
import com.nopalsoft.thetruecolor.screens.BaseScreen

class DialogHelpSettings(currentScreen: BaseScreen) : BaseDialog(currentScreen, WIDTH, HEIGHT, 80f) {
    enum class Languages {
        DEFAULT, ENGLISH, SPANISH, CHINESE, RUSSIAN, FRENCH, JAPANESE, PORTUGUESE
    }

    var colorsTable: Table

    var buttonDefault: TextButton
    var buttonEnglish: TextButton
    var buttonSpanish: TextButton
    var buttonChinese: TextButton
    var buttonRussian: TextButton
    var buttonFrench: TextButton
    var buttonJapanese: TextButton
    var buttonPortuguese: TextButton
    var buttonMore: TextButton

    var dialogMoreLanguages: DialogMoreLanguages

    init {
        setCloseButton(560f)

        val languageLabel = Label(Assets.languagesBundle!!.get("language"), LabelStyle(Assets.fontSmall, Color.BLACK))
        languageLabel.setPosition(getWidth() / 2f - languageLabel.getWidth() / 2f, 555f)
        addActor(languageLabel)

        dialogMoreLanguages = DialogMoreLanguages(currentScreen)

        buttonMore = createButton(Assets.languagesBundle!!.get("more"), null, Assets.flagMoreDrawable)
        buttonMore.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                buttonMore.setChecked(false)
                dialogMoreLanguages.show(currentScreen.stage!!)
            }
        })

        buttonDefault = createButton("Default", Languages.DEFAULT, null)

        buttonEnglish = createButton("English", Languages.ENGLISH, Assets.flagEnglishDrawable)

        buttonSpanish = createButton("Español", Languages.SPANISH, Assets.flagSpanishDrawable)

        buttonChinese = createButton("中文", Languages.CHINESE, Assets.flagChineseDrawable)

        buttonRussian = createButton("Русский", Languages.RUSSIAN, Assets.flagRussianDrawable)
        buttonRussian.label.setFontScale(.7f)

        buttonFrench = createButton("Français", Languages.FRENCH, Assets.flagFrenchDrawable)

        buttonJapanese = createButton("日本語", Languages.JAPANESE, Assets.flagJapaneseDrawable)

        buttonPortuguese = createButton("Português", Languages.PORTUGUESE, Assets.flagPortugueseDrawable)

        when (Settings.selectedLanguage) {
            Languages.DEFAULT -> buttonDefault.setChecked(true)
            Languages.ENGLISH -> buttonEnglish.setChecked(true)
            Languages.SPANISH -> buttonSpanish.setChecked(true)
            Languages.CHINESE -> buttonChinese.setChecked(true)
            Languages.RUSSIAN -> buttonRussian.setChecked(true)
            Languages.FRENCH -> buttonFrench.setChecked(true)
            Languages.JAPANESE -> buttonJapanese.setChecked(true)
            Languages.PORTUGUESE -> buttonPortuguese.setChecked(true)
        }


        val btGroup = ButtonGroup(buttonDefault, buttonEnglish, buttonSpanish, buttonChinese, buttonRussian, buttonFrench, buttonJapanese, buttonPortuguese)
        btGroup.setMaxCheckCount(1)

        val languagesTable = Table()
        languagesTable.setSize(getWidth(), 200f)
        languagesTable.setPosition(0f, 300f)

        languagesTable.defaults().expandX().pad(3f, 10f, 3f, 10f).fill().uniform()

        languagesTable.add<TextButton?>(buttonDefault)
        languagesTable.add<TextButton?>(buttonEnglish)
        languagesTable.add<TextButton?>(buttonSpanish)
        languagesTable.row()
        languagesTable.add<TextButton?>(buttonChinese)
        languagesTable.add<TextButton?>(buttonRussian)
        languagesTable.add<TextButton?>(buttonFrench)
        languagesTable.row()
        languagesTable.add<TextButton?>(buttonJapanese)
        languagesTable.add<TextButton?>(buttonPortuguese)
        languagesTable.add<TextButton?>(buttonMore)
        languagesTable.row()

        // The colors
        colorsTable = Table()
        colorsTable.setSize(getWidth(), 240f)

        fillTableColores()

        addActor(colorsTable)
        addActor(languagesTable)
    }

    private fun fillTableColores() {
        colorsTable.clear()

        val imageBlue = Image(Assets.barTimerDrawable)
        imageBlue.setColor(Color.BLUE)

        val imageCyan = Image(Assets.barTimerDrawable)
        imageCyan.setColor(Color.CYAN)

        val imageGreen = Image(Assets.barTimerDrawable)
        imageGreen.setColor(Color.GREEN)

        val imageYellow = Image(Assets.barTimerDrawable)
        imageYellow.setColor(Color.YELLOW)

        val imagePink = Image(Assets.barTimerDrawable)
        imagePink.setColor(Color.PINK)

        val imageBrown = Image(Assets.barTimerDrawable)
        imageBrown.setColor(Color(.6f, .3f, 0f, 1f))

        val imagePurple = Image(Assets.barTimerDrawable)
        imagePurple.setColor(Color.PURPLE)

        val imageRed = Image(Assets.barTimerDrawable)
        imageRed.setColor(Color.RED)

        colorsTable.defaults().expandX().padTop(5f).padBottom(5f)

        colorsTable.add(getNewLabelWithColor(languages?.get("blue"), Color.BLUE))
        colorsTable.add(imageBlue).size(40f).left()

        colorsTable.add(getNewLabelWithColor(languages?.get("cyan"), Color.CYAN))
        colorsTable.add(imageCyan).size(40f).left()

        colorsTable.row()
        colorsTable.add(getNewLabelWithColor(languages?.get("green"), Color.GREEN))
        colorsTable.add(imageGreen).size(40f).left()

        colorsTable.add(getNewLabelWithColor(languages?.get("yellow"), Color.YELLOW))
        colorsTable.add(imageYellow).size(40f).left()

        colorsTable.row()
        colorsTable.add(getNewLabelWithColor(languages?.get("pink"), Color.PINK))
        colorsTable.add(imagePink).size(40f).left()

        colorsTable.add(getNewLabelWithColor(languages?.get("brown"), Color(.6f, .3f, 0f, 1f)))
        colorsTable.add(imageBrown).size(40f).left()

        colorsTable.row()
        colorsTable.add(getNewLabelWithColor(languages?.get("purple"), Color.PURPLE))
        colorsTable.add(imagePurple).size(40f).left()

        colorsTable.add(getNewLabelWithColor(languages?.get("red"), Color.RED))
        colorsTable.add(imageRed).size(40f).left()
    }

    private fun getNewLabelWithColor(text: String?, color: Color?): Label {
        val colorsLabelStyle = LabelStyle(Assets.fontSmall, color)
        val label = Label(text, colorsLabelStyle)
        if (Settings.selectedLanguage == Languages.RUSSIAN) {
            label.setFontScale(.7f)
        }
        return label
    }

    private fun createButton(texto: String?, language: Languages?, flag: TextureRegionDrawable?): TextButton {
        val buttonAuxiliary = TextButton(texto, Assets.textButtonStyle)
        if (flag != null) {
            buttonAuxiliary.add(Image(flag))
        }
        if (language != null) {
            buttonAuxiliary.addListener(addClickListener(language))
        }
        buttonAuxiliary.label.setFontScale(.8f)
        return buttonAuxiliary
    }

    private fun addClickListener(language: Languages): ClickListener {
        return object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.selectedLanguage = language
                save()
                loadAssetsWithSettings()
                languages = Assets.languagesBundle
                fillTableColores()
            }
        }
    }

    companion object {
        const val WIDTH: Float = 440f
        const val HEIGHT: Float = 600f
    }
}