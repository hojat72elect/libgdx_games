

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class IncendiaryDart extends TippedDart {

    {
        image = ItemSpriteSheet.INCENDIARY_DART;
    }

    @Override
    protected void onThrow(int cell) {
        Char enemy = Actor.findChar(cell);
        if ((enemy == null || enemy == curUser) && Dungeon.level.flamable[cell]) {
            GameScene.add(Blob.seed(cell, 4, Fire.class));
            decrementDurability();
            if (durability > 0 || spawnedForEffect) {
                super.onThrow(cell);
            } else {
                Dungeon.level.drop(new Dart(), cell).sprite.drop();
            }
        } else {
            super.onThrow(cell);
        }
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        //when processing charged shot, only burn enemies
        if (!processingChargedShot || attacker.alignment != defender.alignment) {
            Buff.affect(defender, Burning.class).reignite(defender);
        }
        return super.proc(attacker, defender, damage);
    }
}
