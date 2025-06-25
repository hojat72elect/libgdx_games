

package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Berry extends Food {

    {
        image = ItemSpriteSheet.BERRY;
        energy = Hunger.HUNGRY / 3f; //100 food value

        bones = false;
    }

    @Override
    protected float eatingTime() {
        if (Dungeon.hero.hasTalent(Talent.IRON_STOMACH)
                || Dungeon.hero.hasTalent(Talent.ENERGIZING_MEAL)
                || Dungeon.hero.hasTalent(Talent.MYSTICAL_MEAL)
                || Dungeon.hero.hasTalent(Talent.INVIGORATING_MEAL)
                || Dungeon.hero.hasTalent(Talent.FOCUSED_MEAL)
                || Dungeon.hero.hasTalent(Talent.ENLIGHTENING_MEAL)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        SeedCounter counter = Buff.count(hero, SeedCounter.class, 1);
        if (counter.count() >= 2) {
            Dungeon.level.drop(Generator.randomUsingDefaults(Generator.Category.SEED), hero.pos).sprite.drop();
            counter.detach();
        }
    }

    @Override
    public int value() {
        return 5 * quantity;
    }

    public static class SeedCounter extends CounterBuff {
        {
            revivePersists = true;
        }
    }
}
