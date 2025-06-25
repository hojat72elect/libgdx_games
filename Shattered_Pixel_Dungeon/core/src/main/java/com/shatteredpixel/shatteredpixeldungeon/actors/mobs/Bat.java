

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Random;

public class Bat extends Mob {

    {
        spriteClass = BatSprite.class;

        HP = HT = 30;
        defenseSkill = 15;
        baseSpeed = 2f;

        EXP = 7;
        maxLvl = 15;

        flying = true;

        loot = PotionOfHealing.class;
        lootChance = 0.1667f; //by default, see lootChance()
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(5, 18);
    }

    @Override
    public int attackSkill(Char target) {
        return 16;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 4);
    }

    @Override
    public void die(Object cause) {
        flying = false;
        super.die(cause);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        int reg = Math.min(damage - 4, HT - HP);

        if (reg > 0) {
            HP += reg;
            sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(reg), FloatingText.HEALING);
        }

        return damage;
    }

    @Override
    public float lootChance() {
        return super.lootChance() * ((7f - Dungeon.LimitedDrops.BAT_HP.count) / 7f);
    }

    @Override
    public Item createLoot() {
        Dungeon.LimitedDrops.BAT_HP.count++;
        return super.createLoot();
    }
}
