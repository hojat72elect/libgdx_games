

package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.CorrosionParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfCorrosion extends Wand {

    {
        image = ItemSpriteSheet.WAND_CORROSION;

        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_SOLID;
    }

    @Override
    public void onZap(Ballistica bolt) {
        CorrosiveGas gas = Blob.seed(bolt.collisionPos, 50 + 10 * buffedLvl(), CorrosiveGas.class);
        CellEmitter.get(bolt.collisionPos).burst(Speck.factory(Speck.CORROSION), 10);
        gas.setStrength(2 + buffedLvl(), getClass());
        GameScene.add(gas);
        Sample.INSTANCE.play(Assets.Sounds.GAS);

        for (int i : PathFinder.NEIGHBOURS9) {
            Char ch = Actor.findChar(bolt.collisionPos + i);
            if (ch != null) {
                wandProc(ch, chargesPerCast());

                if (i == 0 && ch instanceof DwarfKing) {
                    Statistics.qualifiedForBossChallengeBadge = false;
                }
            }
        }

        if (Actor.findChar(bolt.collisionPos) == null) {
            Dungeon.level.pressCell(bolt.collisionPos);
        }
    }

    @Override
    public void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar(
                curUser.sprite.parent,
                MagicMissile.CORROSION,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        int level = Math.max(0, buffedLvl());

        // lvl 0 - 33%
        // lvl 1 - 50%
        // lvl 2 - 60%
        float procChance = (level + 1f) / (level + 3f) * procChanceMultiplier(attacker);
        if (Random.Float() < procChance) {

            float powerMulti = Math.max(1f, procChance);

            Buff.affect(defender, Ooze.class).set(Ooze.DURATION * powerMulti);
            CellEmitter.center(defender.pos).burst(CorrosionParticle.SPLASH, 5);
        }
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(ColorMath.random(0xAAAAAA, 0xFF8800));
        particle.am = 0.6f;
        particle.setLifespan(1f);
        particle.acc.set(0, 20);
        particle.setSize(0.5f, 3f);
        particle.shuffleXY(1f);
    }

    @Override
    public String statsDesc() {
        if (levelKnown)
            return Messages.get(this, "stats_desc", 2 + buffedLvl());
        else
            return Messages.get(this, "stats_desc", 2);
    }

    @Override
    public String upgradeStat1(int level) {
        return Integer.toString(level + 2);
    }

    @Override
    public String upgradeStat2(int level) {
        return Messages.decimalFormat("#.##x", 1 + .2f * level);
    }
}
