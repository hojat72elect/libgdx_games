

package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Food extends Item {

    public static final float TIME_TO_EAT = 3f;

    public static final String AC_EAT = "EAT";

    public float energy = Hunger.HUNGRY;

    {
        stackable = true;
        image = ItemSpriteSheet.RATION;

        defaultAction = AC_EAT;

        bones = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_EAT);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_EAT)) {

            detach(hero.belongings.backpack);
            Catalog.countUse(getClass());

            satisfy(hero);
            GLog.i(Messages.get(this, "eat_msg"));

            hero.sprite.operate(hero.pos);
            hero.busy();
            SpellSprite.show(hero, SpellSprite.FOOD);
            eatSFX();

            hero.spend(eatingTime());

            Talent.onFoodEaten(hero, energy, this);

            Statistics.foodEaten++;
            Badges.validateFoodEaten();
        }
    }

    protected void eatSFX() {
        Sample.INSTANCE.play(Assets.Sounds.EAT);
    }

    protected float eatingTime() {
        if (Dungeon.hero.hasTalent(Talent.IRON_STOMACH)
                || Dungeon.hero.hasTalent(Talent.ENERGIZING_MEAL)
                || Dungeon.hero.hasTalent(Talent.MYSTICAL_MEAL)
                || Dungeon.hero.hasTalent(Talent.INVIGORATING_MEAL)
                || Dungeon.hero.hasTalent(Talent.FOCUSED_MEAL)
                || Dungeon.hero.hasTalent(Talent.ENLIGHTENING_MEAL)) {
            return TIME_TO_EAT - 2;
        } else {
            return TIME_TO_EAT;
        }
    }

    protected void satisfy(Hero hero) {
        float foodVal = energy;
        if (Dungeon.isChallenged(Challenges.NO_FOOD)) {
            foodVal /= 3f;
        }

        Artifact.ArtifactBuff buff = hero.buff(HornOfPlenty.hornRecharge.class);
        if (buff != null && buff.isCursed()) {
            foodVal *= 0.67f;
            GLog.n(Messages.get(Hunger.class, "cursedhorn"));
        }

        Buff.affect(hero, Hunger.class).satisfy(foodVal);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 10 * quantity;
    }
}
