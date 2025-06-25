

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.CursedWand;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class WildEnergy extends TargetedSpell {

    {
        image = ItemSpriteSheet.WILD_ENERGY;

        usesTargeting = true;

        talentChance = 1 / (float) Recipe.OUT_QUANTITY;
    }

    //we rely on cursedWand to do fx instead
    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        CursedWand.cursedZap(this, curUser, bolt, callback);
    }

    @Override
    protected void affectTarget(Ballistica bolt, final Hero hero) {
        Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        ScrollOfRecharging.charge(hero);
        SpellSprite.show(hero, SpellSprite.CHARGE);

        hero.belongings.charge(1f);
        ArtifactRecharge.chargeArtifacts(hero, 4f);

        Buff.affect(hero, Recharging.class, 8f);
        Buff.affect(hero, ArtifactRecharge.class).extend(8).ignoreHornOfPlenty = false;
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

        private static final int OUT_QUANTITY = 5;

        {
            inputs = new Class[]{ScrollOfRecharging.class, MetalShard.class};
            inQuantity = new int[]{1, 1};

            cost = 4;

            output = WildEnergy.class;
            outQuantity = OUT_QUANTITY;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            Catalog.countUse(MetalShard.class);
            return super.brew(ingredients);
        }
    }
}
