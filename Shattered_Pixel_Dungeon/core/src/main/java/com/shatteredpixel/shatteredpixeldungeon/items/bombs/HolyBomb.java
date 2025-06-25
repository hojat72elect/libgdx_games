

package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HolyBomb extends Bomb {

    {
        image = ItemSpriteSheet.HOLY_BOMB;
    }

    @Override
    protected int explosionRange() {
        return 2;
    }

    @Override
    public void explode(int cell) {
        super.explode(cell);

        if (Dungeon.level.heroFOV[cell]) {
            new Flare(10, 64).show(Dungeon.hero.sprite.parent, DungeonTilemap.tileCenterToWorld(cell), 2f);
        }

        ArrayList<Char> affected = new ArrayList<>();

        PathFinder.buildDistanceMap(cell, BArray.not(Dungeon.level.solid, null), explosionRange());
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                Char ch = Actor.findChar(i);
                if (ch != null) {
                    affected.add(ch);
                }
            }
        }

        for (Char ch : affected) {
            if (ch.properties().contains(Char.Property.UNDEAD) || ch.properties().contains(Char.Property.DEMONIC)) {
                ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);

                //bomb deals an additional 50% damage to unholy enemies
                int damage = Math.round(Random.NormalIntRange(Dungeon.scalingDepth() + 4, 12 + 3 * Dungeon.scalingDepth()) * 0.5f);
                ch.damage(damage, new HolyDamage());
            }
        }

        Sample.INSTANCE.play(Assets.Sounds.READ);
    }

    public static class HolyDamage {
    }

    @Override
    public int value() {
        //prices of ingredients
        return quantity * (20 + 30);
    }
}
