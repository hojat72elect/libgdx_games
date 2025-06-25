

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.plants.BlandfruitBush;
import com.watabou.utils.Point;

public class SecretLarderRoom extends SecretRoom {

    @Override
    public int minHeight() {
        return 6;
    }

    @Override
    public int minWidth() {
        return 6;
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY_SP);

        Point c = center();

        Painter.fill(level, c.x - 1, c.y - 1, 3, 3, Terrain.WATER);
        Painter.set(level, c, Terrain.GRASS);

        level.plant(new BlandfruitBush.Seed(), level.pointToCell(c));

        int extraFood = (int) (Hunger.STARVING - Hunger.HUNGRY) * (1 + Dungeon.depth / 5);

        while (extraFood > 0) {
            Food food;
            if (extraFood >= Hunger.STARVING) {
                food = new Pasty();
                extraFood -= Hunger.STARVING;
            } else {
                food = new ChargrilledMeat();
                extraFood -= (Hunger.STARVING - Hunger.HUNGRY);
            }
            int foodPos;
            do {
                foodPos = level.pointToCell(random());
            } while (level.map[foodPos] != Terrain.EMPTY_SP || level.heaps.get(foodPos) != null);
            level.drop(food, foodPos);
        }

        entrance().set(Door.Type.HIDDEN);
    }
}
