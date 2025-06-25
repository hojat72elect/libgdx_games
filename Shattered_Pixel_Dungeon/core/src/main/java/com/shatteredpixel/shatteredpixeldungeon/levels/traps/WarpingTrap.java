

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.BArray;

public class WarpingTrap extends TeleportationTrap {

    {
        color = TEAL;
        shape = STARS;
    }

    @Override
    public void activate() {
        if (Dungeon.level.distance(Dungeon.hero.pos, pos) <= 1) {
            BArray.setFalse(Dungeon.level.visited);
            BArray.setFalse(Dungeon.level.mapped);
        }

        super.activate();

        GameScene.updateFog(); //just in case hero wasn't moved
        Dungeon.observe();
    }
}
