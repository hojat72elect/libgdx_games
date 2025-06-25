

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.WebParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class Web extends Blob {

    {
        //acts before the hero, to ensure terrain is adjusted correctly
        actPriority = HERO_PRIO + 1;
    }

    @Override
    protected void evolve() {

        int cell;

        Level l = Dungeon.level;
        for (int i = area.left; i < area.right; i++) {
            for (int j = area.top; j < area.bottom; j++) {
                cell = i + j * l.width();
                off[cell] = cur[cell] > 0 ? cur[cell] - 1 : 0;

                volume += off[cell];

                l.solid[cell] = off[cell] > 0 || (Terrain.flags[l.map[cell]] & Terrain.SOLID) != 0;
                l.flamable[cell] = off[cell] > 0 || (Terrain.flags[l.map[cell]] & Terrain.FLAMABLE) != 0;
            }
        }
    }

    @Override
    public void seed(Level level, int cell, int amount) {
        super.seed(level, cell, amount);
        level.solid[cell] = cur[cell] > 0 || (Terrain.flags[level.map[cell]] & Terrain.SOLID) != 0;
        level.flamable[cell] = cur[cell] > 0 || (Terrain.flags[level.map[cell]] & Terrain.FLAMABLE) != 0;
    }

    //affects characters as they step on it. See Level.OccupyCell and Level.PressCell
    public static void affectChar(Char ch) {
        Buff.prolong(ch, Roots.class, Roots.DURATION);
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);

        emitter.pour(WebParticle.FACTORY, 0.25f);
    }

    @Override
    public void clear(int cell) {
        super.clear(cell);
        if (cur == null) return;
        Level l = Dungeon.level;
        l.solid[cell] = cur[cell] > 0 || (Terrain.flags[l.map[cell]] & Terrain.SOLID) != 0;
        l.flamable[cell] = cur[cell] > 0 || (Terrain.flags[l.map[cell]] & Terrain.FLAMABLE) != 0;
    }

    @Override
    public void fullyClear() {
        super.fullyClear();
        Dungeon.level.buildFlagMaps();
    }

    @Override
    public void onBuildFlagMaps(Level l) {
        if (volume > 0) {
            for (int i = 0; i < l.length(); i++) {
                l.solid[i] = l.solid[i] || cur[i] > 0;
                l.flamable[i] = l.flamable[i] || cur[i] > 0;
            }
        }
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}
