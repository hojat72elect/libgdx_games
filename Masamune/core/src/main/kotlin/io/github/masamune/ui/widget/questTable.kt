package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.KGroup
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.scene2d

@Scene2dDsl
class QuestTable(
    skin: Skin,
    onEntrySelected: ((entryIdx: Int) -> Unit)? = null,
) : ScrollableSelectionTable<SelectableTextWidget>(
    skin,
    entriesPerRow = 1,
    onEntrySelected = onEntrySelected,
), KGroup {

    fun quest(name: String): SelectableTextWidget {
        val invItem = scene2d.selectableText(name, skin) {
            select(this@QuestTable.numEntries == 0)
        }
        addEntry(invItem)
        return invItem
    }

}

@Scene2dDsl
fun <S> KWidget<S>.questTable(
    skin: Skin,
    onEntrySelected: ((entryIdx: Int) -> Unit)? = null,
    init: (@Scene2dDsl QuestTable).(S) -> Unit = {},
): QuestTable = actor(QuestTable(skin, onEntrySelected), init)
