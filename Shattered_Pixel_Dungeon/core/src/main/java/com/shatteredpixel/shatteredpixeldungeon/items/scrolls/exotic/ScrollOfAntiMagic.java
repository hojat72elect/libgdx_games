

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ScrollOfAntiMagic extends ExoticScroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_ANTIMAGIC;
    }

    @Override
    public void doRead() {

        detach(curUser.belongings.backpack);
        Buff.affect(curUser, MagicImmune.class, MagicImmune.DURATION);
        new Flare(5, 32).color(0x00FF00, true).show(curUser.sprite, 2f);

        identify();

        readAnimation();
    }
}
