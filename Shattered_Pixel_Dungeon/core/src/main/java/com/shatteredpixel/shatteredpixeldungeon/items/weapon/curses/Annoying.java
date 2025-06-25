

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Annoying extends Weapon.Enchantment {

    private static final ItemSprite.Glowing BLACK = new ItemSprite.Glowing(0x000000);

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {

        float procChance = 1 / 20f * procChanceMultiplier(attacker);
        if (Random.Float() < procChance) {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                mob.beckon(attacker.pos);
            }
            attacker.sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f, 3);
            Sample.INSTANCE.play(Assets.Sounds.MIMIC);
            Invisibility.dispel();
            //~1/100 for each rare line, ~1/10 for each common line
            if (Random.Int(33) != 0) {
                GLog.n(Messages.get(this, "msg_" + Random.IntRange(1, 10)));
            } else {
                GLog.n(Messages.get(this, "msg_" + Random.IntRange(11, 13)));
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