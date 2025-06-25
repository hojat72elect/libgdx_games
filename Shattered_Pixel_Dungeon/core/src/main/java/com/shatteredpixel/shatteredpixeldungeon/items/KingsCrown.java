

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChooseAbility;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class KingsCrown extends Item {

    private static final String AC_WEAR = "WEAR";

    {
        image = ItemSpriteSheet.CROWN;

        defaultAction = AC_WEAR;

        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_WEAR);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_WEAR)) {

            curUser = hero;
            if (hero.belongings.armor() != null) {
                GameScene.show(new WndChooseAbility(this, hero.belongings.armor(), hero));
            } else {
                GLog.w(Messages.get(this, "naked"));
            }
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

    public void upgradeArmor(Hero hero, Armor armor, ArmorAbility ability) {

        detach(hero.belongings.backpack);
        Catalog.countUse(getClass());

        hero.sprite.emitter().burst(Speck.factory(Speck.CROWN), 12);
        hero.spend(Actor.TICK);
        hero.busy();

        if (armor != null) {

            if (ability instanceof Ratmogrify) {
                GLog.p(Messages.get(this, "ratgraded"));
            } else {
                GLog.p(Messages.get(this, "upgraded"));
            }

            ClassArmor classArmor = ClassArmor.upgrade(hero, armor);
            if (hero.belongings.armor == armor) {

                hero.belongings.armor = classArmor;
                ((HeroSprite) hero.sprite).updateArmor();
                classArmor.activate(hero);
            } else {

                armor.detach(hero.belongings.backpack);
                classArmor.collect(hero.belongings.backpack);
            }
        }

        hero.armorAbility = ability;
        Talent.initArmorTalents(hero);

        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.MASTERY);
    }
}
