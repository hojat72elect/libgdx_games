

package com.shatteredpixel.shatteredpixeldungeon.items.remains;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class SealShard extends RemainsItem {

    {
        image = ItemSpriteSheet.SEAL_SHARD;
    }

    @Override
    protected void doEffect(Hero hero) {
        Buff.affect(hero, Barrier.class).incShield(Math.round(hero.HT / 5f));
        hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(Math.round(hero.HT / 5f)), FloatingText.SHIELDING);
        Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
    }
}
