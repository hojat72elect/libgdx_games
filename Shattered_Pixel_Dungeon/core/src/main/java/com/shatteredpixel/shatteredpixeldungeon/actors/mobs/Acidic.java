

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AcidicSprite;

public class Acidic extends Scorpio {

    {
        spriteClass = AcidicSprite.class;

        properties.add(Property.ACIDIC);

        loot = PotionOfExperience.class;
        lootChance = 1f;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        Buff.affect(enemy, Ooze.class).set(Ooze.DURATION);
        return super.attackProc(enemy, damage);
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (Dungeon.level.adjacent(pos, enemy.pos)) {
            Buff.affect(enemy, Ooze.class).set(Ooze.DURATION);
        }
        return super.defenseProc(enemy, damage);
    }

    @Override
    public Item createLoot() {
        return new PotionOfExperience();
    }
}
