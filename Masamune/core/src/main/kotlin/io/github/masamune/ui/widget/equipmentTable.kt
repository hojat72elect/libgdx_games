package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import io.github.masamune.tiledmap.ItemCategory
import ktx.scene2d.KGroup
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.scene2d

@Scene2dDsl
class EquipmentTable(
    skin: Skin,
) : SelectionTable<EquipmentItemWidget>(
    skin,
    entriesPerRow = 1,
), KGroup {

    private var maxCategoryWidth: Float = 0f

    fun item(category: ItemCategory, categoryName: String) {
        val equipmentItem = scene2d.equipmentItem(category, categoryName, skin) {
            select(this@EquipmentTable.numEntries == 0)
        }
        addEntry(equipmentItem)

        if (equipmentItem.categoryLabelCell.minWidth > maxCategoryWidth) {
            maxCategoryWidth = equipmentItem.categoryLabelCell.minWidth
            forEach { itemWidget ->
                itemWidget.categoryLabelCell.width(maxCategoryWidth)
            }
        }
    }

    fun itemName(category: ItemCategory, itemName: String) {
        singleEntry { it.category == category }.item(itemName)
    }

}

@Scene2dDsl
fun <S> KWidget<S>.equipmentTable(
    skin: Skin,
    init: (@Scene2dDsl EquipmentTable).(S) -> Unit = {},
): EquipmentTable = actor(EquipmentTable(skin), init)
