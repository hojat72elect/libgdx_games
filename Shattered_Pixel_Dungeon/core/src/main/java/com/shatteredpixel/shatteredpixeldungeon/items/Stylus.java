

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Enchanting;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Stylus extends Item {

    private static final float TIME_TO_INSCRIBE = 2;

    private static final String AC_INSCRIBE = "INSCRIBE";

    {
        image = ItemSpriteSheet.STYLUS;

        stackable = true;

        defaultAction = AC_INSCRIBE;

        bones = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_INSCRIBE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_INSCRIBE)) {

            curUser = hero;
            GameScene.selectItem(itemSelector);
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    private void inscribe(Armor armor) {

        if (!armor.cursedKnown) {
            GLog.w(Messages.get(this, "identify"));
            return;
        } else if (armor.cursed || armor.hasCurseGlyph()) {
            GLog.w(Messages.get(this, "cursed"));
            return;
        }

        detach(curUser.belongings.backpack);
        Catalog.countUse(getClass());

        GLog.w(Messages.get(this, "inscribed"));

        armor.inscribe();

        curUser.sprite.operate(curUser.pos);
        curUser.sprite.centerEmitter().start(PurpleParticle.BURST, 0.05f, 10);
        Enchanting.show(curUser, armor);
        Sample.INSTANCE.play(Assets.Sounds.BURNING);

        curUser.spend(TIME_TO_INSCRIBE);
        curUser.busy();
    }

    @Override
    public int value() {
        return 30 * quantity;
    }

    private final WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(Stylus.class, "prompt");
        }

        @Override
        public Class<? extends Bag> preferredBag() {
            return Belongings.Backpack.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof Armor;
        }

        @Override
        public void onSelect(Item item) {
            if (item != null) {
                Stylus.this.inscribe((Armor) item);
            }
        }
    };
}
