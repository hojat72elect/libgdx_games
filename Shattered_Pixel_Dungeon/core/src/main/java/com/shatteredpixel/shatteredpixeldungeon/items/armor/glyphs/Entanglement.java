

package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor.Glyph;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Entanglement extends Glyph {

    private static final ItemSprite.Glowing BROWN = new ItemSprite.Glowing(0x663300);

    @Override
    public int proc(Armor armor, Char attacker, final Char defender, final int damage) {

        final int level = Math.max(0, armor.buffedLvl());
        float procChance = 1 / 4f * procChanceMultiplier(defender);

        if (Random.Float() < procChance) {

            float powerMulti = Math.max(1f, procChance);

            Buff.affect(defender, Earthroot.Armor.class).level(Math.round((5 + 2 * level) * powerMulti));
            CellEmitter.bottom(defender.pos).start(EarthParticle.FACTORY, 0.05f, 8);
            if (defender == Dungeon.hero) PixelScene.shake(1, 0.4f);
        }

        return damage;
    }

    @Override
    public Glowing glowing() {
        return BROWN;
    }
}
