

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TenguDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class ForceCube extends MissileWeapon {

    {
        image = ItemSpriteSheet.FORCE_CUBE;

        tier = 5;
        baseUses = 5;

        sticky = false;
    }

    @Override
    public void hitSound(float pitch) {
        //no hitsound as it never hits enemies directly
    }

    @Override
    protected void onThrow(int cell) {
        if (Dungeon.level.pit[cell]) {
            super.onThrow(cell);
            return;
        }

        rangedHit(null, cell);
        Dungeon.level.pressCell(cell);

        ArrayList<Char> targets = new ArrayList<>();
        if (Actor.findChar(cell) != null) targets.add(Actor.findChar(cell));

        for (int i : PathFinder.NEIGHBOURS8) {
            if (!(Dungeon.level.traps.get(cell + i) instanceof TenguDartTrap)) Dungeon.level.pressCell(cell + i);
            if (Actor.findChar(cell + i) != null) targets.add(Actor.findChar(cell + i));
        }

        for (Char target : targets) {
            curUser.shoot(target, this);
            if (target == Dungeon.hero && !target.isAlive()) {
                Badges.validateDeathFromFriendlyMagic();
                Dungeon.fail(this);
                GLog.n(Messages.get(this, "ondeath"));
            }
        }

        WandOfBlastWave.BlastWave.blast(cell);
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
    }
}
