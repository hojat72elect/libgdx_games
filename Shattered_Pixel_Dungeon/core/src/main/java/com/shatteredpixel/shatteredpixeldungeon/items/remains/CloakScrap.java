

package com.shatteredpixel.shatteredpixeldungeon.items.remains;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class CloakScrap extends RemainsItem {

    {
        image = ItemSpriteSheet.CLOAK_SCRAP;
    }

    @Override
    protected void doEffect(Hero hero) {
        ArtifactRecharge.chargeArtifacts(hero, 4f);
        ScrollOfRecharging.charge(hero);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
    }
}
