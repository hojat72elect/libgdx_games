

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;

//generic class for buffs which convert an enemy into an ally
// There is a decent amount of logic that ties into this, which is why it has its own abstract class
public abstract class AllyBuff extends Buff {

    {
        revivePersists = true;
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            target.alignment = Char.Alignment.ALLY;
            if (target.buff(PinCushion.class) != null) {
                target.buff(PinCushion.class).detach();
            }
            return true;
        } else {
            return false;
        }
    }

    //for when applying an ally buff should also cause that enemy to give exp/loot as if they had died
    //consider that chars with the ally alignment do not drop items or award exp on death
    public static void affectAndLoot(Mob enemy, Hero hero, Class<? extends AllyBuff> buffCls) {
        boolean wasEnemy = enemy.alignment == Char.Alignment.ENEMY || enemy instanceof Mimic;
        Buff.affect(enemy, buffCls);

        if (enemy.buff(buffCls) != null && wasEnemy) {
            enemy.rollToDropLoot();

            Statistics.enemiesSlain++;
            Badges.validateMonstersSlain();
            Statistics.qualifiedForNoKilling = false;
            Bestiary.setSeen(enemy.getClass());
            Bestiary.countEncounter(enemy.getClass());

            AscensionChallenge.processEnemyKill(enemy);

            int exp = hero.lvl <= enemy.maxLvl ? enemy.EXP : 0;
            if (exp > 0) {
                hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(exp), FloatingText.EXPERIENCE);
            }
            hero.earnExp(exp, enemy.getClass());

            if (hero.subClass == HeroSubClass.MONK) {
                Buff.affect(hero, MonkEnergy.class).gainEnergy(enemy);
            }
        }
    }
}
