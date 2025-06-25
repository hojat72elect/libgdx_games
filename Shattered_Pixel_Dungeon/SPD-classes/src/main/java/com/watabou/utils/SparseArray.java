

package com.watabou.utils;

import com.badlogic.gdx.utils.IntMap;

import java.util.Arrays;
import java.util.List;

public class SparseArray<T> extends IntMap<T> {

    @Override
    public synchronized T put(int key, T value) {
        return super.put(key, value);
    }

    @Override
    public synchronized T get(int key, T defaultValue) {
        return super.get(key, defaultValue);
    }

    @Override
    public synchronized T remove(int key) {
        return super.remove(key);
    }

    public synchronized int[] keyArray() {
        return keys().toArray().toArray();
    }

    public synchronized List<T> valueList() {
        return Arrays.asList(values().toArray().toArray());
    }
}
