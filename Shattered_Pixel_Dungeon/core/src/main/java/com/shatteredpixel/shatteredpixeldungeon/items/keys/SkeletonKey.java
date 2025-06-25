

package com.shatteredpixel.shatteredpixeldungeon.items.keys;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSupportPrompt;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

import java.io.IOException;

public class SkeletonKey extends Key {

    {
        image = ItemSpriteSheet.SKELETON_KEY;
    }

    public SkeletonKey() {
        this(0);
    }

    public SkeletonKey(int depth) {
        super();
        this.depth = depth;
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {
        if (!SPDSettings.supportNagged()) {
            try {
                Dungeon.saveAll();
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        ShatteredPixelDungeon.scene().add(new WndSupportPrompt());
                    }
                });
            } catch (IOException e) {
                ShatteredPixelDungeon.reportException(e);
            }
        }

        return super.doPickUp(hero, pos);
    }
}
