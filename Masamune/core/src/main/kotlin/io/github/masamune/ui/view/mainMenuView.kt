package io.github.masamune.ui.view

import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.scenes.scene2d.actions.Actions.forever
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Scaling
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.MainMenuViewModel
import io.github.masamune.ui.widget.PopupTable
import io.github.masamune.ui.widget.popupTable
import ktx.actors.alpha
import ktx.actors.plusAssign
import ktx.actors.then
import ktx.scene2d.KButtonTable
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.buttonGroup
import ktx.scene2d.checkBox
import ktx.scene2d.image
import ktx.scene2d.label
import ktx.scene2d.progressBar
import ktx.scene2d.scene2d
import ktx.scene2d.table
import java.util.*


@Scene2dDsl
class MainMenuView(
    model: MainMenuViewModel,
    webView: Boolean,
    skin: Skin,
) : View<MainMenuViewModel>(skin, model), KTable {

    private val startGameLabel: Label
    private val continueGameLabel: Label
    private val musicBar: ProgressBar
    private val soundBar: ProgressBar
    private val quitGameLabel: Label
    private var currentSelection: Table
    private val languageBtnGroup: KButtonTable
    private val popupTable: PopupTable

    init {
        left().bottom().padBottom(30f).padLeft(10f)
        setFillParent(true)

        table(skin) {
            this.background = skin.getDrawable("nine_path_bg_2")
            this.defaults().pad(10f)

            this@MainMenuView.currentSelection = table(skin) { startGameTableCell ->
                selectionImage(skin, visible = true)
                this@MainMenuView.startGameLabel =
                    label(this@MainMenuView.i18nTxt(I18NKey.GENERAL_NEW_GAME_TITLE), "dialog_option", skin) {
                    it.expandX()
                }
                startGameTableCell.uniformX().growX().row()
            }

            table(skin) { continueGameLabelCell ->
                selectionImage(skin)
                this@MainMenuView.continueGameLabel =
                    label(this@MainMenuView.i18nTxt(I18NKey.GENERAL_CONTINUE), "dialog_option", skin) {
                        if (model.hasNoSaveState()) {
                            this.alpha = 0.25f
                        }
                        it.expandX()
                    }
                continueGameLabelCell.uniformX().growX().row()
            }

            table(skin) { pbTableCell ->
                this.left()
                label(this@MainMenuView.i18nTxt(I18NKey.GENERAL_VOLUME_MUSIC), "dialog_content", skin) {
                    it.padBottom(5f).colspan(2).row()
                }
                selectionImage(skin)
                this@MainMenuView.musicBar = progressBar(0f, 1f, 0.05f, false, "yellow", skin) {
                    this.value = model.musicVolume
                    this.name = "volumePB"
                    it.width(200f)
                }
                pbTableCell.uniformX().growX().row()
            }

            table(skin) { pbTableCell ->
                this.left()
                label(this@MainMenuView.i18nTxt(I18NKey.GENERAL_VOLUME_SOUND), "dialog_content", skin) {
                    it.padBottom(5f).colspan(2).row()
                }
                selectionImage(skin)
                this@MainMenuView.soundBar = progressBar(0f, 1f, 0.05f, false, "yellow", skin) {
                    this.value = model.soundVolume
                    this.name = "volumePB"
                    it.width(200f)
                }
                pbTableCell.uniformX().growX().row()
            }

            table(skin) { languageTableCell ->
                val isGerman = this@MainMenuView.viewModel.language().contains("de", ignoreCase = true)
                selectionImage(skin)
                this@MainMenuView.languageBtnGroup = buttonGroup(1, 1, skin) {
                    checkBox(this@MainMenuView.i18nTxt(I18NKey.GENERAL_LANGUAGE_ENGLISH), "default", skin) { boxCell ->
                        this.labelCell.padLeft(5f)
                        this.isChecked = !isGerman
                        boxCell.padRight(15f)
                    }
                    checkBox(this@MainMenuView.i18nTxt(I18NKey.GENERAL_LANGUAGE_GERMAN), "default", skin) {
                        this.isChecked = isGerman
                        this.labelCell.padLeft(5f)
                    }
                    it.expandX()
                }
                languageTableCell.uniformX().growX().row()
            }

            table(skin) { quitGameTableCell ->
                selectionImage(skin)
                this@MainMenuView.quitGameLabel =
                    label(this@MainMenuView.i18nTxt(I18NKey.GENERAL_QUIT_GAME), "dialog_option", skin) {
                    it.expandX()
                }
                quitGameTableCell.uniformX().growX()
            }

            if (webView) {
                this@MainMenuView.quitGameLabel.parent.remove()
            }
        }

        popupTable = scene2d.popupTable("", listOf(i18nTxt(I18NKey.GENERAL_NO), i18nTxt(I18NKey.GENERAL_YES)), skin)

        registerOnPropertyChanges()
    }

    override fun registerOnPropertyChanges() = Unit

    override fun onUpPressed() {
        if (popupTable.hasParent()) {
            if (popupTable.prevOption()) {
                viewModel.playSndMenuClick()
            }
            return
        }

        val parentTable = currentSelection.parent
        var currentIndex = parentTable.children.indexOf(currentSelection)
        if (currentIndex <= 0) {
            return
        }

        currentSelection.findActor<Image>("selection").isVisible = false
        currentIndex -= 1
        if (currentIndex == 1 && viewModel.hasNoSaveState()) {
            currentIndex = 0
        }
        currentSelection = parentTable.getChild(currentIndex) as Table
        currentSelection.findActor<Image>("selection").isVisible = true
        viewModel.playSndMenuClick()
    }

    override fun onDownPressed() {
        if (popupTable.hasParent()) {
            if (popupTable.nextOption()) {
                viewModel.playSndMenuClick()
            }
            return
        }

        val parentTable = currentSelection.parent
        var currentIndex = parentTable.children.indexOf(currentSelection)
        if (currentIndex >= parentTable.children.size - 1) {
            return
        }

        currentSelection.findActor<Image>("selection").isVisible = false
        currentIndex += 1
        if (currentIndex == 1 && viewModel.hasNoSaveState()) {
            currentIndex = 2
        }
        currentSelection = parentTable.getChild(currentIndex) as Table
        currentSelection.findActor<Image>("selection").isVisible = true
        viewModel.playSndMenuClick()
    }

    override fun onRightPressed() {
        if (popupTable.hasParent()) {
            return
        }

        val parentTable = currentSelection.parent
        when (parentTable.children.indexOf(currentSelection)) {
            2 -> {
                val pb = currentSelection.findActor<ProgressBar>("volumePB")
                pb.setValue(pb.value + pb.stepSize)
                viewModel.musicVolume = pb.value
            }

            3 -> {
                val pb = currentSelection.findActor<ProgressBar>("volumePB")
                pb.setValue(pb.value + pb.stepSize)
                viewModel.soundVolume = pb.value
                viewModel.playSndMenuClick()
            }

            4 -> {
                val chkGer: CheckBox = languageBtnGroup.getChild(1) as CheckBox
                if (chkGer.isChecked) {
                    return
                }
                openPopup(i18nTxt(I18NKey.GENERAL_LANGUAGE_CHANGE_CONFIRM), "de")
            }
        }
    }

    private fun openPopup(message: String, userObject: String) {
        popupTable.message(message)
        popupTable.firstOption()
        popupTable.userObject = userObject
        stage.addActor(popupTable)
        viewModel.playSndMenuAccept()
    }

    override fun onLeftPressed() {
        if (popupTable.hasParent()) {
            return
        }

        val parentTable = currentSelection.parent
        when (parentTable.children.indexOf(currentSelection)) {
            2 -> {
                val pb = currentSelection.findActor<ProgressBar>("volumePB")
                pb.setValue(pb.value - pb.stepSize)
                viewModel.musicVolume = pb.value
            }

            3 -> {
                val pb = currentSelection.findActor<ProgressBar>("volumePB")
                pb.setValue(pb.value - pb.stepSize)
                viewModel.soundVolume = pb.value
                viewModel.playSndMenuClick()
            }

            4 -> {
                val chkEng: CheckBox = languageBtnGroup.getChild(0) as CheckBox
                if (chkEng.isChecked) {
                    return
                }
                openPopup(i18nTxt(I18NKey.GENERAL_LANGUAGE_CHANGE_CONFIRM), "en")
            }
        }
    }

    private fun onSelectPopupTable() {
        popupTable.remove()
        if (popupTable.selectedOption == 0) {
            // no selected -> don't do anything
            viewModel.playSndMenuAbort()
            return
        }

        when (popupTable.userObject) {
            "de" -> {
                viewModel.updateLanguage(Locale.GERMAN)
                val chk: CheckBox = languageBtnGroup.getChild(1) as CheckBox
                chk.isChecked = true
            }

            "en" -> {
                viewModel.updateLanguage(Locale.ENGLISH)
                val chk: CheckBox = languageBtnGroup.getChild(0) as CheckBox
                chk.isChecked = true
            }

            "startGame" -> viewModel.startGame()
        }
    }

    override fun onSelectPressed() {
        if (popupTable.hasParent()) {
            onSelectPopupTable()
            return
        }

        val parentTable = currentSelection.parent
        when (parentTable.children.indexOf(currentSelection)) {
            0 -> {
                if (viewModel.hasSaveState()) {
                    openPopup(i18nTxt(I18NKey.GENERAL_NEW_GAME_CONFIRM), "startGame")
                } else {
                    viewModel.startGame()
                }
            }

            1 -> viewModel.continueGame()
            5 -> viewModel.quitGame()
        }
    }

    companion object {
        private fun KTable.selectionImage(skin: Skin, visible: Boolean = false) =
            image(skin.getDrawable("arrow")) { cell ->
                setScaling(Scaling.contain)
                this += forever(fadeOut(0.5f) then fadeIn(0.25f) then delay(0.25f))
                cell.padRight(5f).left()
                this.isVisible = visible
                this.name = "selection"
            }
    }
}

@Scene2dDsl
fun <S> KWidget<S>.mainMenuView(
    model: MainMenuViewModel,
    webView: Boolean,
    skin: Skin,
    init: (@Scene2dDsl MainMenuView).(S) -> Unit = {},
): MainMenuView = actor(MainMenuView(model, webView, skin), init)
