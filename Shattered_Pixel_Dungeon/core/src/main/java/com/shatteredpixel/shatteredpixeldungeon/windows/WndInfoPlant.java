

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.tiles.TerrainFeaturesTilemap;

public class WndInfoPlant extends WndTitledMessage {

    public WndInfoPlant(Plant plant) {

        super(TerrainFeaturesTilemap.tile(plant.pos, Dungeon.level.map[plant.pos]),
                Messages.titleCase(plant.name()), plant.desc());
    }
}
