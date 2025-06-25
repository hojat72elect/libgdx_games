

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfMagicMapping extends Scroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_MAGICMAP;
    }

    @Override
    public void doRead() {

        detach(curUser.belongings.backpack);
        int length = Dungeon.level.length();
        int[] map = Dungeon.level.map;
        boolean[] mapped = Dungeon.level.mapped;
        boolean[] discoverable = Dungeon.level.discoverable;

        boolean noticed = false;

        for (int i = 0; i < length; i++) {

            int terr = map[i];

            if (discoverable[i]) {

                mapped[i] = true;
                if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                    Dungeon.level.discover(i);

                    if (Dungeon.level.heroFOV[i]) {
                        GameScene.discoverTile(i, terr);
                        discover(i);

                        noticed = true;
                    }
                }
            }
        }
        GameScene.updateFog();

        GLog.i(Messages.get(this, "layout"));
        if (noticed) {
            Sample.INSTANCE.play(Assets.Sounds.SECRET);
        }

        SpellSprite.show(curUser, SpellSprite.MAP);
        Sample.INSTANCE.play(Assets.Sounds.READ);

        identify();

        readAnimation();
    }

    @Override
    public int value() {
        return isKnown() ? 40 * quantity : super.value();
    }

    public static void discover(int cell) {
        CellEmitter.get(cell).start(Speck.factory(Speck.DISCOVER), 0.1f, 4);
    }
}
