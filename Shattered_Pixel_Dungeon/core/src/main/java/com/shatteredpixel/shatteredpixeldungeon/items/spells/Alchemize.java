

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndEnergizeItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTradeItem;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Alchemize extends Spell {

    {
        image = ItemSpriteSheet.ALCHEMIZE;

        talentChance = 1 / (float) Recipe.OUT_QUANTITY;
    }

    private static WndBag parentWnd;

    @Override
    protected void onCast(Hero hero) {
        parentWnd = GameScene.selectItem(itemSelector);
    }

    @Override
    public int value() {
        //lower value, as it's very cheap to make (and also sold at shops)
        return (int) (20 * (quantity / (float) Recipe.OUT_QUANTITY));
    }

    @Override
    public int energyVal() {
        return (int) (4 * (quantity / (float) Recipe.OUT_QUANTITY));
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        private static final int OUT_QUANTITY = 8;

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            if (ingredients.size() != 2) return false;

            if (ingredients.get(0) instanceof Plant.Seed && ingredients.get(1) instanceof Runestone) {
                return true;
            }

            return ingredients.get(0) instanceof Runestone && ingredients.get(1) instanceof Plant.Seed;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 2;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            ingredients.get(0).quantity(ingredients.get(0).quantity() - 1);
            ingredients.get(1).quantity(ingredients.get(1).quantity() - 1);
            return sampleOutput(null);
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            return new Alchemize().quantity(OUT_QUANTITY);
        }
    }

    private static final WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
        @Override
        public String textPrompt() {
            return Messages.get(Alchemize.class, "prompt");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return !(item instanceof Alchemize)
                    && (Shopkeeper.canSell(item) || item.energyVal() > 0);
        }

        @Override
        public void onSelect(Item item) {
            if (item != null) {
                if (parentWnd != null) {
                    parentWnd = GameScene.selectItem(itemSelector);
                }
                GameScene.show(new WndAlchemizeItem(item, parentWnd));
            }
        }
    };


    public static class WndAlchemizeItem extends WndInfoItem {

        private static final float GAP = 2;
        private static final int BTN_HEIGHT = 18;

        private final WndBag owner;

        public WndAlchemizeItem(Item item, WndBag owner) {
            super(item);

            this.owner = owner;

            float pos = height;

            if (Shopkeeper.canSell(item)) {
                if (item.quantity() == 1) {

                    RedButton btnSell = new RedButton(Messages.get(this, "sell", item.value())) {
                        @Override
                        protected void onClick() {
                            WndTradeItem.sell(item);
                            hide();
                            consumeAlchemize();
                        }
                    };
                    btnSell.setRect(0, pos + GAP, width, BTN_HEIGHT);
                    btnSell.icon(new ItemSprite(ItemSpriteSheet.GOLD));
                    add(btnSell);

                    pos = btnSell.bottom();
                } else {

                    int priceAll = item.value();
                    RedButton btnSell1 = new RedButton(Messages.get(this, "sell_1", priceAll / item.quantity())) {
                        @Override
                        protected void onClick() {
                            WndTradeItem.sellOne(item);
                            hide();
                            consumeAlchemize();
                        }
                    };
                    btnSell1.setRect(0, pos + GAP, width, BTN_HEIGHT);
                    btnSell1.icon(new ItemSprite(ItemSpriteSheet.GOLD));
                    add(btnSell1);
                    RedButton btnSellAll = new RedButton(Messages.get(this, "sell_all", priceAll)) {
                        @Override
                        protected void onClick() {
                            WndTradeItem.sell(item);
                            hide();
                            consumeAlchemize();
                        }
                    };
                    btnSellAll.setRect(0, btnSell1.bottom() + 1, width, BTN_HEIGHT);
                    btnSellAll.icon(new ItemSprite(ItemSpriteSheet.GOLD));
                    add(btnSellAll);

                    pos = btnSellAll.bottom();
                }
            }

            if (item.energyVal() > 0) {
                if (item.quantity() == 1) {

                    RedButton btnEnergize = new RedButton(Messages.get(this, "energize", item.energyVal())) {
                        @Override
                        protected void onClick() {
                            WndEnergizeItem.energizeAll(item);
                            hide();
                            consumeAlchemize();
                        }
                    };
                    btnEnergize.setRect(0, pos + GAP, width, BTN_HEIGHT);
                    btnEnergize.icon(new ItemSprite(ItemSpriteSheet.ENERGY));
                    add(btnEnergize);

                    pos = btnEnergize.bottom();
                } else {

                    int energyAll = item.energyVal();
                    RedButton btnEnergize1 = new RedButton(Messages.get(this, "energize_1", energyAll / item.quantity())) {
                        @Override
                        protected void onClick() {
                            WndEnergizeItem.energizeOne(item);
                            hide();
                            consumeAlchemize();
                        }
                    };
                    btnEnergize1.setRect(0, pos + GAP, width, BTN_HEIGHT);
                    btnEnergize1.icon(new ItemSprite(ItemSpriteSheet.ENERGY));
                    add(btnEnergize1);
                    RedButton btnEnergizeAll = new RedButton(Messages.get(this, "energize_all", energyAll)) {
                        @Override
                        protected void onClick() {
                            WndEnergizeItem.energizeAll(item);
                            hide();
                            consumeAlchemize();
                        }
                    };
                    btnEnergizeAll.setRect(0, btnEnergize1.bottom() + 1, width, BTN_HEIGHT);
                    btnEnergizeAll.icon(new ItemSprite(ItemSpriteSheet.ENERGY));
                    add(btnEnergizeAll);

                    pos = btnEnergizeAll.bottom();
                }
            }

            resize(width, (int) pos);
        }

        private void consumeAlchemize() {
            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
            if (curItem.quantity() <= 1) {
                curItem.detachAll(Dungeon.hero.belongings.backpack);
                if (owner != null) {
                    owner.hide();
                }
            } else {
                curItem.detach(Dungeon.hero.belongings.backpack);
                if (owner != null) {
                    owner.hide();
                }
                GameScene.selectItem(itemSelector);
            }
            Catalog.countUse(getClass());
            if (curItem instanceof Alchemize && Random.Float() < ((Alchemize) curItem).talentChance) {
                Talent.onScrollUsed(curUser, curUser.pos, ((Alchemize) curItem).talentFactor, curItem.getClass());
            }
        }
    }
}
