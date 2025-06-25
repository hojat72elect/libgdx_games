

package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class StoneOfBlink extends Runestone {

    {
        image = ItemSpriteSheet.STONE_BLINK;
    }

    private static Ballistica throwPath;

    @Override
    public int throwPos(Hero user, int dst) {
        throwPath = new Ballistica(user.pos, dst, Ballistica.PROJECTILE);
        return throwPath.collisionPos;
    }

    @Override
    protected void onThrow(int cell) {
        if (Actor.findChar(cell) != null && throwPath.dist >= 1) {
            cell = throwPath.path.get(throwPath.dist - 1);
        }
        throwPath = null;
        super.onThrow(cell);
    }

    @Override
    protected void activate(int cell) {
        ScrollOfTeleportation.teleportToLocation(curUser, cell);
    }
}
