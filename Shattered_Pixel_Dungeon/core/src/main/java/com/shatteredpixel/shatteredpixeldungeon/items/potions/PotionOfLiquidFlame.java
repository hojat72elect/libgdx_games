

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class PotionOfLiquidFlame extends Potion {

    {
        icon = ItemSpriteSheet.Icons.POTION_LIQFLAME;
    }

    @Override
    public void shatter(int cell) {

        splash(cell);
        if (Dungeon.level.heroFOV[cell]) {
            identify();

            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

        for (int offset : PathFinder.NEIGHBOURS9) {
            if (!Dungeon.level.solid[cell + offset]) {

                GameScene.add(Blob.seed(cell + offset, 2, Fire.class));
            }
        }
    }

    @Override
    public int value() {
        return isKnown() ? 30 * quantity : super.value();
    }
}
