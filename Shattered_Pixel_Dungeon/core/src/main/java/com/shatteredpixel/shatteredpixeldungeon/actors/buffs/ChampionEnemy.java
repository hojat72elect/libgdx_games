

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public abstract class ChampionEnemy extends Buff {

    {
        type = buffType.POSITIVE;
        revivePersists = true;
    }

    protected int color;
    protected int rays;

    @Override
    public int icon() {
        return BuffIndicator.CORRUPT;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(color);
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.aura(color, rays);
        else target.sprite.clearAura();
    }

    public void onAttackProc(Char enemy) {

    }

    public boolean canAttackWithExtraReach(Char enemy) {
        return false;
    }

    public float meleeDamageFactor() {
        return 1f;
    }

    public float damageTakenFactor() {
        return 1f;
    }

    public float evasionAndAccuracyFactor() {
        return 1f;
    }

    {
        immunities.add(AllyBuff.class);
    }

    public static void rollForChampion(Mob m) {
        if (Dungeon.mobsToChampion <= 0) Dungeon.mobsToChampion = 8;

        Dungeon.mobsToChampion--;

        //we roll for a champion enemy even if we aren't spawning one to ensure that
        //mobsToChampion does not affect levelgen RNG (number of calls to Random.Int() is constant)
        Class<? extends ChampionEnemy> buffCls;
        switch (Random.Int(6)) {
            case 0:
            default:
                buffCls = Blazing.class;
                break;
            case 1:
                buffCls = Projecting.class;
                break;
            case 2:
                buffCls = AntiMagic.class;
                break;
            case 3:
                buffCls = Giant.class;
                break;
            case 4:
                buffCls = Blessed.class;
                break;
            case 5:
                buffCls = Growing.class;
                break;
        }

        if (Dungeon.mobsToChampion <= 0 && Dungeon.isChallenged(Challenges.CHAMPION_ENEMIES)) {
            Buff.affect(m, buffCls);
            if (m.state != m.PASSIVE) {
                m.state = m.WANDERING;
            }
        }
    }

    public static class Blazing extends ChampionEnemy {

        {
            color = 0xFF8800;
            rays = 4;
        }

        @Override
        public void onAttackProc(Char enemy) {
            if (!Dungeon.level.water[enemy.pos]) {
                Buff.affect(enemy, Burning.class).reignite(enemy);
            }
        }

        @Override
        public void detach() {
            //don't trigger when killed by being knocked into a pit
            if (target.flying || !Dungeon.level.pit[target.pos]) {
                for (int i : PathFinder.NEIGHBOURS9) {
                    if (!Dungeon.level.solid[target.pos + i] && !Dungeon.level.water[target.pos + i]) {
                        GameScene.add(Blob.seed(target.pos + i, 2, Fire.class));
                    }
                }
            }
            super.detach();
        }

        @Override
        public float meleeDamageFactor() {
            return 1.25f;
        }

        {
            immunities.add(Burning.class);
        }
    }

    public static class Projecting extends ChampionEnemy {

        {
            color = 0x8800FF;
            rays = 4;
        }

        @Override
        public float meleeDamageFactor() {
            return 1.25f;
        }

        @Override
        public boolean canAttackWithExtraReach(Char enemy) {
            if (Dungeon.level.distance(target.pos, enemy.pos) > 4) {
                return false;
            } else {
                boolean[] passable = BArray.not(Dungeon.level.solid, null);
                for (Char ch : Actor.chars()) {
                    //our own tile is always passable
                    passable[ch.pos] = ch == target;
                }

                PathFinder.buildDistanceMap(enemy.pos, passable, 4);

                return PathFinder.distance[target.pos] <= 4;
            }
        }
    }

    public static class AntiMagic extends ChampionEnemy {

        {
            color = 0x00FF00;
            rays = 5;
        }

        @Override
        public float damageTakenFactor() {
            return 0.5f;
        }

        {
            immunities.addAll(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic.RESISTS);
        }
    }

    //Also makes target large, see Char.properties()
    public static class Giant extends ChampionEnemy {

        {
            color = 0x0088FF;
            rays = 5;
        }

        @Override
        public float damageTakenFactor() {
            return 0.2f;
        }

        @Override
        public boolean canAttackWithExtraReach(Char enemy) {
            if (Dungeon.level.distance(target.pos, enemy.pos) > 2) {
                return false;
            } else {
                boolean[] passable = BArray.not(Dungeon.level.solid, null);
                for (Char ch : Actor.chars()) {
                    //our own tile is always passable
                    passable[ch.pos] = ch == target;
                }

                PathFinder.buildDistanceMap(enemy.pos, passable, 2);

                return PathFinder.distance[target.pos] <= 2;
            }
        }
    }

    public static class Blessed extends ChampionEnemy {

        {
            color = 0xFFFF00;
            rays = 6;
        }

        @Override
        public float evasionAndAccuracyFactor() {
            return 4f;
        }
    }

    public static class Growing extends ChampionEnemy {

        {
            color = 0xFF2222; //a little white helps it stick out from background
            rays = 6;
        }

        private float multiplier = 1.19f;

        @Override
        public boolean act() {
            multiplier += 0.01f;
            spend(4 * TICK);
            return true;
        }

        @Override
        public float meleeDamageFactor() {
            return multiplier;
        }

        @Override
        public float damageTakenFactor() {
            return 1f / multiplier;
        }

        @Override
        public float evasionAndAccuracyFactor() {
            return multiplier;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", (int) (100 * (multiplier - 1)), (int) (100 * (1 - 1f / multiplier)));
        }

        private static final String MULTIPLIER = "multiplier";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(MULTIPLIER, multiplier);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            multiplier = bundle.getFloat(MULTIPLIER);
        }
    }
}
