

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Dazzling extends Weapon.Enchantment {

    private static final ItemSprite.Glowing BLACK = new ItemSprite.Glowing(0x000000);

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {

        float procChance = 1 / 10f * procChanceMultiplier(attacker);
        if (Random.Float() < procChance) {
            for (Char ch : Actor.chars()) {
                if (ch.fieldOfView != null && ch.fieldOfView[defender.pos]) {
                    Buff.prolong(ch, Blindness.class, ch == attacker ? Blindness.DURATION : Blindness.DURATION / 2f);
                    if (ch == Dungeon.hero) {
                        GameScene.flash(0x80FFFFFF);
                    }
                }
            }
            if (Dungeon.level.heroFOV[attacker.pos] || Dungeon.level.heroFOV[defender.pos]) {
                Sample.INSTANCE.play(Assets.Sounds.BLAST);
            }
        }

        return damage;
    }

    @Override
    public boolean curse() {
        return true;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }
}
