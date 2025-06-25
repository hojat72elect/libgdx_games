

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class CurseInfusion extends InventorySpell {

    {
        image = ItemSpriteSheet.CURSE_INFUSE;

        talentChance = 1 / (float) Recipe.OUT_QUANTITY;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        return ((item instanceof EquipableItem && !(item instanceof MissileWeapon)) || item instanceof Wand);
    }

    @Override
    protected void onItemSelected(Item item) {

        CellEmitter.get(curUser.pos).burst(ShadowParticle.UP, 5);
        Sample.INSTANCE.play(Assets.Sounds.CURSED);

        item.cursed = true;
        if (item instanceof MeleeWeapon || item instanceof SpiritBow) {
            Weapon w = (Weapon) item;
            if (w.enchantment != null) {
                //if we are freshly applying curse infusion, don't replace an existing curse
                if (w.hasGoodEnchant() || w.curseInfusionBonus) {
                    w.enchant(Weapon.Enchantment.randomCurse(w.enchantment.getClass()));
                }
            } else {
                w.enchant(Weapon.Enchantment.randomCurse());
            }
            w.curseInfusionBonus = true;
            if (w instanceof MagesStaff) {
                ((MagesStaff) w).updateWand(true);
            }
        } else if (item instanceof Armor) {
            Armor a = (Armor) item;
            if (a.glyph != null) {
                //if we are freshly applying curse infusion, don't replace an existing curse
                if (a.hasGoodGlyph() || a.curseInfusionBonus) {
                    a.inscribe(Armor.Glyph.randomCurse(a.glyph.getClass()));
                }
            } else {
                a.inscribe(Armor.Glyph.randomCurse());
            }
            a.curseInfusionBonus = true;
        } else if (item instanceof Wand) {
            ((Wand) item).curseInfusionBonus = true;
            ((Wand) item).updateLevel();
        } else if (item instanceof RingOfMight) {
            curUser.updateHT(false);
        }
        Badges.validateItemLevelAquired(item);
        updateQuickslot();
    }

    @Override
    public int value() {
        return (int) (60 * (quantity / (float) Recipe.OUT_QUANTITY));
    }

    @Override
    public int energyVal() {
        return (int) (12 * (quantity / (float) Recipe.OUT_QUANTITY));
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        private static final int OUT_QUANTITY = 4;

        {
            inputs = new Class[]{ScrollOfRemoveCurse.class, MetalShard.class};
            inQuantity = new int[]{1, 1};

            cost = 6;

            output = CurseInfusion.class;
            outQuantity = OUT_QUANTITY;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            Catalog.countUse(MetalShard.class);
            return super.brew(ingredients);
        }
    }
}
