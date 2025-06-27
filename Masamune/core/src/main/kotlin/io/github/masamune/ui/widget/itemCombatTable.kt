package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.KGroup
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.scene2d

@Scene2dDsl
class ItemCombatTable(skin: Skin) : ScrollableSelectionTable<CombatItemWidget>(
    skin,
    entriesPerRow = 2,
), KGroup {
    fun item(title: String, targetDescriptor: String, amount: Int) {
        val itemEntry = scene2d.combatItem(title, targetDescriptor, amount, skin) {
            select(this@ItemCombatTable.numEntries == 0)
        }
        addEntry(itemEntry)
    }
}

@Scene2dDsl
fun <S> KWidget<S>.itemCombatTable(
    skin: Skin,
    init: (@Scene2dDsl ItemCombatTable).(S) -> Unit = {},
): ItemCombatTable = actor(ItemCombatTable(skin), init)
