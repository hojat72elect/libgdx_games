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

class DialogHelpSettings(currentScreen: BaseScreen) :
    BaseDialog(currentScreen, WIDTH, HEIGHT, 80F) {

    private var tableColors = Table()

    private val buttonDefault = createButton("Default", Languages.DEFAULT, null)
    private val buttonEnglish = createButton("English", Languages.ENGLISH, Assets.flagEnglish)
    private val buttonSpanish = createButton("Espalier", Languages.SPANISH, Assets.flagSpanish)
    private val buttonChineseTaiwan = createButton("中文", Languages.CHINESE_TAIWAN, Assets.flagChinese_TW)
    private val buttonRussian = createButton("Русский", Languages.RUSSIAN, Assets.flagRussian)
    private val buttonFrench = createButton("Français", Languages.FRENCH, Assets.flagFrench)
    private val buttonJapanese = createButton("日本語", Languages.JAPANESE, Assets.flagJapanese)
    private val buttonPortugese = createButton("Portuguese", Languages.PORTUGUESE, Assets.flagPortugese)
    private val buttonMore = createButton(languages.get("more"), null, Assets.flagMore)

    private val dialogMoreLanguages = DialogMoreLanguages(currentScreen)


    init {
        setCloseButton(560f)
        val labelLanguage = Label(languages.get("language"), LabelStyle(Assets.fontSmall, Color.BLACK))
        labelLanguage.setPosition(width / 2f - labelLanguage.width / 2f, 555f)
        addActor(labelLanguage)

        buttonMore.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                buttonMore.isChecked = false
                dialogMoreLanguages.show(currentScreen.stage)
            }
        })
        buttonRussian.label.setFontScale(.7f)

        when (Settings.selectedLanguage) {
            Languages.DEFAULT -> buttonDefault.isChecked = true
            Languages.ENGLISH -> buttonEnglish.isChecked = true
            Languages.SPANISH -> buttonSpanish.isChecked = true
            Languages.CHINESE_TAIWAN -> buttonChineseTaiwan.isChecked = true
            Languages.RUSSIAN -> buttonRussian.isChecked = true
            Languages.FRENCH -> buttonFrench.isChecked = true
            Languages.JAPANESE -> buttonJapanese.isChecked = true
            Languages.PORTUGUESE -> buttonPortugese.isChecked = true
        }

        val btGroup = ButtonGroup(
            buttonDefault,
            buttonEnglish,
            buttonSpanish,
            buttonChineseTaiwan,
            buttonRussian,
            buttonFrench,
            buttonJapanese,
            buttonPortugese
        )
        btGroup.setMaxCheckCount(1)

        val tableLanguages = Table()
        tableLanguages.setSize(width, 200f)
        tableLanguages.setPosition(0f, 300f)

        tableLanguages.defaults().expandX().pad(3f, 10f, 3f, 10f).fill().uniform()

        tableLanguages.add(buttonDefault)
        tableLanguages.add(buttonEnglish)
        tableLanguages.add(buttonSpanish)
        tableLanguages.row()
        tableLanguages.add(buttonChineseTaiwan)
        tableLanguages.add(buttonRussian)
        tableLanguages.add(buttonFrench)
        tableLanguages.row()
        tableLanguages.add(buttonJapanese)
        tableLanguages.add(buttonPortugese)
        tableLanguages.add(buttonMore)
        tableLanguages.row()

        // The colors
        tableColors = Table()
        tableColors.setSize(width, 240f)

        fillTableColors()

        addActor(tableColors)
        addActor(tableLanguages)
    }

    private fun createButton(
        text: String,
        language: Languages?,
        flag: TextureRegionDrawable?
    ): TextButton {
        val textButton = TextButton(text, Assets.textButtonStyle)
        if (flag != null) {
            textButton.add(Image(flag))
        }
        if (language != null) {
            textButton.addListener(addClickListener(language))
        }
        textButton.label.setFontScale(.8f)
        return textButton
    }

    private fun addClickListener(language: Languages): ClickListener {
        return object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.selectedLanguage = language
                save()
                loadAssetsWithSettings()
                fillTableColors()
            }
        }
    }

    private fun fillTableColors() {
        tableColors.clear()

        val iBlue = Image(Assets.barTimer)
        iBlue.color = Color.BLUE

        val iCyan = Image(Assets.barTimer)
        iCyan.color = Color.CYAN

        val iGreen = Image(Assets.barTimer)
        iGreen.color = Color.GREEN

        val iYellow = Image(Assets.barTimer)
        iYellow.color = Color.YELLOW

        val iPink = Image(Assets.barTimer)
        iPink.color = Color.PINK

        val iBrown = Image(Assets.barTimer)
        iBrown.color = Color(.6f, .3f, 0f, 1f)

        val iPurple = Image(Assets.barTimer)
        iPurple.color = Color.PURPLE

        val iRed = Image(Assets.barTimer)
        iRed.color = Color.RED

        tableColors.defaults().expandX().padTop(5f).padBottom(5f)

        tableColors.add(getNewLabelWithColor(languages["blue"], Color.BLUE))
        tableColors.add(iBlue).size(40f).left()

        tableColors.add(getNewLabelWithColor(languages["cyan"], Color.CYAN))
        tableColors.add(iCyan).size(40f).left()

        tableColors.row()
        tableColors.add(getNewLabelWithColor(languages["green"], Color.GREEN))
        tableColors.add(iGreen).size(40f).left()

        tableColors.add(getNewLabelWithColor(languages["yellow"], Color.YELLOW))
        tableColors.add(iYellow).size(40f).left()

        tableColors.row()
        tableColors.add(getNewLabelWithColor(languages["pink"], Color.PINK))
        tableColors.add(iPink).size(40f).left()

        tableColors.add(getNewLabelWithColor(languages["brown"], Color(.6f, .3f, 0f, 1f)))
        tableColors.add(iBrown).size(40f).left()

        tableColors.row()
        tableColors.add(getNewLabelWithColor(languages["purple"], Color.PURPLE))
        tableColors.add(iPurple).size(40f).left()

        tableColors.add(getNewLabelWithColor(languages["red"], Color.RED))
        tableColors.add(iRed).size(40f).left()
    }

    private fun getNewLabelWithColor(text: String, color: Color): Label {
        val labelStyleColors = LabelStyle(Assets.fontSmall, color)
        val label = Label(text, labelStyleColors)
        if (Settings.selectedLanguage == Languages.RUSSIAN) {
            label.setFontScale(.7f)
        }
        return label
    }

    enum class Languages {
        DEFAULT, ENGLISH, SPANISH, CHINESE_TAIWAN, RUSSIAN, FRENCH, JAPANESE, PORTUGUESE
    }

    companion object {
        private const val WIDTH = 440F
        private const val HEIGHT = 600F

    }
}