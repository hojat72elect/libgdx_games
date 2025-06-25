

package com.shatteredpixel.shatteredpixeldungeon.items.armor.curses;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Overgrowth extends Armor.Glyph {

    private static final ItemSprite.Glowing BLACK = new ItemSprite.Glowing(0x000000);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        float procChance = 1 / 20f * procChanceMultiplier(defender);
        if (Random.Float() < procChance) {

            Plant p = ((Plant.Seed) Generator.randomUsingDefaults(Generator.Category.SEED)).couch(defender.pos, null);

            //momentarily revoke warden benefits, otherwise this curse would be incredibly powerful
            if (defender instanceof Hero && ((Hero) defender).subClass == HeroSubClass.WARDEN) {
                ((Hero) defender).subClass = HeroSubClass.NONE;
                p.activate(defender);
                ((Hero) defender).subClass = HeroSubClass.WARDEN;
            } else {
                p.activate(defender);
            }


            CellEmitter.get(defender.pos).burst(LeafParticle.LEVEL_SPECIFIC, 10);
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }

    @Override
    public boolean curse() {
        return true;
    }
}
