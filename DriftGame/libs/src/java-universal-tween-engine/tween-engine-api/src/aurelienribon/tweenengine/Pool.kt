package aurelienribon.tweenengine

/**
 * A light-weight pool of objects that can be reused to avoid allocation. Based on Nathan Sweet pool implementation.
 */
abstract class Pool<T>(initCapacity: Int, private val callback: Callback<T>?) {
    private val objects = ArrayList<T>(initCapacity)

    protected abstract fun create(): T

    fun get(): T {
        val obj = if (objects.isEmpty()) create() else objects.removeAt(objects.size - 1)
        callback?.onUnPool(obj)
        return obj
    }

    fun free(obj: T) {
        if (objects.contains(obj).not()) {
            callback?.onPool(obj)
            objects.add(obj)
        }
    }

    fun clear() {
        objects.clear()
    }

    fun size() = objects.size

    interface Callback<T> {
        fun onPool(obj: T)

        fun onUnPool(obj: T)
    }
}
