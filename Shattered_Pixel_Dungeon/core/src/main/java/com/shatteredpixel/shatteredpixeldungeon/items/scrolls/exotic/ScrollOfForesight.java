

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Foresight;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class ScrollOfForesight extends ExoticScroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_FORESIGHT;
    }

    @Override
    public void doRead() {

        detach(curUser.belongings.backpack);
        Sample.INSTANCE.play(Assets.Sounds.READ);

        Buff.affect(curUser, Foresight.class, Foresight.DURATION);

        identify();

        readAnimation();
    }
}
