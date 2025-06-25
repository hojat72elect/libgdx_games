

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Cleanse extends ClericSpell {

    public static Cleanse INSTANCE = new Cleanse();

    @Override
    public int icon() {
        return HeroIcon.CLEANSE;
    }

    @Override
    public float chargeUse(Hero hero) {
        return 2;
    }

    public String desc() {
        int immunity = 2 * (Dungeon.hero.pointsInTalent(Talent.CLEANSE) - 1);
        if (immunity > 0) immunity++;
        int shield = 10 * Dungeon.hero.pointsInTalent(Talent.CLEANSE);
        return Messages.get(this, "desc", immunity, shield) + "\n\n" + Messages.get(this, "charge_cost", (int) chargeUse(Dungeon.hero));
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.hasTalent(Talent.CLEANSE);
    }

    @Override
    public void onCast(HolyTome tome, Hero hero) {

        ArrayList<Char> affected = new ArrayList<>();
        affected.add(hero);

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (Dungeon.level.heroFOV[mob.pos] && mob.alignment == Char.Alignment.ALLY) {
                affected.add(mob);
            }
        }

        Char ally = PowerOfMany.getPoweredAlly();
        //hero is always affected, to just check for life linked ally
        if (ally != null && ally.buff(LifeLinkSpell.LifeLinkSpellBuff.class) != null
                && !affected.contains(ally)) {
            affected.add(ally);
        }

        for (Char ch : affected) {
            for (Buff b : ch.buffs()) {
                if (b.type == Buff.buffType.NEGATIVE
                        && !(b instanceof AllyBuff)
                        && !(b instanceof LostInventory)) {
                    b.detach();
                }
            }

            if (hero.pointsInTalent(Talent.CLEANSE) > 1) {
                //0, 2, or 4. 1 less than displayed as spell is instant
                Buff.prolong(ch, PotionOfCleansing.Cleanse.class, 2 * (Dungeon.hero.pointsInTalent(Talent.CLEANSE) - 1));
            }
            Buff.affect(ch, Barrier.class).setShield(10 * hero.pointsInTalent(Talent.CLEANSE));
            new Flare(6, 32).color(0xFF4CD2, true).show(ch.sprite, 2f);
        }

        hero.spend(1f);
        hero.busy();
        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.READ);

        onSpellCast(tome, hero);
    }
}
