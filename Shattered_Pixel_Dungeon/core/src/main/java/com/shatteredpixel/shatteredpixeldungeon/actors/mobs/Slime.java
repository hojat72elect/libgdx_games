

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeSprite;
import com.watabou.utils.Random;

public class Slime extends Mob {

    {
        spriteClass = SlimeSprite.class;

        HP = HT = 20;
        defenseSkill = 5;

        EXP = 4;
        maxLvl = 9;

        lootChance = 0.2f; //by default, see lootChance()
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(2, 5);
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public void damage(int dmg, Object src) {
        float scaleFactor = AscensionChallenge.statModifier(this);
        int scaledDmg = Math.round(dmg / scaleFactor);
        if (scaledDmg >= 5) {
            //takes 5/6/7/8/9/10 dmg at 5/7/10/14/19/25 incoming dmg
            scaledDmg = 4 + (int) (Math.sqrt(8 * (scaledDmg - 4) + 1) - 1) / 2;
        }
        dmg = (int) (scaledDmg * AscensionChallenge.statModifier(this));
        super.damage(dmg, src);
    }

    @Override
    public float lootChance() {
        //each drop makes future drops 1/4 as likely
        // so loot chance looks like: 1/5, 1/20, 1/80, 1/320, etc.
        return super.lootChance() * (float) Math.pow(1 / 4f, Dungeon.LimitedDrops.SLIME_WEP.count);
    }

    @Override
    public Item createLoot() {
        Dungeon.LimitedDrops.SLIME_WEP.count++;
        Generator.Category c = Generator.Category.WEP_T2;
        MeleeWeapon w = (MeleeWeapon) Generator.randomUsingDefaults(Generator.Category.WEP_T2);
        w.level(0);
        return w;
    }
}
