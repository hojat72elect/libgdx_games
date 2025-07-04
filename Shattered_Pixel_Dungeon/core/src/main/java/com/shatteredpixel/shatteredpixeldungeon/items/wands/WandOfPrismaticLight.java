

package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfPrismaticLight extends DamageWand {

    {
        image = ItemSpriteSheet.WAND_PRISMATIC_LIGHT;

        collisionProperties = Ballistica.MAGIC_BOLT;
    }

    public int min(int lvl) {
        return 1 + lvl;
    }

    public int max(int lvl) {
        return 5 + 3 * lvl;
    }

    @Override
    public void onZap(Ballistica beam) {
        affectMap(beam);

        if (Dungeon.level.viewDistance < 6) {
            if (Dungeon.isChallenged(Challenges.DARKNESS)) {
                Buff.prolong(curUser, Light.class, 2f + buffedLvl());
            } else {
                Buff.prolong(curUser, Light.class, 10f + buffedLvl() * 5);
            }
        }

        Char ch = Actor.findChar(beam.collisionPos);
        if (ch != null) {
            wandProc(ch, chargesPerCast());
            affectTarget(ch);
        }
    }

    private void affectTarget(Char ch) {
        int dmg = damageRoll();

        //three in (5+lvl) chance of failing
        if (Random.Int(5 + buffedLvl()) >= 3) {
            Buff.prolong(ch, Blindness.class, 2f + (buffedLvl() * 0.333f));
            ch.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 6);
        }

        if (ch.properties().contains(Char.Property.DEMONIC) || ch.properties().contains(Char.Property.UNDEAD)) {
            ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10 + buffedLvl());
            Sample.INSTANCE.play(Assets.Sounds.BURNING);

            ch.damage(Math.round(dmg * 1.333f), this);
        } else {
            ch.sprite.centerEmitter().burst(RainbowParticle.BURST, 10 + buffedLvl());

            ch.damage(dmg, this);
        }
    }

    private void affectMap(Ballistica beam) {
        boolean noticed = false;
        for (int c : beam.subPath(0, beam.dist)) {
            if (!Dungeon.level.insideMap(c)) {
                continue;
            }
            for (int n : PathFinder.NEIGHBOURS9) {
                int cell = c + n;

                if (Dungeon.level.discoverable[cell])
                    Dungeon.level.mapped[cell] = true;

                int terr = Dungeon.level.map[cell];
                if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                    Dungeon.level.discover(cell);

                    GameScene.discoverTile(cell, terr);
                    ScrollOfMagicMapping.discover(cell);

                    noticed = true;
                }
            }

            CellEmitter.center(c).burst(RainbowParticle.BURST, Random.IntRange(1, 2));
        }
        if (noticed)
            Sample.INSTANCE.play(Assets.Sounds.SECRET);

        GameScene.updateFog();
    }

    @Override
    public String upgradeStat2(int level) {
        return Messages.decimalFormat("#", 100 * (1 - (3 / (float) (5 + level)))) + "%";
    }

    @Override
    public String upgradeStat3(int level) {
        if (Dungeon.isChallenged(Challenges.DARKNESS)) {
            return Integer.toString(2 + level);
        } else {
            return Integer.toString(10 + 5 * level);
        }
    }

    @Override
    public void fx(Ballistica beam, Callback callback) {
        curUser.sprite.parent.add(
                new Beam.LightRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
        callback.call();
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        //cripples enemy
        Buff.prolong(defender, Cripple.class, Math.round((1 + staff.buffedLvl()) * procChanceMultiplier(attacker)));
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(Random.Int(0x1000000));
        particle.am = 0.5f;
        particle.setLifespan(1f);
        particle.speed.polar(Random.Float(PointF.PI2), 2f);
        particle.setSize(1f, 2f);
        particle.radiateXY(0.5f);
    }
}
