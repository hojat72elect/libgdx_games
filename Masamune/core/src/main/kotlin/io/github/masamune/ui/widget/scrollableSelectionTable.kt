package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.app.gdxError
import ktx.scene2d.KGroup
import ktx.scene2d.scene2d
import ktx.scene2d.table

interface SelectableWidget {
    fun select(value: Boolean)
}

abstract class ScrollableSelectionTable<T>(
    val skin: Skin,
    val entriesPerRow: Int,
    private val firstEntryPredicate: ((T) -> Boolean)? = null,
    private val onEntrySelected: ((entryIdx: Int) -> Unit)? = null,
) : ScrollPane(null, skin), KGroup where T : Actor, T : SelectableWidget {

    private val contentTable: Table
    var selectedEntryIdx: Int = 0
        private set
    val numEntries: Int
        get() = contentTable.children.size

    val selectedEntry: T
        get() = this[selectedEntryIdx]

    init {
        fadeScrollBars = false
        this.setFlickScroll(false)
        this.setForceScroll(false, true)
        this.setOverscroll(false, false)
        this.setScrollingDisabled(true, false)

        contentTable = scene2d.table(skin) {
            top().padTop(5f).padRight(10f)
        }
        actor = contentTable
    }

    @Suppress("UNCHECKED_CAST")
    operator fun get(idx: Int): T = contentTable.getChild(idx) as T

    fun forEach(action: (T) -> Unit) {
        for (i in 0 until contentTable.children.size) {
            action(this[i])
        }
    }

    /**
     * Removes all entries of the table.
     */
    fun clearEntries() {
        contentTable.clear()
        selectedEntryIdx = 0
    }

    fun addEntry(entry: T) {
        val cell = contentTable.add(entry)
        cell.growX()
        if (contentTable.children.size % entriesPerRow == 0) {
            cell.row()
        } else {
            cell.padRight(25f)
        }
    }

    open fun prevEntryId(): Int = selectedEntryIdx - 1

    fun prevEntry(step: Int = 1): Boolean {
        var result = false
        repeat(step) {
            val selResult = selectEntry(prevEntryId())
            result = result or selResult
        }
        return result
    }

    open fun nextEntryId(): Int = selectedEntryIdx + 1

    fun nextEntry(step: Int = 1): Boolean {
        var result = false
        repeat(step) {
            val selResult = selectEntry(nextEntryId())
            result = result or selResult
        }
        return result
    }

    fun count(predicate: (T) -> Boolean): Int {
        var result = 0
        for (i in 0 until contentTable.children.size) {
            if (predicate(this[i])) {
                ++result
            }
        }
        return result
    }

    open fun hasNoEntries(): Boolean = !contentTable.hasChildren()

    open fun hasEntries(): Boolean = contentTable.hasChildren()

    fun selectEntry(idx: Int): Boolean {
        if (hasNoEntries()) {
            return false
        }

        val realIdx = when {
            idx < 0 -> 0
            idx >= contentTable.children.size -> contentTable.children.size - 1
            else -> idx
        }

        if (realIdx == selectedEntryIdx) {
            return false
        }

        this[selectedEntryIdx].select(false)
        selectedEntryIdx = realIdx
        this[selectedEntryIdx].select(true)
        onEntrySelected?.invoke(selectedEntryIdx)
        return true
    }

    fun singleEntry(predicate: (T) -> Boolean): T {
        if (hasNoEntries()) {
            gdxError("There are no entries in $this")
        }

        for (i in 0 until contentTable.children.size) {
            if (predicate(this[i])) {
                return this[i]
            }
        }

        gdxError("There is no entry for the given predicate in $this")
    }

    /**
     * Selects the first entry of the table.
     */
    fun selectFirstEntry() {
        if (hasNoEntries()) {
            return
        }

        // unselect all
        for (i in 0 until contentTable.children.size) {
            this[i].select(false)
        }

        // select first entry
        val predicate = firstEntryPredicate
        if (predicate == null) {
            selectedEntryIdx = 0
            this[selectedEntryIdx].select(true)
            return
        }

        // select first entry matching predicate
        for (i in 0 until contentTable.children.size) {
            if (predicate(this[i])) {
                selectedEntryIdx = i
                this[selectedEntryIdx].select(true)
                return
            }
        }
    }
}
