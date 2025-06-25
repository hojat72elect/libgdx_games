

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkeletonSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Skeleton extends Mob {

    {
        spriteClass = SkeletonSprite.class;

        HP = HT = 25;
        defenseSkill = 9;

        EXP = 5;
        maxLvl = 10;

        loot = Generator.Category.WEAPON;
        lootChance = 0.1667f; //by default, see lootChance()

        properties.add(Property.UNDEAD);
        properties.add(Property.INORGANIC);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(2, 10);
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        if (cause == Chasm.class) return;

        boolean heroKilled = false;
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            Char ch = findChar(pos + PathFinder.NEIGHBOURS8[i]);
            if (ch != null && ch.isAlive()) {
                int damage = Math.round(Random.NormalIntRange(6, 12));
                damage = Math.round(damage * AscensionChallenge.statModifier(this));

                //all sources of DR are 2x effective vs. bone explosion
                //this does not consume extra uses of rock armor and earthroot armor

                WandOfLivingEarth.RockArmor rockArmor = ch.buff(WandOfLivingEarth.RockArmor.class);
                if (rockArmor != null) {
                    int preDmg = damage;
                    damage = rockArmor.absorb(damage);
                    damage *= Math.round(damage / (float) preDmg); //apply the % reduction twice
                }

                Earthroot.Armor armor = ch.buff(Earthroot.Armor.class);
                if (damage > 0 && armor != null) {
                    int preDmg = damage;
                    damage = armor.absorb(damage);
                    damage -= (preDmg - damage); //apply the flat reduction twice
                }

                //apply DR twice (with 2 rolls for more consistency)
                damage = Math.max(0, damage - (ch.drRoll() + ch.drRoll()));
                ch.damage(damage, this);
                if (ch == Dungeon.hero && !ch.isAlive()) {
                    heroKilled = true;
                }
            }
        }

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play(Assets.Sounds.BONES);
        }

        if (heroKilled) {
            Dungeon.fail(this);
            GLog.n(Messages.get(this, "explo_kill"));
        }
    }

    @Override
    public float lootChance() {
        //each drop makes future drops 1/3 as likely
        // so loot chance looks like: 1/6, 1/18, 1/54, 1/162, etc.
        return super.lootChance() * (float) Math.pow(1 / 3f, Dungeon.LimitedDrops.SKELE_WEP.count);
    }

    @Override
    public Item createLoot() {
        Dungeon.LimitedDrops.SKELE_WEP.count++;
        return super.createLoot();
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 5);
    }
}
