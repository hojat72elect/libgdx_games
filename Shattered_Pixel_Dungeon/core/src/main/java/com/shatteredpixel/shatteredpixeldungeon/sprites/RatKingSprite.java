

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.utils.Holiday;
import com.watabou.noosa.TextureFilm;

public class RatKingSprite extends MobSprite {

    public boolean festive;

    public RatKingSprite() {
        super();

        resetAnims();
    }

    public void resetAnims() {

        int c;
        switch (Holiday.getCurrentHoliday()) {
            case APRIL_FOOLS:
                c = 8;
                break;
            case WINTER_HOLIDAYS:
                c = 16;
                break;
            default:
                c = 0;
                break;
        }

        if (Dungeon.hero != null && Dungeon.hero.armorAbility instanceof Ratmogrify) {
            c = 24;
            if (parent != null) aura(0xFFFF00, 5);
        }

        texture(Assets.Sprites.RATKING);

        TextureFilm frames = new TextureFilm(texture, 16, 17);

        idle = new Animation(2, true);
        idle.frames(frames, c, c, c, c + 1);

        run = new Animation(10, true);
        run.frames(frames, c + 2, c + 3, c + 4, c + 5, c + 6);

        attack = new Animation(15, false);
        attack.frames(frames, c);

        die = new Animation(10, false);
        die.frames(frames, c);

        play(idle);
    }


    @Override
    public void link(Char ch) {
        super.link(ch);
        if (Dungeon.hero != null && Dungeon.hero.armorAbility instanceof Ratmogrify) {
            aura(0xFFFF00, 5);
        }
    }
}
