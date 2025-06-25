

package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;

public class StoneOfFear extends Runestone {

    {
        image = ItemSpriteSheet.STONE_FEAR;
    }

    @Override
    protected void activate(int cell) {

        Char ch = Actor.findChar(cell);

        if (ch != null && ch.alignment != Char.Alignment.ALLY) {
            Buff.affect(ch, Terror.class, Terror.DURATION).object = curUser.id();
        }

        new Flare(5, 16).color(0xFF0000, true).show(Dungeon.hero.sprite.parent, DungeonTilemap.tileCenterToWorld(cell), 2f);
        Sample.INSTANCE.play(Assets.Sounds.READ);
    }
}
