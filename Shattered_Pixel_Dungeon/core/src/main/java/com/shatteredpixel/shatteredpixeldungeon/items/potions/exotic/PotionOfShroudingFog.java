

package com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class PotionOfShroudingFog extends ExoticPotion {

    {
        icon = ItemSpriteSheet.Icons.POTION_SHROUDFOG;
    }

    @Override
    public void shatter(int cell) {

        splash(cell);
        if (Dungeon.level.heroFOV[cell]) {
            identify();

            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
            Sample.INSTANCE.play(Assets.Sounds.GAS);
        }

        int centerVolume = 180;
        for (int i : PathFinder.NEIGHBOURS8) {
            if (!Dungeon.level.solid[cell + i]) {
                GameScene.add(Blob.seed(cell + i, 180, SmokeScreen.class));
            } else {
                centerVolume += 180;
            }
        }

        GameScene.add(Blob.seed(cell, centerVolume, SmokeScreen.class));
    }
}
