

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.watabou.utils.Bundle;

public class SnipersMark extends FlavourBuff implements ActionIndicator.Action {

    public int object = 0;
    public float percentDmgBonus = 0;

    private static final String OBJECT = "object";
    private static final String BONUS = "bonus";

    public static final float DURATION = 4f;

    {
        type = buffType.POSITIVE;
    }

    public void set(int object, float bonus) {
        this.object = object;
        this.percentDmgBonus = bonus;
    }

    @Override
    public boolean attachTo(Char target) {
        ActionIndicator.setAction(this);
        return super.attachTo(target);
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(OBJECT, object);
        bundle.put(BONUS, percentDmgBonus);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        object = bundle.getInt(OBJECT);
        percentDmgBonus = bundle.getFloat(BONUS);
    }

    @Override
    public int icon() {
        return BuffIndicator.MARK;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    @Override
    public String actionName() {
        SpiritBow bow = Dungeon.hero.belongings.getItem(SpiritBow.class);

        if (bow == null) return null;

        switch (bow.augment) {
            case NONE:
            default:
                return Messages.get(this, "action_name_snapshot");
            case SPEED:
                return Messages.get(this, "action_name_volley");
            case DAMAGE:
                return Messages.get(this, "action_name_sniper");
        }
    }

    @Override
    public int actionIcon() {
        return HeroIcon.SNIPERS_MARK;
    }

    @Override
    public int indicatorColor() {
        return 0x444444;
    }

    @Override
    public void doAction() {

        Hero hero = Dungeon.hero;
        if (hero == null) return;

        SpiritBow bow = hero.belongings.getItem(SpiritBow.class);
        if (bow == null) return;

        SpiritBow.SpiritArrow arrow = bow.knockArrow();
        if (arrow == null) return;

        Char ch = (Char) Actor.findById(object);
        if (ch == null) return;

        int cell = QuickSlotButton.autoAim(ch, arrow);
        if (cell == -1) return;

        bow.sniperSpecial = true;
        bow.sniperSpecialBonusDamage = percentDmgBonus;

        arrow.cast(hero, cell);
        detach();
    }
}
