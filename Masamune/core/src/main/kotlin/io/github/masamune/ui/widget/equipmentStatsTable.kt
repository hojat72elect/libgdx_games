package io.github.masamune.ui.widget

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import io.github.masamune.tiledmap.ItemCategory
import io.github.masamune.ui.model.UIStats
import io.github.masamune.ui.view.zeroIfMissing
import ktx.actors.txt
import ktx.app.gdxError
import ktx.scene2d.KTable
import ktx.scene2d.KTableWidget
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.defaultStyle
import ktx.scene2d.label
import ktx.scene2d.table

class EquipmentStatsTable(
    skin: Skin,
    title: String,
    equipmentNames: Map<ItemCategory, String>,
    statsNames: Map<UIStats, String>,
) : KTable, Table(skin) {

    private val statsLabels: Map<UIStats, ShopStatsLabel>
    private val equipmentTable: EquipmentTable
    private val titleLabel: Label
    private val playerNameLabel: Label

    init {
        align(Align.topLeft)

        table(skin) {
            // header: title + character faces + names
            this@EquipmentStatsTable.titleLabel = label(title, "dialog_image_caption", skin) {
                setAlignment(Align.center)
                it.padLeft(10f).padTop(10f).align(Align.topLeft).row()
            }

            // characters and name
            table(skin) { cell ->
                frameImage(skin, "dialog_face_frame", "hero") {
                    it.left().fillX()
                }
                this@EquipmentStatsTable.playerNameLabel = label("", defaultStyle, skin) {
                    this.color = skin.getColor("dark_grey")
                    it.padLeft(15f).fillX().padRight(25f).left()
                }
                cell.padLeft(15f).padBottom(15f).top().left().row()
            }

            // stats table
            table(skin) { cell ->
                val normalColor = skin.getColor("dark_grey")
                val greenColor = skin.getColor("green")
                val blueColor = skin.getColor("blue")
                this@EquipmentStatsTable.statsLabels = mapOf(
                    UIStats.LIFE to statsRow(skin, statsNames.zeroIfMissing(UIStats.LIFE), greenColor, 0f),
                    UIStats.MANA to statsRow(skin, statsNames.zeroIfMissing(UIStats.MANA), blueColor, 0f),
                    UIStats.STRENGTH to statsRow(skin, statsNames.zeroIfMissing(UIStats.STRENGTH), normalColor),
                    UIStats.AGILITY to statsRow(skin, statsNames.zeroIfMissing(UIStats.AGILITY), normalColor),
                    UIStats.CONSTITUTION to statsRow(skin, statsNames.zeroIfMissing(UIStats.CONSTITUTION), normalColor),
                    UIStats.INTELLIGENCE to statsRow(skin, statsNames.zeroIfMissing(UIStats.INTELLIGENCE), normalColor),
                    UIStats.DAMAGE to statsRow(skin, statsNames.zeroIfMissing(UIStats.DAMAGE), normalColor),
                    UIStats.ARMOR to statsRow(skin, statsNames.zeroIfMissing(UIStats.ARMOR), normalColor),
                    UIStats.RESISTANCE to statsRow(skin, statsNames.zeroIfMissing(UIStats.RESISTANCE), normalColor),
                )

                cell.align(Align.topLeft).padLeft(10f).row()
            }
        }

        // optional equipment table
        equipmentTable = equipmentTable(skin) { cell ->
            item(ItemCategory.WEAPON, equipmentNames[ItemCategory.WEAPON] ?: "")
            item(ItemCategory.ARMOR, equipmentNames[ItemCategory.ARMOR] ?: "")
            item(ItemCategory.HELMET, equipmentNames[ItemCategory.HELMET] ?: "")
            item(ItemCategory.BOOTS, equipmentNames[ItemCategory.BOOTS] ?: "")
            item(ItemCategory.ACCESSORY, equipmentNames[ItemCategory.ACCESSORY] ?: "")

            this.isVisible = false
            cell.align(Align.bottomLeft).padBottom(80f).growX()
        }
    }

    fun statsValue(stat: UIStats, value: String) {
        val statsLabel = statsLabels[stat] ?: gdxError("No StatsLabel for $stat")
        statsLabel.valueTxt(value)
    }

    fun clearDiff() {
        statsLabels.values.forEach { it.diffTxt(0) }
    }

    fun diffValue(stat: UIStats, value: Int) {
        val statsLabel = statsLabels[stat] ?: gdxError("No StatsLabel for $stat")
        statsLabel.diffTxt(value)
    }

    fun equipmentName(category: ItemCategory, name: String) {
        equipmentTable.itemName(category, name)
    }

    fun title(value: String) {
        titleLabel.txt = value
    }

    fun playerName(name: String) {
        playerNameLabel.txt = name
    }

    fun showEquipment(show: Boolean) {
        equipmentTable.isVisible = show
    }

    fun selectEquipment(category: ItemCategory) {
        equipmentTable.unselectAll()
        equipmentTable.selectEntry { it.category == category }
    }

    fun prevEquipment(): Boolean = equipmentTable.prevEntry()

    fun nextEquipment(): Boolean = equipmentTable.nextEntry()

    fun selectedCategory(): ItemCategory = equipmentTable.selectedEntry.category

    fun stopEquipmentSelectAnimation() {
        equipmentTable.selectedEntry.stopSelectAnimation()
    }

    fun resumeEquipmentSelectAnimation() {
        equipmentTable.selectedEntry.resumeSelectAnimation()
    }

    fun clearEquipment() {
        equipmentTable.forEach { it.item("") }
    }

    companion object {
        private fun KTableWidget.statsRow(
            skin: Skin,
            name: String,
            color: Color,
            minDiffWidth: Float = 40f,
        ): ShopStatsLabel {
            label(name, defaultStyle, skin) {
                this.color = skin.getColor("dark_grey")
                setAlignment(Align.right)
                it.fillX()
            }
            return shopStatsLabel(skin, "", color, minDiffWidth) {
                it.padLeft(20f).align(Align.left).row()
            }
        }
    }
}


@Scene2dDsl
fun <S> KWidget<S>.equipmentStatsTable(
    skin: Skin,
    title: String,
    equipmentNames: Map<ItemCategory, String>,
    statsNames: Map<UIStats, String>,
    init: (@Scene2dDsl EquipmentStatsTable).(S) -> Unit = {},
): EquipmentStatsTable = actor(EquipmentStatsTable(skin, title, equipmentNames, statsNames), init)
