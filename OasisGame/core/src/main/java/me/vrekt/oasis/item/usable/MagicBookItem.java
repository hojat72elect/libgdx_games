package me.vrekt.oasis.item.usable;

import java.util.LinkedList;

import me.vrekt.oasis.entity.player.magic.MagicSpell;
import me.vrekt.oasis.item.Items;

/**
 * Magic book item(s)
 */
public abstract class MagicBookItem extends ItemUsable {

    protected final LinkedList<MagicSpell> spells = new LinkedList<>();

    public MagicBookItem(Items itemType, String key, String name, String description) {
        super(itemType, key, name, description);
    }

    /**
     * @return all spells within this book
     */
    public LinkedList<MagicSpell> spells() {
        return spells;
    }
}
