

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthParticle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.List;

//used for various enemy attacks to keep track of rocks that will fall after some number of turns
public class DelayedRockFall extends FlavourBuff {

    private int[] rockPositions;
    private final ArrayList<Emitter> rockEmitters = new ArrayList<>();

    public void setRockPositions(List<Integer> rockPositions) {
        this.rockPositions = new int[rockPositions.size()];
        for (int i = 0; i < rockPositions.size(); i++) {
            this.rockPositions[i] = rockPositions.get(i);
        }

        fx(true);
    }

    @Override
    public boolean act() {
        for (int i : rockPositions) {
            CellEmitter.get(i).start(Speck.factory(Speck.ROCK), 0.07f, 10);

            Char ch = Actor.findChar(i);
            if (ch != null) {
                affectChar(ch);
            } else {
                affectCell(i);
            }
        }

        PixelScene.shake(3, 0.7f);
        Sample.INSTANCE.play(Assets.Sounds.ROCKS);

        detach();
        return super.act();
    }

    public void affectChar(Char ch) {
        //do nothing by default
    }

    public void affectCell(int cell) {
        //do nothing by default
    }

    @Override
    public void fx(boolean on) {
        if (on && rockPositions != null) {
            for (int i : this.rockPositions) {
                Emitter e = CellEmitter.get(i);
                e.y -= DungeonTilemap.SIZE * 0.2f;
                e.height *= 0.4f;
                e.pour(EarthParticle.FALLING, 0.1f);
                rockEmitters.add(e);
            }
        } else {
            for (Emitter e : rockEmitters) {
                e.on = false;
            }
        }
    }

    private static final String POSITIONS = "positions";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(POSITIONS, rockPositions);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        rockPositions = bundle.getIntArray(POSITIONS);
    }
}
