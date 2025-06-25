

package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ShrapnelBomb extends Bomb {

    {
        image = ItemSpriteSheet.SHRAPNEL_BOMB;
    }

    @Override
    public boolean explodesDestructively() {
        return false;
    }

    @Override
    protected int explosionRange() {
        return 8;
    }

    @Override
    public void explode(int cell) {
        super.explode(cell);

        boolean[] FOV = new boolean[Dungeon.level.length()];
        Point c = Dungeon.level.cellToPoint(cell);
        ShadowCaster.castShadow(c.x, c.y, Dungeon.level.width(), FOV, Dungeon.level.losBlocking, explosionRange());

        ArrayList<Char> affected = new ArrayList<>();

        for (int i = 0; i < FOV.length; i++) {
            if (FOV[i]) {
                if (Dungeon.level.heroFOV[i] && !Dungeon.level.solid[i]) {
                    CellEmitter.center(i).burst(BlastParticle.FACTORY, 5);
                }
                Char ch = Actor.findChar(i);
                if (ch != null) {
                    affected.add(ch);
                }
            }
        }

        for (Char ch : affected) {
            //regular bomb damage over an FOV up to 8-range
            int damage = Random.NormalIntRange(4 + Dungeon.scalingDepth(), 12 + 3 * Dungeon.scalingDepth());
            damage -= ch.drRoll();
            ch.damage(damage, this);
            if (ch == Dungeon.hero && !ch.isAlive()) {
                Dungeon.fail(this);
            }
        }
    }

    @Override
    public int value() {
        //prices of ingredients
        return quantity * (20 + 50);
    }
}
