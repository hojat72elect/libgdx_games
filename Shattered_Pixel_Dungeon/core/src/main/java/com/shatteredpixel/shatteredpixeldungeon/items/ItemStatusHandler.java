

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class ItemStatusHandler<T extends Item> {

    private final Class<? extends T>[] items;
    private final LinkedHashMap<Class<? extends T>, String> itemLabels;
    private final LinkedHashMap<String, Integer> labelImages;
    private final LinkedHashSet<Class<? extends T>> known;

    public ItemStatusHandler(Class<? extends T>[] items, HashMap<String, Integer> labelImages) {

        this.items = items;

        this.itemLabels = new LinkedHashMap<>();
        this.labelImages = new LinkedHashMap<>(labelImages);
        known = new LinkedHashSet<>();

        ArrayList<String> labelsLeft = new ArrayList<>(labelImages.keySet());

        for (int i = 0; i < items.length; i++) {

            Class<? extends T> item = items[i];

            int index = Random.Int(labelsLeft.size());

            itemLabels.put(item, labelsLeft.get(index));
            labelsLeft.remove(index);
        }
    }

    public ItemStatusHandler(Class<? extends T>[] items, HashMap<String, Integer> labelImages, Bundle bundle) {

        this.items = items;

        this.itemLabels = new LinkedHashMap<>();
        this.labelImages = new LinkedHashMap<>(labelImages);
        known = new LinkedHashSet<>();

        ArrayList<String> allLabels = new ArrayList<>(labelImages.keySet());

        restore(bundle, allLabels);
    }

    private static final String PFX_LABEL = "_label";
    private static final String PFX_KNOWN = "_known";

    public void save(Bundle bundle) {
        for (int i = 0; i < items.length; i++) {
            String itemName = items[i].getSimpleName();
            bundle.put(itemName + PFX_LABEL, itemLabels.get(items[i]));
            bundle.put(itemName + PFX_KNOWN, known.contains(items[i]));
        }
    }

    public void saveSelectively(Bundle bundle, ArrayList<Item> itemsToSave) {
        List<Class<? extends T>> items = Arrays.asList(this.items);
        for (Item item : itemsToSave) {
            if (items.contains(item.getClass())) {
                Class<? extends T> cls = items.get(items.indexOf(item.getClass()));
                String itemName = cls.getSimpleName();
                bundle.put(itemName + PFX_LABEL, itemLabels.get(cls));
                bundle.put(itemName + PFX_KNOWN, known.contains(cls));
            }
        }
    }

    public void saveClassesSelectively(Bundle bundle, ArrayList<Class<? extends Item>> clsToSave) {
        List<Class<? extends T>> items = Arrays.asList(this.items);
        for (Class<? extends Item> cls : clsToSave) {
            if (items.contains(cls)) {
                Class<? extends T> toSave = items.get(items.indexOf(cls));
                String itemName = toSave.getSimpleName();
                bundle.put(itemName + PFX_LABEL, itemLabels.get(toSave));
                bundle.put(itemName + PFX_KNOWN, known.contains(toSave));
            }
        }
    }

    private void restore(Bundle bundle, ArrayList<String> labelsLeft) {

        ArrayList<Class<? extends T>> unlabelled = new ArrayList<>();

        for (int i = 0; i < items.length; i++) {

            Class<? extends T> item = items[i];
            String itemName = item.getSimpleName();

            if (bundle.contains(itemName + PFX_LABEL)) {

                String label = bundle.getString(itemName + PFX_LABEL);
                itemLabels.put(item, label);
                labelsLeft.remove(label);

                if (bundle.getBoolean(itemName + PFX_KNOWN)) {
                    known.add(item);
                }
            } else {

                unlabelled.add(items[i]);
            }
        }

        for (Class<? extends T> item : unlabelled) {

            String itemName = item.getSimpleName();

            int index = Random.Int(labelsLeft.size());

            itemLabels.put(item, labelsLeft.get(index));
            labelsLeft.remove(index);

            if (bundle.contains(itemName + PFX_KNOWN) && bundle.getBoolean(itemName + PFX_KNOWN)) {
                known.add(item);
            }
        }
    }

    public boolean contains(T item) {
        for (Class<? extends Item> i : items) {
            if (item.getClass().equals(i)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Class<? extends T> itemCls) {
        for (Class<? extends Item> i : items) {
            if (itemCls.equals(i)) {
                return true;
            }
        }
        return false;
    }

    public int image(T item) {
        return labelImages.get(label(item));
    }

    public int image(Class<? extends T> itemCls) {
        return labelImages.get(label(itemCls));
    }

    public String label(T item) {
        return itemLabels.get(item.getClass());
    }

    public String label(Class<? extends T> itemCls) {
        return itemLabels.get(itemCls);
    }

    public boolean isKnown(T item) {
        return known.contains(item.getClass());
    }

    public boolean isKnown(Class<? extends T> itemCls) {
        return known.contains(itemCls);
    }

    public void know(T item) {
        known.add((Class<? extends T>) item.getClass());
    }

    public void know(Class<? extends T> itemCls) {
        known.add(itemCls);
    }

    public HashSet<Class<? extends T>> known() {
        return known;
    }

    public HashSet<Class<? extends T>> unknown() {
        LinkedHashSet<Class<? extends T>> result = new LinkedHashSet<>();
        for (Class<? extends T> i : items) {
            if (!known.contains(i)) {
                result.add(i);
            }
        }
        return result;
    }
}
