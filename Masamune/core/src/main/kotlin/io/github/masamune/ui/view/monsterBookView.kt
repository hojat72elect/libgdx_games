package io.github.masamune.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.MonsterBookViewModel
import io.github.masamune.ui.model.MonsterModel
import io.github.masamune.ui.model.UIStats
import io.github.masamune.ui.widget.MonsterInfoTable
import io.github.masamune.ui.widget.OptionTable
import io.github.masamune.ui.widget.QuestTable
import io.github.masamune.ui.widget.monsterInfoTable
import io.github.masamune.ui.widget.optionTable
import io.github.masamune.ui.widget.questTable
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.table

private enum class UiMonsterBookViewState {
    SELECT_OPTION, SELECT_MONSTER
}

class MonsterBookView(
    skin: Skin,
    model: MonsterBookViewModel,
) : View<MonsterBookViewModel>(skin, model), KTable {

    private val optionTable: OptionTable
    private val monsterSpTable: Table
    private val monsterTable: QuestTable
    private val monsterInfoTable: MonsterInfoTable
    private var state = UiMonsterBookViewState.SELECT_OPTION

    private var monsterModels: List<MonsterModel> = emptyList()

    init {
        setFillParent(true)
        background = skin.getDrawable("dialog_frame")

        // left side -> monster info
        val uiStatsLabels = mapOf(
            UIStats.LIFE to i18nTxt(I18NKey.STATS_LIFE),
            UIStats.MANA to i18nTxt(I18NKey.STATS_MANA),
            UIStats.AGILITY to i18nTxt(I18NKey.STATS_AGILITY),
            UIStats.DAMAGE to i18nTxt(I18NKey.STATS_ATTACK),
            UIStats.ARMOR to i18nTxt(I18NKey.STATS_ARMOR),
            UIStats.RESISTANCE to i18nTxt(I18NKey.STATS_RESISTANCE),
            UIStats.TALONS to i18nTxt(I18NKey.STATS_TALONS),
            UIStats.XP to i18nTxt(I18NKey.STATS_XP),
        )
        monsterInfoTable = monsterInfoTable(skin, uiStatsLabels) {
            it.grow().pad(10f, 10f, 10f, 70f)
        }

        table(skin) { rightTableCell ->
            // top right -> view options
            table(skin) { tblCell ->
                background = skin.getDrawable("dialog_frame")

                this@MonsterBookView.optionTable = optionTable(skin) { optionTableCell ->
                    option(this@MonsterBookView.i18nTxt(I18NKey.MENU_OPTION_MONSTERS))
                    option(this@MonsterBookView.i18nTxt(I18NKey.MENU_OPTION_QUIT))

                    optionTableCell.fill().align(Align.left)
                }

                tblCell.top().right().padRight(10f).padTop(10f).row()
            }

            // bottom right -> scrollable monster type list
            this@MonsterBookView.monsterSpTable = table(skin) { tblCell ->
                background = skin.getDrawable("dialog_frame")

                this@MonsterBookView.monsterTable = questTable(skin, this@MonsterBookView::onMonsterSelected) {
                    it.grow()
                }

                tblCell.growY().bottom().right().minWidth(280f).pad(40f, 0f, 10f, 10f)
            }

            rightTableCell.growY()
        }

        showMonsterTables(false)
        registerOnPropertyChanges()
    }

    override fun registerOnPropertyChanges() {
        viewModel.onPropertyChange(MonsterBookViewModel::monsterModels) { monsters ->
            optionTable.firstOption()
            state = UiMonsterBookViewState.SELECT_OPTION
            isVisible = monsters.isNotEmpty()
            showMonsterTables(false)
        }
    }

    private fun onMonsterSelected(monsterIdx: Int) {
        val monster = monsterModels[monsterIdx]
        if (monster == MonsterModel.UNKNOWN) {
            monsterInfoTable.monster(monster.name, "", null, emptyMap())
            return
        }
        monsterInfoTable.monster(monster.name, monster.description, monster.drawable, monster.stats)
    }

    private fun showMonsterTables(show: Boolean) {
        monsterSpTable.isVisible = show
        monsterInfoTable.isVisible = show
        if (!show) {
            monsterInfoTable.clearMonster()
        }
    }

    private fun updateMonsters(monsters: List<MonsterModel>) {
        state = UiMonsterBookViewState.SELECT_MONSTER
        viewModel.playSndMenuAccept()
        optionTable.stopSelectAnimation()
        monsterModels = monsters

        monsterTable.clearEntries()
        monsters.forEach { monsterModel ->
            monsterTable.quest(monsterModel.name)
        }
        monsterTable.selectFirstEntry()

        showMonsterTables(true)
        onMonsterSelected(0)
    }

    override fun onUpPressed() {
        when (state) {
            UiMonsterBookViewState.SELECT_OPTION -> {
                if (optionTable.prevOption()) {
                    viewModel.playSndMenuClick()
                }
            }

            UiMonsterBookViewState.SELECT_MONSTER -> {
                if (monsterTable.prevEntry()) {
                    viewModel.playSndMenuClick()
                }
            }
        }
    }

    override fun onDownPressed() {
        when (state) {
            UiMonsterBookViewState.SELECT_OPTION -> {
                if (optionTable.nextOption()) {
                    viewModel.playSndMenuClick()
                }
            }

            UiMonsterBookViewState.SELECT_MONSTER -> {
                if (monsterTable.nextEntry()) {
                    viewModel.playSndMenuClick()
                }
            }
        }
    }

    override fun onBackPressed() {
        when (state) {
            UiMonsterBookViewState.SELECT_OPTION -> {
                if (optionTable.lastOption()) {
                    viewModel.playSndMenuClick()
                }
            }

            UiMonsterBookViewState.SELECT_MONSTER -> {
                state = UiMonsterBookViewState.SELECT_OPTION
                viewModel.playSndMenuAbort()
                showMonsterTables(false)
                optionTable.resumeSelectAnimation()
            }
        }
    }

    override fun onSelectPressed() {
        when (state) {
            UiMonsterBookViewState.SELECT_OPTION -> {
                when (optionTable.selectedOption) {
                    0 -> updateMonsters(viewModel.monsterModels)
                    1 -> viewModel.quit()
                }
            }

            UiMonsterBookViewState.SELECT_MONSTER -> Unit
        }
    }
}

@Scene2dDsl
fun <S> KWidget<S>.monsterBookView(
    model: MonsterBookViewModel,
    skin: Skin,
    init: (@Scene2dDsl MonsterBookView).(S) -> Unit = {},
): MonsterBookView = actor(MonsterBookView(skin, model), init)
