

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MonkSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Monk extends Mob {

    {
        spriteClass = MonkSprite.class;

        HP = HT = 70;
        defenseSkill = 30;

        EXP = 11;
        maxLvl = 21;

        loot = Food.class;
        lootChance = 0.083f;

        properties.add(Property.UNDEAD);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(12, 25);
    }

    @Override
    public int attackSkill(Char target) {
        return 30;
    }

    @Override
    public float attackDelay() {
        return super.attackDelay() * 0.5f;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 2);
    }

    @Override
    public void rollToDropLoot() {
        Imp.Quest.process(this);

        super.rollToDropLoot();
    }

    protected float focusCooldown = 0;

    @Override
    protected boolean act() {
        boolean result = super.act();
        if (buff(Focus.class) == null && state == HUNTING && focusCooldown <= 0) {
            Buff.affect(this, Focus.class);
        }
        return result;
    }

    @Override
    protected void spend(float time) {
        focusCooldown -= time;
        super.spend(time);
    }

    @Override
    public void move(int step, boolean travelling) {
        // moving reduces cooldown by an additional 0.67, giving a total reduction of 1.67f.
        // basically monks will become focused notably faster if you kite them.
        if (travelling) focusCooldown -= 0.67f;
        super.move(step, travelling);
    }

    @Override
    public int defenseSkill(Char enemy) {
        if (buff(Focus.class) != null && paralysed == 0 && state != SLEEPING) {
            return INFINITE_EVASION;
        }
        return super.defenseSkill(enemy);
    }

    @Override
    public String defenseVerb() {
        Focus f = buff(Focus.class);
        if (f == null) {
            return super.defenseVerb();
        } else {
            f.detach();
            if (sprite != null && sprite.visible) {
                Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
            }
            focusCooldown = Random.NormalFloat(6, 7);
            return Messages.get(this, "parried");
        }
    }

    private static final String FOCUS_COOLDOWN = "focus_cooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FOCUS_COOLDOWN, focusCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        focusCooldown = bundle.getInt(FOCUS_COOLDOWN);
    }

    public static class Focus extends Buff {

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.MIND_VISION;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0.25f, 1.5f, 1f);
        }
    }
}
