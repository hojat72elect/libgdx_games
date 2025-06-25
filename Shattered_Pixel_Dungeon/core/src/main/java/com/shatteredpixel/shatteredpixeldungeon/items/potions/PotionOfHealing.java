

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class PotionOfHealing extends Potion {

    {
        icon = ItemSpriteSheet.Icons.POTION_HEALING;

        bones = true;
    }

    @Override
    public void apply(Hero hero) {
        identify();
        cure(hero);
        heal(hero);
    }

    public static void heal(Char ch) {
        if (ch == Dungeon.hero && Dungeon.isChallenged(Challenges.NO_HEALING)) {
            pharmacophobiaProc(Dungeon.hero);
        } else {
            //starts out healing 30 hp, equalizes with hero health total at level 11
            Healing healing = Buff.affect(ch, Healing.class);
            healing.setHeal((int) (0.8f * ch.HT + 14), 0.25f, 0);
            healing.applyVialEffect();
            if (ch == Dungeon.hero) {
                GLog.p(Messages.get(PotionOfHealing.class, "heal"));
            }
        }
    }

    public static void pharmacophobiaProc(Hero hero) {
        // harms the hero for ~40% of their max HP in poison
        Buff.affect(hero, Poison.class).set(4 + hero.lvl / 2);
    }

    public static void cure(Char ch) {
        Buff.detach(ch, Poison.class);
        Buff.detach(ch, Cripple.class);
        Buff.detach(ch, Weakness.class);
        Buff.detach(ch, Vulnerable.class);
        Buff.detach(ch, Bleeding.class);
        Buff.detach(ch, Blindness.class);
        Buff.detach(ch, Drowsy.class);
        Buff.detach(ch, Slow.class);
        Buff.detach(ch, Vertigo.class);
    }

    @Override
    public int value() {
        return isKnown() ? 30 * quantity : super.value();
    }
}
