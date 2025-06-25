

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FungalSentrySprite;
import com.watabou.utils.Random;

public class FungalSentry extends Mob {

    {
        spriteClass = FungalSentrySprite.class;

        HP = HT = 200;
        defenseSkill = 12;

        EXP = 10;
        maxLvl = -2;

        state = WANDERING = new Waiting();

        properties.add(Property.IMMOVABLE);
        properties.add(Property.MINIBOSS);
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public float spawningWeight() {
        return 0;
    }

    @Override
    protected boolean getCloser(int target) {
        return false;
    }

    @Override
    protected boolean getFurther(int target) {
        return false;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(5, 10);
    }

    @Override
    //TODO attack is a little permissive atm?
    protected boolean canAttack(Char enemy) {
        return super.canAttack(enemy)
                || new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    //TODO if we want to allow them to be literally killed, probably should give them a heal if hero is out of FOV, or similar

    @Override
    public int attackProc(Char enemy, int damage) {
        Buff.affect(enemy, Poison.class).extend(6);
        return super.attackProc(enemy, damage);
    }

    @Override
    public int attackSkill(Char target) {
        return 50;
    }

    {
        immunities.add(ToxicGas.class);
        immunities.add(Poison.class);
    }

    private class Waiting extends Mob.Wandering {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            //always notices the hero
            if (enemyInFOV) {

                return noticeEnemy();
            } else {

                return continueWandering();
            }
        }

        @Override
        protected boolean noticeEnemy() {
            spend(TICK);
            return super.noticeEnemy();
        }
    }
}
