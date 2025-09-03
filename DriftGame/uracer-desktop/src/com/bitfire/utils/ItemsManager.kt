package com.bitfire.utils

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable

class ItemsManager<T : Disposable> : Iterable<T>, Disposable {
    private val owned = Array<Boolean>()
    private val items = Array<T>()

    override fun dispose() {
        for (i in 0..<items.size) {
            if (owned.get(i)) {
                items.get(i).dispose()
            }
        }

        items.clear()
        owned.clear()
    }

    /**
     * Add an item to the manager, if own is true the manager will manage the resource's lifecycle; otherwise, will transfer ownership to it.
     */
    @JvmOverloads
    fun add(item: T, own: Boolean = true) {
        items.add(item)
        owned.add(own)
    }

    /**
     * Returns the item at the specified index.
     */
    fun get(index: Int): T = items.get(index)

    /**
     * Returns the number of items managed by this instance
     */
    fun count() = items.size

    /**
     * Returns an iterator on the managed items.
     */
    override fun iterator(): MutableIterator<T> = items.iterator()

    /**
     * Removes a previously added resource
     */
    fun remove(item: T) {
        val index = items.indexOf(item, true)
        if (index == ITEM_NOT_FOUND) {
            return
        }

        if (owned.get(index)) items.get(index).dispose()
        items.removeIndex(index)
        owned.removeIndex(index)
        items.removeValue(item, true)
    }

    companion object {
        private const val ITEM_NOT_FOUND = -1
    }
}
