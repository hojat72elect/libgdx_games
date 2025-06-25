

package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class FlashBangBomb extends Bomb {

    {
        image = ItemSpriteSheet.FLASHBANG;
    }

    @Override
    protected int explosionRange() {
        return 2;
    }

    @Override
    public void explode(int cell) {
        super.explode(cell);

        ArrayList<Char> affected = new ArrayList<>();
        PathFinder.buildDistanceMap(cell, BArray.not(Dungeon.level.solid, null), explosionRange());
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE && Actor.findChar(i) != null) {
                affected.add(Actor.findChar(i));
            }
        }

        ArrayList<Lightning.Arc> arcs = new ArrayList<>();
        for (Char ch : affected) {
            //25% bonus damage and 10 turns of stun
            int damage = Math.round(Random.NormalIntRange(4 + Dungeon.scalingDepth(), 12 + 3 * Dungeon.scalingDepth()) / 4f);
            ch.damage(damage, new Electricity());
            if (ch.isAlive()) Buff.prolong(ch, Paralysis.class, Paralysis.DURATION);
            arcs.add(new Lightning.Arc(DungeonTilemap.tileCenterToWorld(cell), ch.sprite.center()));

            if (ch == Dungeon.hero) {
                GameScene.flash(0x80FFFFFF);
            }

            if (ch == Dungeon.hero && !ch.isAlive()) {
                Badges.validateDeathFromFriendlyMagic();
                GLog.n(Messages.get(this, "ondeath"));
                Dungeon.fail(this);
            }
        }

        CellEmitter.center(cell).burst(SparkParticle.FACTORY, 20);
        Dungeon.hero.sprite.parent.addToFront(new Lightning(arcs, null));
        Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
    }

    @Override
    public int value() {
        //prices of ingredients
        return quantity * (20 + 30);
    }
}
