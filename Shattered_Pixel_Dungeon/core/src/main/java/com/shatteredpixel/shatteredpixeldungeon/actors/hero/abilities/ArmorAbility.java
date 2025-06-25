

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public abstract class ArmorAbility implements Bundlable {

    protected float baseChargeUse = 35;

    public void use(ClassArmor armor, Hero hero) {
        if (targetingPrompt() == null) {
            activate(armor, hero, hero.pos);
        } else {
            GameScene.selectCell(new CellSelector.Listener() {
                @Override
                public void onSelect(Integer cell) {
                    activate(armor, hero, cell);
                }

                @Override
                public String prompt() {
                    return targetingPrompt();
                }
            });
        }
    }

    //leave null for no targeting
    public String targetingPrompt() {
        return null;
    }

    public boolean useTargeting() {
        return targetingPrompt() != null;
    }

    public int targetedPos(Char user, int dst) {
        return new Ballistica(user.pos, dst, Ballistica.PROJECTILE).collisionPos;
    }

    public float chargeUse(Hero hero) {
        float chargeUse = baseChargeUse;
        if (hero.hasTalent(Talent.HEROIC_ENERGY)) {
            //reduced charge use by 12%/23%/32%/40%
            switch (hero.pointsInTalent(Talent.HEROIC_ENERGY)) {
                case 1:
                default:
                    chargeUse *= 0.88f;
                    break;
                case 2:
                    chargeUse *= 0.77f;
                    break;
                case 3:
                    chargeUse *= 0.68f;
                    break;
                case 4:
                    chargeUse *= 0.6f;
                    break;
            }
        }
        return chargeUse;
    }

    protected abstract void activate(ClassArmor armor, Hero hero, Integer target);

    public String name() {
        return Messages.get(this, "name");
    }

    public String shortDesc() {
        return Messages.get(this, "short_desc");
    }

    public String desc() {
        return Messages.get(this, "desc") + "\n\n" + Messages.get(this, "cost", (int) baseChargeUse);
    }

    public int icon() {
        return HeroIcon.NONE;
    }

    public abstract Talent[] talents();

    @Override
    public void storeInBundle(Bundle bundle) {
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
    }
}
