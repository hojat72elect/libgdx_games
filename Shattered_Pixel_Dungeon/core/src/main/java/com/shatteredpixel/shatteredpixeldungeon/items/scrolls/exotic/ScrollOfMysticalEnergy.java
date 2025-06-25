

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class ScrollOfMysticalEnergy extends ExoticScroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_MYSTENRG;
    }

    @Override
    public void doRead() {

        detach(curUser.belongings.backpack);
        //append buff
        Buff.affect(curUser, ArtifactRecharge.class).set(30).ignoreHornOfPlenty = false;

        Sample.INSTANCE.play(Assets.Sounds.READ);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);

        SpellSprite.show(curUser, SpellSprite.CHARGE, 0, 1, 1);
        identify();
        ScrollOfRecharging.charge(curUser);

        readAnimation();
    }
}
