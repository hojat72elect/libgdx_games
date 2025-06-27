package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.app.gdxError

abstract class SelectionTable<T>(
    skin: Skin,
    val entriesPerRow: Int,
    private val firstEntryPredicate: ((T) -> Boolean)? = null,
) : Table(skin) where T : Actor, T : SelectableWidget {

    var selectedEntryIdx: Int = 0
        private set
    val numEntries: Int
        get() = children.size

    val selectedEntry: T
        get() = this[selectedEntryIdx]

    @Suppress("UNCHECKED_CAST")
    operator fun get(idx: Int): T = getChild(idx) as T

    fun forEach(action: (T) -> Unit) {
        for (i in 0 until children.size) {
            action(this[i])
        }
    }

    fun clearEntries() {
        clear()
        selectedEntryIdx = 0
    }

    fun addEntry(entry: T) {
        val cell = add(entry)
        cell.growX()
        if (children.size % entriesPerRow == 0) {
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
        for (i in 0 until children.size) {
            if (predicate(this[i])) {
                ++result
            }
        }
        return result
    }

    open fun hasNoEntries(): Boolean = !hasChildren()

    open fun hasEntries(): Boolean = hasChildren()

    fun selectEntry(idx: Int): Boolean {
        if (hasNoEntries()) {
            return false
        }

        val realIdx = when {
            idx < 0 -> 0
            idx >= children.size -> children.size - 1
            else -> idx
        }

        if (realIdx == selectedEntryIdx) {
            return false
        }

        this[selectedEntryIdx].select(false)
        selectedEntryIdx = realIdx
        this[selectedEntryIdx].select(true)
        return true
    }

    fun singleEntry(predicate: (T) -> Boolean): T {
        for (i in 0 until children.size) {
            if (predicate(this[i])) {
                return this[i]
            }
        }

        gdxError("There is no entry for the given predicate in $this")
    }

    fun selectFirstEntry() {
        if (hasNoEntries()) {
            return
        }

        // unselect all
        for (i in 0 until children.size) {
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
        for (i in 0 until children.size) {
            if (predicate(this[i])) {
                selectedEntryIdx = i
                this[selectedEntryIdx].select(true)
                return
            }
        }
    }

    fun selectEntry(predicate: (T) -> Boolean) {
        for (i in 0 until children.size) {
            if (predicate(this[i])) {
                selectedEntryIdx = i
                this[selectedEntryIdx].select(true)
                return
            }
        }

        gdxError("There is no entry for the given predicate in $this")
    }

    fun unselectAll() {
        for (i in 0 until children.size) {
            this[i].select(false)
        }
    }
}
