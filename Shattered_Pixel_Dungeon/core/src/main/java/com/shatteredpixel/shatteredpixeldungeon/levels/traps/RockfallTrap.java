

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RockfallTrap extends Trap {

    {
        color = GREY;
        shape = DIAMOND;

        canBeHidden = false;
        avoidsHallways = true;
    }

    @Override
    public void activate() {

        ArrayList<Integer> rockCells = new ArrayList<>();

        //determines if the trap is actually in the world, or if it is being spawned for its effect
        boolean onGround = Dungeon.level.traps.get(pos) == this;
        Room r = null;
        if (Dungeon.level instanceof RegularLevel) {
            r = ((RegularLevel) Dungeon.level).room(pos);
        }

        if (onGround && r != null) {
            int cell;
            for (Point p : r.getPoints()) {
                cell = Dungeon.level.pointToCell(p);
                if (!Dungeon.level.solid[cell]) {
                    rockCells.add(cell);
                }
            }

            //if we don't have a room, then just do 5x5
        } else {
            PathFinder.buildDistanceMap(pos, BArray.not(Dungeon.level.solid, null), 2);
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                    rockCells.add(i);
                }
            }
        }

        boolean seen = false;
        for (int cell : rockCells) {

            if (Dungeon.level.heroFOV[cell]) {
                CellEmitter.get(cell - Dungeon.level.width()).start(Speck.factory(Speck.ROCK), 0.07f, 10);
                seen = true;
            }

            Char ch = Actor.findChar(cell);

            if (ch != null && ch.isAlive()) {
                if (ch instanceof Mob) {
                    Buff.prolong(ch, Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
                }
                int damage = Random.NormalIntRange(5 + scalingDepth(), 10 + scalingDepth() * 2);
                damage -= ch.drRoll();
                ch.damage(Math.max(damage, 0), this);

                if (ch.isActive()) {
                    Buff.prolong(ch, Paralysis.class, Paralysis.DURATION);
                } else if (!ch.isAlive() && ch == Dungeon.hero) {
                    Dungeon.fail(this);
                    GLog.n(Messages.get(this, "ondeath"));
                    if (reclaimed) Badges.validateDeathFromFriendlyMagic();
                }
            }
        }

        if (seen) {
            PixelScene.shake(3, 0.7f);
            Sample.INSTANCE.play(Assets.Sounds.ROCKS);
        }
    }
}
