

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;

public class ScrollOfPassage extends ExoticScroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_PASSAGE;
    }

    @Override
    public void doRead() {

        detach(curUser.belongings.backpack);
        identify();
        readAnimation();

        if (!Dungeon.interfloorTeleportAllowed()) {

            GLog.w(Messages.get(ScrollOfTeleportation.class, "no_tele"));
            return;
        }

        Level.beforeTransition();
        InterlevelScene.mode = InterlevelScene.Mode.RETURN;
        InterlevelScene.returnDepth = Math.max(1, (Dungeon.depth - 1 - (Dungeon.depth - 2) % 5));
        InterlevelScene.returnBranch = 0;
        InterlevelScene.returnPos = -1;
        Game.switchScene(InterlevelScene.class);
    }
}
