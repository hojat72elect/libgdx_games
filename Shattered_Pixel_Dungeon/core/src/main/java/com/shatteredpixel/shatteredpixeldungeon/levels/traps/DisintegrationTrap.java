

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class DisintegrationTrap extends Trap {

    {
        color = VIOLET;
        shape = CROSSHAIR;

        canBeHidden = false;
        avoidsHallways = true;
    }

    @Override
    public void activate() {
        Char target = Actor.findChar(pos);

        //find the closest char that can be aimed at
        //can't target beyond view distance, with a min of 6 (torch range)
        //add 0.5 for better consistency with vision radius shape
        float range = Math.max(6, Dungeon.level.viewDistance) + 0.5f;
        if (target == null) {
            float closestDist = Float.MAX_VALUE;
            for (Char ch : Actor.chars()) {
                if (!ch.isAlive()) continue;
                float curDist = Dungeon.level.trueDistance(pos, ch.pos);
                //invis targets are considered to be at max range
                if (ch.invisible > 0) curDist = Math.max(curDist, range);
                Ballistica bolt = new Ballistica(pos, ch.pos, Ballistica.PROJECTILE);
                if (bolt.collisionPos == ch.pos
                        && (curDist < closestDist || (curDist == closestDist && target instanceof Hero))) {
                    target = ch;
                    closestDist = curDist;
                }
            }
            if (closestDist > range) {
                target = null;
            }
        }

        Heap heap = Dungeon.level.heaps.get(pos);
        if (heap != null) heap.explode();

        if (target != null) {
            if (target instanceof Mob) {
                Buff.prolong(target, Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
            }
            if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[target.pos]) {
                Sample.INSTANCE.play(Assets.Sounds.RAY);
                ShatteredPixelDungeon.scene().add(new Beam.DeathRay(DungeonTilemap.tileCenterToWorld(pos), target.sprite.center()));
            }
            target.damage(Random.NormalIntRange(30, 50) + scalingDepth(), this);
            if (target == Dungeon.hero) {
                Hero hero = (Hero) target;
                if (!hero.isAlive()) {
                    Badges.validateDeathFromGrimOrDisintTrap();
                    Dungeon.fail(this);
                    GLog.n(Messages.get(this, "ondeath"));
                    if (reclaimed) Badges.validateDeathFromFriendlyMagic();
                }
            }
        }
    }
}
