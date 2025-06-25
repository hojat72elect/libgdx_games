

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class CapeOfThorns extends Artifact {

    {
        image = ItemSpriteSheet.ARTIFACT_CAPE;

        levelCap = 10;

        charge = 0;
        chargeCap = 100;
        cooldown = 0;

        defaultAction = "NONE"; //so it can be quickslotted
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new Thorns();
    }

    @Override
    public void charge(Hero target, float amount) {
        if (cooldown == 0) {
            charge += Math.round(4 * amount);
            updateQuickslot();
        }
        if (charge >= chargeCap) {
            target.buff(Thorns.class).proc(0, null, null);
        }
    }

    @Override
    public String desc() {
        String desc = Messages.get(this, "desc");
        if (isEquipped(Dungeon.hero)) {
            desc += "\n\n";
            if (cooldown == 0)
                desc += Messages.get(this, "desc_inactive");
            else
                desc += Messages.get(this, "desc_active");
        }

        return desc;
    }

    public class Thorns extends ArtifactBuff {

        @Override
        public boolean act() {
            if (cooldown > 0) {
                cooldown--;
                if (cooldown == 0) {
                    GLog.w(Messages.get(this, "inert"));
                }
                updateQuickslot();
            }
            spend(TICK);
            return true;
        }

        public int proc(int damage, Char attacker, Char defender) {
            if (cooldown == 0) {
                charge += damage * (0.5 + level() * 0.05);
                if (charge >= chargeCap) {
                    charge = 0;
                    cooldown = 10 + level();
                    GLog.p(Messages.get(this, "radiating"));
                }
            }

            if (cooldown != 0) {
                int deflected = Random.NormalIntRange(0, damage);
                damage -= deflected;

                if (attacker != null && Dungeon.level.adjacent(attacker.pos, defender.pos)) {
                    attacker.damage(deflected, this);
                }

                exp += deflected;

                if (exp >= (level() + 1) * 5 && level() < levelCap) {
                    exp -= (level() + 1) * 5;
                    upgrade();
                    Catalog.countUse(CapeOfThorns.class);
                    GLog.p(Messages.get(this, "levelup"));
                }
            }
            updateQuickslot();
            return damage;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns(cooldown));
        }

        @Override
        public int icon() {
            if (cooldown == 0)
                return BuffIndicator.NONE;
            else
                return BuffIndicator.THORNS;
        }

        @Override
        public void detach() {
            cooldown = 0;
            charge = 0;
            super.detach();
        }
    }
}
