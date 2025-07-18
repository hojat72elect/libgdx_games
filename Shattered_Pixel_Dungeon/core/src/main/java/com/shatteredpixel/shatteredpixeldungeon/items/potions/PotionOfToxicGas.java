

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class PotionOfToxicGas extends Potion {

    {
        icon = ItemSpriteSheet.Icons.POTION_TOXICGAS;
    }

    @Override
    public void shatter(int cell) {

        splash(cell);
        if (Dungeon.level.heroFOV[cell]) {
            identify();

            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
            Sample.INSTANCE.play(Assets.Sounds.GAS);
        }

        GameScene.add(Blob.seed(cell, 1000, ToxicGas.class));
    }

    @Override
    public int value() {
        return isKnown() ? 30 * quantity : super.value();
    }
}
