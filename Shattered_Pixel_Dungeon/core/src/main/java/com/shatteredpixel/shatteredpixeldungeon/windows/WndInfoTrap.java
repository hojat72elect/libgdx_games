

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.TerrainFeaturesTilemap;

public class WndInfoTrap extends WndTitledMessage {

    public WndInfoTrap(Trap trap) {

        super(TerrainFeaturesTilemap.tile(trap.pos, Dungeon.level.map[trap.pos]),
                Messages.titleCase(trap.name()),
                (!trap.active ? Messages.get(WndInfoTrap.class, "inactive") + "\n\n" : "") + trap.desc());
    }
}
