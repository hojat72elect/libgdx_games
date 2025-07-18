

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public abstract class InventorySpell extends Spell {

    @Override
    protected void onCast(Hero hero) {
        GameScene.selectItem(itemSelector);
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
            if (!(curItem instanceof InventorySpell)) {
                return;
            }

            if (item != null) {

                //Infusion opens a separate window that can be cancelled
                //so we don't do a lot of logic here
                if (!(curItem instanceof MagicalInfusion)) {
                    curItem = detach(curUser.belongings.backpack);
                }

                ((InventorySpell) curItem).onItemSelected(item);
                if (!(curItem instanceof MagicalInfusion)) {
                    curUser.spend(1f);
                    curUser.busy();
                    (curUser.sprite).operate(curUser.pos);

                    Sample.INSTANCE.play(Assets.Sounds.READ);
                    Invisibility.dispel();

                    Catalog.countUse(curItem.getClass());
                    if (Random.Float() < ((Spell) curItem).talentChance) {
                        Talent.onScrollUsed(curUser, curUser.pos, ((Spell) curItem).talentFactor, curItem.getClass());
                    }
                }
            }
        }
    };
}
