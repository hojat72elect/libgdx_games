package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.KGroup
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.scene2d

@Scene2dDsl
class MagicTable(skin: Skin) : ScrollableSelectionTable<MagicEntryWidget>(
    skin,
    entriesPerRow = 2,
    firstEntryPredicate = { it.canPerform },
), KGroup {

    fun magic(title: String, targetDescriptor: String, mana: Int, canPerform: Boolean) {
        val magicEntry = scene2d.magicEntry(title, targetDescriptor, mana, canPerform, skin) {
            select(false)
        }
        addEntry(magicEntry)
    }

    override fun prevEntryId(): Int {
        if (hasNoEntries()) {
            return selectedEntryIdx
        }

        for (i in selectedEntryIdx - 1 downTo 0) {
            val entry = this[i]
            if (entry.canPerform) {
                return i
            }
        }
        return selectedEntryIdx
    }

    override fun nextEntryId(): Int {
        if (hasNoEntries()) {
            return selectedEntryIdx
        }

        for (i in selectedEntryIdx + 1 until numEntries) {
            val entry = this[i]
            if (entry.canPerform) {
                return i
            }
        }
        return selectedEntryIdx
    }

    override fun hasNoEntries(): Boolean = count { it.canPerform } == 0

    override fun hasEntries(): Boolean = count { it.canPerform } > 0

}

@Scene2dDsl
fun <S> KWidget<S>.magicTable(
    skin: Skin,
    init: (@Scene2dDsl MagicTable).(S) -> Unit = {},
): MagicTable = actor(MagicTable(skin), init)
