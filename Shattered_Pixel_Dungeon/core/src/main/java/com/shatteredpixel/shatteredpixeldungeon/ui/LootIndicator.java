

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.input.GameAction;

public class LootIndicator extends Tag {

    private ItemSlot slot;

    private Item lastItem = null;
    private int lastQuantity = 0;

    public LootIndicator() {
        super(0x185898);

        setSize(SIZE, SIZE);

        visible = false;
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        slot = new ItemSlot() {
            protected void onClick() {
                LootIndicator.this.onClick();
                if (Dungeon.hero.ready && Dungeon.hero.handle(Dungeon.hero.pos)) {
                    Dungeon.hero.next();
                }
            }

            @Override
            public GameAction keyAction() {
                return SPDAction.TAG_LOOT;
            }

            @Override
            public GameAction secondaryTooltipAction() {
                return SPDAction.WAIT_OR_PICKUP;
            }
        };
        slot.showExtraInfo(false);
        add(slot);
    }

    @Override
    protected void layout() {
        super.layout();

        if (!flipped) {
            slot.setRect(x, y, SIZE, height);
            slot.setMargins(2, 2, 0, 2);
        } else {
            slot.setRect(x + (width() - SIZE), y, SIZE, height);
            slot.setMargins(0, 2, 2, 2);
        }
    }

    @Override
    public void update() {

        if (Dungeon.hero.ready) {
            Heap heap = Dungeon.level.heaps.get(Dungeon.hero.pos);
            if (heap != null) {

                Item item =
                        heap.type == Heap.Type.CHEST ? ItemSlot.CHEST :
                                heap.type == Heap.Type.LOCKED_CHEST ? ItemSlot.LOCKED_CHEST :
                                        heap.type == Heap.Type.CRYSTAL_CHEST ? ItemSlot.CRYSTAL_CHEST :
                                                heap.type == Heap.Type.TOMB ? ItemSlot.TOMB :
                                                        heap.type == Heap.Type.SKELETON ? ItemSlot.SKELETON :
                                                                heap.type == Heap.Type.REMAINS ? ItemSlot.REMAINS :
                                                                        heap.peek();
                if (item != lastItem || item.quantity() != lastQuantity) {
                    lastItem = item;
                    lastQuantity = item.quantity();

                    slot.item(item);
                    flash();
                }
                visible = true;
            } else {

                lastItem = null;
                visible = false;
            }
        }

        slot.enable(visible && Dungeon.hero.ready);

        super.update();
    }
}
