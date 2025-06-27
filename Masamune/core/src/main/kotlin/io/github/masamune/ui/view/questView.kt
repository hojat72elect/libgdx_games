package io.github.masamune.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.QuestModel
import io.github.masamune.ui.model.QuestViewModel
import io.github.masamune.ui.widget.OptionTable
import io.github.masamune.ui.widget.QuestInfoTable
import io.github.masamune.ui.widget.QuestTable
import io.github.masamune.ui.widget.optionTable
import io.github.masamune.ui.widget.questInfoTable
import io.github.masamune.ui.widget.questTable
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.table

private enum class UiQuestViewState {
    SELECT_OPTION, SELECT_QUEST
}

class QuestView(
    skin: Skin,
    model: QuestViewModel,
) : View<QuestViewModel>(skin, model), KTable {

    private val optionTable: OptionTable
    private val questSpTable: Table
    private val questTable: QuestTable
    private val questInfoTable: QuestInfoTable
    private var state = UiQuestViewState.SELECT_OPTION

    private var currentQuests: List<QuestModel> = emptyList()

    init {
        setFillParent(true)
        background = skin.getDrawable("dialog_frame")

        // left side -> quest info
        questInfoTable = questInfoTable(skin) {
            it.grow().pad(10f, 10f, 10f, 70f)
        }

        table(skin) { rightTableCell ->
            // top right -> quest view options
            table(skin) { tblCell ->
                background = skin.getDrawable("dialog_frame")

                this@QuestView.optionTable = optionTable(skin) { optionTableCell ->
                    option(this@QuestView.i18nTxt(I18NKey.MENU_OPTION_QUESTS_ACTIVE))
                    option(this@QuestView.i18nTxt(I18NKey.MENU_OPTION_QUESTS_COMPLETED))
                    option(this@QuestView.i18nTxt(I18NKey.MENU_OPTION_QUIT))

                    optionTableCell.fill().align(Align.left)
                }

                tblCell.top().right().padRight(10f).padTop(10f).row()
            }

            // bottom right -> scrollable quest list
            this@QuestView.questSpTable = table(skin) { tblCell ->
                background = skin.getDrawable("dialog_frame")

                this@QuestView.questTable = questTable(skin, this@QuestView::onQuestSelected) {
                    it.grow()
                }

                tblCell.growY().bottom().right().minWidth(280f).pad(40f, 0f, 10f, 10f)
            }

            rightTableCell.growY()
        }

        showQuestTables(false)
        registerOnPropertyChanges()
    }

    override fun registerOnPropertyChanges() {
        viewModel.onPropertyChange(QuestViewModel::activeQuests) { quests ->
            optionTable.firstOption()
            state = UiQuestViewState.SELECT_OPTION
            isVisible = quests.isNotEmpty()
            showQuestTables(false)
        }
    }

    private fun onQuestSelected(questIdx: Int) {
        val quest = currentQuests[questIdx]
        if (quest.name === i18nTxt(I18NKey.QUEST_NONE_NAME)) {
            questInfoTable.clearQuest()
        } else {
            questInfoTable.quest(quest.name, quest.description)
        }
    }

    private fun showQuestTables(show: Boolean) {
        questSpTable.isVisible = show
        questInfoTable.isVisible = show
        if (!show) {
            questInfoTable.clearQuest()
        }
    }

    private fun updateQuests(quests: List<QuestModel>) {
        state = UiQuestViewState.SELECT_QUEST
        viewModel.playSndMenuAccept()
        optionTable.stopSelectAnimation()
        currentQuests = quests

        questTable.clearEntries()
        quests.forEach { questModel ->
            questTable.quest(questModel.name)
        }
        questTable.selectFirstEntry()

        showQuestTables(true)
        onQuestSelected(0)
    }

    override fun onUpPressed() {
        when (state) {
            UiQuestViewState.SELECT_OPTION -> {
                if (optionTable.prevOption()) {
                    viewModel.playSndMenuClick()
                }
            }

            UiQuestViewState.SELECT_QUEST -> {
                if (questTable.prevEntry()) {
                    viewModel.playSndMenuClick()
                }
            }
        }
    }

    override fun onDownPressed() {
        when (state) {
            UiQuestViewState.SELECT_OPTION -> {
                if (optionTable.nextOption()) {
                    viewModel.playSndMenuClick()
                }
            }

            UiQuestViewState.SELECT_QUEST -> {
                if (questTable.nextEntry()) {
                    viewModel.playSndMenuClick()
                }
            }
        }
    }

    override fun onBackPressed() {
        when (state) {
            UiQuestViewState.SELECT_OPTION -> {
                if (optionTable.lastOption()) {
                    viewModel.playSndMenuClick()
                }
            }

            UiQuestViewState.SELECT_QUEST -> {
                state = UiQuestViewState.SELECT_OPTION
                viewModel.playSndMenuAbort()
                showQuestTables(false)
                optionTable.resumeSelectAnimation()
            }
        }
    }

    override fun onSelectPressed() {
        when (state) {
            UiQuestViewState.SELECT_OPTION -> {
                when (optionTable.selectedOption) {
                    0 -> updateQuests(viewModel.activeQuests)
                    1 -> updateQuests(viewModel.completedQuests)
                    2 -> viewModel.quit()
                }
            }

            UiQuestViewState.SELECT_QUEST -> Unit
        }
    }
}

@Scene2dDsl
fun <S> KWidget<S>.questView(
    model: QuestViewModel,
    skin: Skin,
    init: (@Scene2dDsl QuestView).(S) -> Unit = {},
): QuestView = actor(QuestView(skin, model), init)
