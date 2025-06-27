package me.vrekt.oasis.save.inventory;

import com.google.gson.annotations.Expose;

import me.vrekt.oasis.item.Item;
import me.vrekt.oasis.item.Items;

/**
 * Singular item
 * TODO: isStackable?
 */
public final class ItemSave {

    @Expose
    private final int slot;

    @Expose
    private final String name;

    @Expose
    private final Items type;

    @Expose
    private final int amount;

    public ItemSave(int slot, Item item) {
        this.slot = slot;
        this.name = item.name();
        this.type = item.type();
        this.amount = item.amount();
    }

    /**
     * @return slot
     */
    public int slot() {
        return slot;
    }

    /**
     * NOTE: Not currently required or used.
     *
     * @return name
     */
    public String name() {
        return name;
    }

    /**
     * @return type
     */
    public Items type() {
        return type;
    }

    /**
     * @return amount
     */
    public int amount() {
        return amount;
    }
}
