

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class EnergyCrystal extends Item {

    {
        image = ItemSpriteSheet.ENERGY;
        stackable = true;
    }

    public EnergyCrystal() {
        this(1);
    }

    public EnergyCrystal(int value) {
        this.quantity = value;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        return new ArrayList<>();
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {

        Catalog.setSeen(getClass());
        Statistics.itemTypesDiscovered.add(getClass());

        Dungeon.energy += quantity;
        //TODO track energy collected maybe? We do already track recipes crafted though..

        GameScene.pickUp(this, pos);
        hero.sprite.showStatusWithIcon(0x44CCFF, Integer.toString(quantity), FloatingText.ENERGY);
        hero.spendAndNext(TIME_TO_PICK_UP);

        Sample.INSTANCE.play(Assets.Sounds.ITEM);

        updateQuickslot();

        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
}
