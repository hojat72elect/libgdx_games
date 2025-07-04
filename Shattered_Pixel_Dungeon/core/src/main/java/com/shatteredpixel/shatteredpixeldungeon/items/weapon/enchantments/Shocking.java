

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Shocking extends Weapon.Enchantment {

    private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing(0xFFFFFF, 0.5f);

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.buffedLvl());

        // lvl 0 - 25%
        // lvl 1 - 40%
        // lvl 2 - 50%
        float procChance = (level + 1f) / (level + 4f) * procChanceMultiplier(attacker);
        if (Random.Float() < procChance) {

            float powerMulti = Math.max(1f, procChance);

            affected.clear();
            arcs.clear();

            arc(attacker, defender, 2, affected, arcs);

            affected.remove(defender); //defender isn't hurt by lightning
            for (Char ch : affected) {
                if (ch.alignment != attacker.alignment) {
                    ch.damage(Math.round(damage * 0.4f * powerMulti), this);
                }
            }

            attacker.sprite.parent.addToFront(new Lightning(arcs, null));
            Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }

    private final ArrayList<Char> affected = new ArrayList<>();

    private final ArrayList<Lightning.Arc> arcs = new ArrayList<>();

    public static void arc(Char attacker, Char defender, int dist, ArrayList<Char> affected, ArrayList<Lightning.Arc> arcs) {

        defender.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
        defender.sprite.flash();

        ArrayList<Char> hitThisArc = new ArrayList<>();
        PathFinder.buildDistanceMap(defender.pos, BArray.not(Dungeon.level.solid, null), dist);
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                Char n = Actor.findChar(i);
                if (n != null && n != attacker && !affected.contains(n)) {
                    hitThisArc.add(n);
                }
            }
        }

        affected.addAll(hitThisArc);
        for (Char hit : hitThisArc) {
            arcs.add(new Lightning.Arc(defender.sprite.center(), hit.sprite.center()));
            arc(attacker, hit, (Dungeon.level.water[hit.pos] && !hit.flying) ? 2 : 1, affected, arcs);
        }
    }
}
