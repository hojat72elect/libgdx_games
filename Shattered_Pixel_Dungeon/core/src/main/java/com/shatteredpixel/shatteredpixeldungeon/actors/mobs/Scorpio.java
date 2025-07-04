

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ScorpioSprite;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class Scorpio extends Mob {

    {
        spriteClass = ScorpioSprite.class;

        HP = HT = 110;
        defenseSkill = 24;
        viewDistance = Light.DISTANCE;

        EXP = 14;
        maxLvl = 27;

        loot = Generator.Category.POTION;
        lootChance = 0.5f;

        properties.add(Property.DEMONIC);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(30, 40);
    }

    @Override
    public int attackSkill(Char target) {
        return 36;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 16);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return !Dungeon.level.adjacent(pos, enemy.pos)
                && (super.canAttack(enemy) || new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        if (Random.Int(2) == 0) {
            Buff.prolong(enemy, Cripple.class, Cripple.DURATION);
        }

        return damage;
    }

    @Override
    protected boolean getCloser(int target) {
        if (state == HUNTING) {
            return enemySeen && getFurther(target);
        } else {
            return super.getCloser(target);
        }
    }

    @Override
    public void aggro(Char ch) {
        //cannot be aggroed to something it can't see
        //skip this check if FOV isn't initialized
        if (ch == null || fieldOfView == null
                || fieldOfView.length != Dungeon.level.length() || fieldOfView[ch.pos]) {
            super.aggro(ch);
        }
    }

    @Override
    public Item createLoot() {
        Class<? extends Potion> loot;
        do {
            loot = (Class<? extends Potion>) Random.oneOf(Generator.Category.POTION.classes);
        } while (loot == PotionOfHealing.class || loot == PotionOfStrength.class);

        return Reflection.newInstance(loot);
    }
}
