

package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AdrenalineSurge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Rotberry extends Plant {

    {
        image = 0;
        seedClass = Seed.class;
    }

    @Override
    public void activate(Char ch) {
        if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN) {
            Buff.affect(ch, AdrenalineSurge.class).reset(1, AdrenalineSurge.DURATION);
        } else {
            GameScene.add(Blob.seed(pos, 100, ToxicGas.class));
        }
    }

    @Override
    public void wither() {
        Dungeon.level.uproot(pos);

        if (Dungeon.level.heroFOV[pos]) {
            CellEmitter.get(pos).burst(LeafParticle.GENERAL, 6);
        }

        //seed always drops, no lotus benefit
        Dungeon.level.drop(new Seed(), pos).sprite.drop();
    }

    public static class Seed extends Plant.Seed {
        {
            image = ItemSpriteSheet.SEED_ROTBERRY;

            plantClass = Rotberry.class;

            unique = true;
        }

        @Override
        public int value() {
            return 30 * quantity;
        }

        @Override
        public int energyVal() {
            return 3 * quantity;
        }
    }
}
