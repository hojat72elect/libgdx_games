

package com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class PotionOfMastery extends ExoticPotion {

    {
        icon = ItemSpriteSheet.Icons.POTION_MASTERY;

        unique = true;

        talentFactor = 2f;
    }

    protected static boolean identifiedByUse = false;

    @Override
    //need to override drink so that time isn't spent right away
    protected void drink(final Hero hero) {

        if (!isKnown()) {
            identify();
            curItem = detach(hero.belongings.backpack);
            identifiedByUse = true;
        } else {
            identifiedByUse = false;
        }

        GameScene.selectItem(itemSelector);
    }

    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(PotionOfMastery.class, "prompt");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return
                    (item instanceof MeleeWeapon && !((MeleeWeapon) item).masteryPotionBonus)
                            || (item instanceof Armor && !((Armor) item).masteryPotionBonus);
        }

        @Override
        public void onSelect(Item item) {

            if (item == null && identifiedByUse) {
                GameScene.show(new WndOptions(new ItemSprite(PotionOfMastery.this),
                        Messages.titleCase(name()),
                        Messages.get(ExoticPotion.class, "warning"),
                        Messages.get(ExoticPotion.class, "yes"),
                        Messages.get(ExoticPotion.class, "no")) {
                    @Override
                    protected void onSelect(int index) {
                        switch (index) {
                            case 0:
                                curUser.spendAndNext(1f);
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
            } else if (item != null) {

                if (item instanceof Weapon) {
                    ((Weapon) item).masteryPotionBonus = true;
                    GLog.p(Messages.get(PotionOfMastery.class, "weapon_easier"));
                } else if (item instanceof Armor) {
                    ((Armor) item).masteryPotionBonus = true;
                    GLog.p(Messages.get(PotionOfMastery.class, "armor_easier"));
                }
                updateQuickslot();

                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                curUser.sprite.operate(curUser.pos);

                if (!identifiedByUse) {
                    curItem.detach(curUser.belongings.backpack);
                }
                identifiedByUse = false;

                if (!anonymous) {
                    Catalog.countUse(PotionOfMastery.class);
                    if (Random.Float() < talentChance) {
                        Talent.onPotionUsed(curUser, curUser.pos, talentFactor);
                    }
                }
            }
        }
    };
}
