

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Explosive extends Weapon.Enchantment {

    private static final ItemSprite.Glowing BLACK = new ItemSprite.Glowing(0x000000);
    private static final ItemSprite.Glowing WARM = new ItemSprite.Glowing(0x000000, 0.5f);
    private static final ItemSprite.Glowing HOT = new ItemSprite.Glowing(0x000000, 0.25f);
    private int durability = 100;

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {

        //average value of 5, or 20 hits to an explosion
        int durToReduce = Math.round(Random.IntRange(0, 10) * procChanceMultiplier(attacker));
        int currentDurability = durability;
        durability -= durToReduce;

        if (currentDurability > 50 && durability <= 50) {
            attacker.sprite.showStatus(CharSprite.WARNING, Messages.get(this, "warm"));
            GLog.w(Messages.get(this, "desc_warm"));
            attacker.sprite.emitter().burst(SmokeParticle.FACTORY, 4);
            Item.updateQuickslot();
        } else if (currentDurability > 10 && durability <= 10) {
            attacker.sprite.showStatus(CharSprite.WARNING, Messages.get(this, "hot"));
            GLog.n(Messages.get(this, "desc_hot"));
            attacker.sprite.emitter().burst(BlastParticle.FACTORY, 5);
            Item.updateQuickslot();
        } else if (durability <= 0) {
            //explosion position is the closest adjacent cell to the defender
            // this will be the attacker's position if they are adjacent
            int explosionPos = -1;
            for (int i : PathFinder.NEIGHBOURS8) {
                if (!Dungeon.level.solid[defender.pos + i] &&
                        (explosionPos == -1 ||
                                Dungeon.level.trueDistance(attacker.pos, defender.pos + i) < Dungeon.level.trueDistance(attacker.pos, explosionPos))) {
                    explosionPos = defender.pos + i;
                }
            }
            if (explosionPos == -1) {
                explosionPos = defender.pos;
            }

            new Bomb.ConjuredBomb().explode(explosionPos);

            durability = 100;
            Item.updateQuickslot();
        }

        return damage;
    }

    @Override
    public boolean curse() {
        return true;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        if (durability > 50) {
            return BLACK;
        } else if (durability > 10) {
            return WARM;
        } else {
            return HOT;
        }
    }

    @Override
    public String desc() {
        String desc = super.desc();
        if (durability > 50) {
            desc += " " + Messages.get(this, "desc_cool");
        } else if (durability > 10) {
            desc += " " + Messages.get(this, "desc_warm");
        } else {
            desc += " _" + Messages.get(this, "desc_hot") + "_";
        }
        return desc;
    }

    private static final String DURABILITY = "durability";

    @Override
    public void restoreFromBundle(Bundle bundle) {
        durability = bundle.getInt(DURABILITY);
        //pre-1.3 saves
        if (durability <= 0) {
            durability = 100;
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(DURABILITY, durability);
    }
}
