

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SheepSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Sheep extends NPC {

    private static final String[] LINE_KEYS = {"Baa!", "Baa?", "Baa.", "Baa..."};

    {
        spriteClass = SheepSprite.class;
    }

    private float lifespan;

    @Override
    protected boolean act() {
        if (Dungeon.level.heroFOV[pos]) {
            Bestiary.setSeen(getClass());
        }
        HP = 0;

        destroy();
        sprite.die();
        return true;
    }

    public void initialize(float lifespan) {
        this.lifespan = lifespan;
        spend(lifespan + Random.Float(-2, 2));
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage(int dmg, Object src) {
        //do nothing
    }

    @Override
    public boolean add(Buff buff) {
        return false;
    }

    @Override
    public boolean interact(Char c) {
        Bestiary.setSeen(getClass());
        sprite.showStatus(CharSprite.NEUTRAL, Messages.get(this, Random.element(LINE_KEYS)));
        if (c == Dungeon.hero) {
            Dungeon.hero.spendAndNext(1f);
            Sample.INSTANCE.play(Assets.Sounds.SHEEP, 1, Random.Float(0.91f, 1.1f));
            //sheep summoned by woolly bomb can be dispelled by interacting
            if (lifespan >= 20) {
                spend(-cooldown());
            }
        }
        return true;
    }

    private static final String LIFESPAN = "lifespan";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LIFESPAN, lifespan);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        lifespan = bundle.getInt(LIFESPAN);
    }
}