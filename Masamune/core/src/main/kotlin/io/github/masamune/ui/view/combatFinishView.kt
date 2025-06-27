package io.github.masamune.ui.view

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import io.github.masamune.ui.model.CombatFinishViewModel
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.UIStats
import io.github.masamune.ui.model.UiCombatFinishState
import io.github.masamune.ui.widget.OptionTable
import io.github.masamune.ui.widget.optionTable
import ktx.actors.plusAssign
import ktx.actors.then
import ktx.actors.txt
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.defaultStyle
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

@Scene2dDsl
class CombatFinishView(
    viewModel: CombatFinishViewModel,
    skin: Skin,
) : View<CombatFinishViewModel>(skin, viewModel), KTable {

    private val xpLabel: Label
    private val talonsLabel: Label
    private val levelUpLabel: TypingLabel
    private val lvlUpStatsLabel: Label
    private val combatSummary: Label
    private val optionTable: OptionTable
    private val centeredTable: Table

    init {
        setFillParent(true)

        // nested inner table to center the content of the dialog automatically
        centeredTable = table(skin) {
            background = skin.getDrawable("dialog_frame")

            this@CombatFinishView.xpLabel = scene2d.label("", defaultStyle, skin) {
                setAlignment(Align.topLeft)
                color = skin.getColor("dark_grey")
            }
            this@CombatFinishView.talonsLabel = scene2d.label("", defaultStyle, skin) {
                setAlignment(Align.topLeft)
                color = skin.getColor("dark_grey")
            }
            this@CombatFinishView.levelUpLabel = scene2d.typingLabel("", defaultStyle, skin) {
                setAlignment(Align.topLeft)
            }
            this@CombatFinishView.lvlUpStatsLabel = scene2d.label("", defaultStyle, skin) {
                setAlignment(Align.topLeft)
                color = skin.getColor("white")
            }
            this@CombatFinishView.combatSummary = scene2d.label("", defaultStyle, skin) {
                setAlignment(Align.topLeft)
                color = skin.getColor("dark_grey")
            }
            this@CombatFinishView.optionTable = scene2d.optionTable(skin)
        }

        registerOnPropertyChanges()
    }

    override fun registerOnPropertyChanges() {
        viewModel.onPropertyChange(CombatFinishViewModel::xpToGain) { xp ->
            xpLabel.txt = buildString {
                append(i18nTxt(I18NKey.COMBAT_TOTAL_XP))
                append(": ")
                append(xp)
            }
        }
        viewModel.onPropertyChange(CombatFinishViewModel::talonsToGain) { talons ->
            talonsLabel.txt = buildString {
                append(i18nTxt(I18NKey.COMBAT_TOTAL_TALONS))
                append(": ")
                append(talons)
            }
        }
        viewModel.onPropertyChange(CombatFinishViewModel::levelsToGain) { lvl ->
            levelUpLabel.isVisible = lvl > 0
            lvlUpStatsLabel.isVisible = lvl > 0
            levelUpLabel.txt = buildString {
                append("{RAINBOW}")
                append(i18nTxt(I18NKey.COMBAT_LEVEL_UPS))
                append(": ")
                append(lvl)
            }
        }
        viewModel.onPropertyChange(CombatFinishViewModel::statsToGain) { stats ->
            val supportedStats = listOf(
                UIStats.STRENGTH to I18NKey.STATS_STRENGTH,
                UIStats.INTELLIGENCE to I18NKey.STATS_INTELLIGENCE,
                UIStats.MANA_MAX to I18NKey.STATS_MANA_MAX,
                UIStats.CONSTITUTION to I18NKey.STATS_CONSTITUTION,
                UIStats.AGILITY to I18NKey.STATS_AGILITY,
            )

            lvlUpStatsLabel.txt = buildString {
                val defaultColor = skin.getColor("dark_grey")

                append("[#${defaultColor}]")
                append(i18nTxt(I18NKey.COMBAT_LEVEL_UP_STATS))
                append(": ")
                append("[]")
                appendLine()

                supportedStats.forEach { (uiStat, i18nKey) ->
                    val gain: Int = stats.getOrDefault(uiStat, 0)
                    if (gain == 0) {
                        return@forEach
                    }

                    if (uiStat == UIStats.MANA_MAX) {
                        append("[#${skin.getColor("blue")}]")
                        append(i18nTxt(i18nKey))
                        append("[]")
                    } else {
                        append("[#${defaultColor}]")
                        append(i18nTxt(i18nKey))
                        append("[]")
                    }
                    append("[#${defaultColor}]")
                    append(": ")
                    append(gain)
                    append("[]")
                    appendLine()
                }
            }
        }
        viewModel.onPropertyChange(CombatFinishViewModel::combatSummary) { combatSummary ->
            this.combatSummary.txt = buildString {
                append(i18nTxt(I18NKey.COMBAT_DEFEATED_ENEMIES))
                appendLine(":")
                append(combatSummary.map { "${it.key}: ${it.value}x" }.joinToString("\n"))
            }
        }
        viewModel.onPropertyChange(CombatFinishViewModel::state) { state ->
            if (state == UiCombatFinishState.UNDEFINED) {
                return@onPropertyChange
            }

            optionTable.clearOptions()
            centeredTable.clearChildren()
            if (state == UiCombatFinishState.DEFEAT) {
                centeredTable.add(combatSummary).grow().padBottom(30f).padTop(10f).row()
                centeredTable.add(optionTable).fill().align(Align.left)

                combatSummary.txt = i18nTxt(I18NKey.COMBAT_DEFEAT)
                optionTable.option(i18nTxt(I18NKey.GENERAL_YES))
                optionTable.option(i18nTxt(I18NKey.GENERAL_NO))
            } else {
                centeredTable.add(xpLabel).grow().padBottom(5f).row()
                centeredTable.add(talonsLabel).grow().padBottom(5f).row()
                if (levelUpLabel.isVisible) {
                    centeredTable.add(levelUpLabel).grow().padTop(20f).padBottom(10f).row()
                    centeredTable.add(lvlUpStatsLabel).grow().row()
                }
                centeredTable.add(combatSummary).grow().padBottom(20f).padTop(5f).row()
                centeredTable.add(optionTable).fill().align(Align.left)

                optionTable.option(i18nTxt(I18NKey.DIALOG_OPTION_OK))
            }

            isVisible = true
            this.clearActions()
            this += Actions.alpha(0.25f) then Actions.fadeIn(0.5f, Interpolation.bounceIn)
        }
    }

    override fun onSelectPressed() {
        viewModel.playSndMenuAccept()
        if (optionTable.children.size == 1) {
            // victory summary -> end combat
            isVisible = false
            viewModel.quitCombat()
            return
        }

        // defeat options
        if (optionTable.selectedOption == 0) {
            viewModel.restartCombat()
        } else {
            viewModel.quitCombat()
        }
        isVisible = false
    }

    override fun onDownPressed() {
        optionTable.nextOption()
        viewModel.playSndMenuClick()
    }

    override fun onUpPressed() {
        optionTable.prevOption()
        viewModel.playSndMenuClick()
    }
}

@Scene2dDsl
fun <S> KWidget<S>.combatFinishView(
    model: CombatFinishViewModel,
    skin: Skin,
    init: (@Scene2dDsl CombatFinishView).(S) -> Unit = {},
): CombatFinishView = actor(CombatFinishView(model, skin), init)
