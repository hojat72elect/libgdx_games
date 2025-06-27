package io.github.masamune.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import io.github.masamune.ui.model.GameMenuViewModel
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.widget.OptionTable
import io.github.masamune.ui.widget.PopupTable
import io.github.masamune.ui.widget.optionTable
import io.github.masamune.ui.widget.popupTable
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.scene2d


@Scene2dDsl
class GameMenuView(
    model: GameMenuViewModel,
    skin: Skin,
) : View<GameMenuViewModel>(skin, model), KTable {

    private val optionTable: OptionTable
    private val popupTable: PopupTable

    init {
        setFillParent(true)
        optionTable = optionTable(skin) { cell ->
            background = skin.getDrawable("dialog_frame")
            cell.width(250f)
        }

        popupTable = scene2d.popupTable(
            message = i18nTxt(I18NKey.GENERAL_CONFIRM_QUIT),
            options = listOf(i18nTxt(I18NKey.GENERAL_NO), i18nTxt(I18NKey.GENERAL_YES)),
            skin
        )

        registerOnPropertyChanges()
    }

    override fun registerOnPropertyChanges() {
        viewModel.onPropertyChange(GameMenuViewModel::options) { options ->
            if (options.isEmpty()) {
                isVisible = false
                return@onPropertyChange
            }

            optionTable.clearOptions()
            options.forEach { optionTable.option(it) }
            optionTable.padTop(15f)
            isVisible = true
        }
    }

    override fun onDownPressed() {
        if (popupTable.hasParent()) {
            if (popupTable.nextOption()) {
                viewModel.playSndMenuClick()
            }
            return
        }

        if (optionTable.nextOption()) {
            viewModel.playSndMenuClick()
        }
    }

    override fun onUpPressed() {
        if (popupTable.hasParent()) {
            if (popupTable.prevOption()) {
                viewModel.playSndMenuClick()
            }
            return
        }

        if (optionTable.prevOption()) {
            viewModel.playSndMenuClick()
        }
    }

    override fun onSelectPressed() {
        if (popupTable.hasParent()) {
            optionTable.resumeSelectAnimation()
            popupTable.remove()
            if (popupTable.selectedOption == 0) {
                // don't quit game
                viewModel.playSndMenuAbort()
            } else {
                isVisible = false
                viewModel.triggerOption(optionTable.numOptions - 1)
            }
            return
        }

        if (optionTable.selectedOption == optionTable.numOptions - 1) {
            // open quit game popup
            popupTable.firstOption()
            stage.addActor(popupTable)
            optionTable.stopSelectAnimation()
            return
        }

        viewModel.triggerOption(optionTable.selectedOption)
    }

    override fun onBackPressed() {
        if (popupTable.hasParent()) {
            optionTable.resumeSelectAnimation()
            viewModel.playSndMenuAbort()
            popupTable.remove()
            return
        }

        viewModel.triggerClose()
    }
}

@Scene2dDsl
fun <S> KWidget<S>.gameMenuView(
    model: GameMenuViewModel,
    skin: Skin,
    init: (@Scene2dDsl GameMenuView).(S) -> Unit = {},
): GameMenuView = actor(GameMenuView(model, skin), init)
