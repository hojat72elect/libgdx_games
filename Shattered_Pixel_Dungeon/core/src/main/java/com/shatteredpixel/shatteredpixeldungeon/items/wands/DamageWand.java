

package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WandEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.audio.Sample;

//for wands that directly damage a target
//wands with AOE or circumstantial direct damage count here (e.g. fireblast, transfusion), but wands with indirect damage do not (e.g. corrosion)
public abstract class DamageWand extends Wand {

    public int min() {
        return min(buffedLvl());
    }

    public abstract int min(int lvl);

    public int max() {
        return max(buffedLvl());
    }

    public abstract int max(int lvl);

    public int damageRoll() {
        return damageRoll(buffedLvl());
    }

    public int damageRoll(int lvl) {
        int dmg = Hero.heroDamageIntRange(min(lvl), max(lvl));
        WandEmpower emp = Dungeon.hero.buff(WandEmpower.class);
        if (emp != null) {
            dmg += emp.dmgBoost;
            emp.left--;
            if (emp.left <= 0) {
                emp.detach();
            }
            Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG, 0.75f, 1.2f);
        }
        return dmg;
    }

    @Override
    public String statsDesc() {
        if (levelKnown)
            return Messages.get(this, "stats_desc", min(), max());
        else
            return Messages.get(this, "stats_desc", min(0), max(0));
    }

    @Override
    public String upgradeStat1(int level) {
        return min(level) + "-" + max(level);
    }
}
