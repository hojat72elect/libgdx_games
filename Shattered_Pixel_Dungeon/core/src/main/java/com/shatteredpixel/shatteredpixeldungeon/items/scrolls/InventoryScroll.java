

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

public abstract class InventoryScroll extends Scroll {

    protected static boolean identifiedByUse = false;

    @Override
    public void doRead() {

        if (!isKnown()) {
            identify();
            curItem = detach(curUser.belongings.backpack);
            identifiedByUse = true;
        } else {
            identifiedByUse = false;
        }

        GameScene.selectItem(itemSelector);
    }

    private void confirmCancelation() {
        GameScene.show(new WndOptions(new ItemSprite(this),
                Messages.titleCase(name()),
                Messages.get(this, "warning"),
                Messages.get(this, "yes"),
                Messages.get(this, "no")) {
            @Override
            protected void onSelect(int index) {
                switch (index) {
                    case 0:
                        curUser.spendAndNext(TIME_TO_READ);
                        identifiedByUse = false;
                        break;
                    case 1:
                        GameScene.selectItem(itemSelector);
                        break;
                }
            }

            public void onBackPressed() {
            }
        });
    }

    private String inventoryTitle() {
        return Messages.get(this, "inv_title");
    }

    protected Class<? extends Bag> preferredBag = null;

    protected boolean usableOnItem(Item item) {
        return true;
    }

    protected abstract void onItemSelected(Item item);

    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return inventoryTitle();
        }

        @Override
        public Class<? extends Bag> preferredBag() {
            return preferredBag;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return usableOnItem(item);
        }

        @Override
        public void onSelect(Item item) {

            //FIXME this safety check shouldn't be necessary
            //it would be better to eliminate the curItem static variable.
            if (!(curItem instanceof InventoryScroll)) {
                return;
            }

            if (item != null) {

                //SoU opens a separate window that can be cancelled
                //so we don't do a lot of logic here
                if (!identifiedByUse && !(curItem instanceof ScrollOfUpgrade)) {
                    curItem = detach(curUser.belongings.backpack);
                }
                ((InventoryScroll) curItem).onItemSelected(item);

                if (!(curItem instanceof ScrollOfUpgrade)) {
                    ((InventoryScroll) curItem).readAnimation();
                    Sample.INSTANCE.play(Assets.Sounds.READ);
                }
            } else if (identifiedByUse && !((Scroll) curItem).anonymous) {

                ((InventoryScroll) curItem).confirmCancelation();
            } else if (((Scroll) curItem).anonymous) {

                curUser.spendAndNext(TIME_TO_READ);
            }
        }
    };
}
