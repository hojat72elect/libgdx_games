

package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Regrowth;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RegrowthBomb extends Bomb {

    {
        image = ItemSpriteSheet.REGROWTH_BOMB;
    }

    @Override
    public boolean explodesDestructively() {
        return false;
    }

    @Override
    protected int explosionRange() {
        return 3;
    }

    @Override
    public void explode(int cell) {
        super.explode(cell);

        if (Dungeon.level.heroFOV[cell]) {
            Splash.at(cell, 0x00FF00, 30);
        }

        ArrayList<Integer> plantCandidates = new ArrayList<>();

        PathFinder.buildDistanceMap(cell, BArray.not(Dungeon.level.solid, null), explosionRange());
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                Char ch = Actor.findChar(i);
                int t = Dungeon.level.map[i];
                if (ch != null) {
                    if (ch.alignment == Dungeon.hero.alignment) {
                        //same as a healing potion
                        PotionOfHealing.cure(ch);
                        PotionOfHealing.heal(ch);
                    }
                } else if ((t == Terrain.EMPTY || t == Terrain.EMPTY_DECO || t == Terrain.EMBERS
                        || t == Terrain.GRASS || t == Terrain.FURROWED_GRASS || t == Terrain.HIGH_GRASS)
                        && Dungeon.level.plants.get(i) == null) {
                    plantCandidates.add(i);
                }
                GameScene.add(Blob.seed(i, 10, Regrowth.class));
            }
        }

        int plants = Random.chances(new float[]{0, 0, 2, 1});

        for (int i = 0; i < plants; i++) {
            Integer plantPos = Random.element(plantCandidates);
            if (plantPos != null) {
                Dungeon.level.plant((Plant.Seed) Generator.randomUsingDefaults(Generator.Category.SEED), plantPos);
                plantCandidates.remove(plantPos);
            }
        }

        Integer plantPos = Random.element(plantCandidates);
        if (plantPos != null) {
            Plant.Seed plant;
            switch (Random.chances(new float[]{0, 6, 3, 1})) {
                case 1:
                default:
                    plant = new WandOfRegrowth.Dewcatcher.Seed();
                    break;
                case 2:
                    plant = new WandOfRegrowth.Seedpod.Seed();
                    break;
                case 3:
                    plant = new Starflower.Seed();
                    break;
            }
            Dungeon.level.plant(plant, plantPos);
        }
    }

    @Override
    public int value() {
        //prices of ingredients
        return quantity * (20 + 30);
    }
}
