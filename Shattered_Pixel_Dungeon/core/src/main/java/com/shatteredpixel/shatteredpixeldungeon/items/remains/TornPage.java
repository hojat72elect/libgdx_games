

package com.shatteredpixel.shatteredpixeldungeon.items.remains;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class TornPage extends RemainsItem {

    {
        image = ItemSpriteSheet.TORN_PAGE;
    }

    @Override
    protected void doEffect(Hero hero) {
        int toHeal = Math.round(hero.HT / 10f);
        hero.HP = Math.min(hero.HP + toHeal, hero.HT);
        hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(toHeal), FloatingText.HEALING);
        Sample.INSTANCE.play(Assets.Sounds.READ);
    }
}
