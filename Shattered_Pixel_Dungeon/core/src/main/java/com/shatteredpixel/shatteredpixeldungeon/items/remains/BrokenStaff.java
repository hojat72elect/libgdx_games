

package com.shatteredpixel.shatteredpixeldungeon.items.remains;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class BrokenStaff extends RemainsItem {

    {
        image = ItemSpriteSheet.BROKEN_STAFF;
    }

    @Override
    protected void doEffect(Hero hero) {
        hero.belongings.charge(1f);
        ScrollOfRecharging.charge(hero);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
    }
}
