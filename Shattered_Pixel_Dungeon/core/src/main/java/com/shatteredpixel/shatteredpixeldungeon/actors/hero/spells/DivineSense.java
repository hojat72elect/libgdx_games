

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

public class DivineSense extends ClericSpell {

    public static final DivineSense INSTANCE = new DivineSense();

    @Override
    public int icon() {
        return HeroIcon.DIVINE_SENSE;
    }

    @Override
    public float chargeUse(Hero hero) {
        return 2;
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.hasTalent(Talent.DIVINE_SENSE);
    }

    @Override
    public void onCast(HolyTome tome, Hero hero) {
        Buff.prolong(hero, DivineSenseTracker.class, 30f);
        Dungeon.observe();

        Sample.INSTANCE.play(Assets.Sounds.READ);

        SpellSprite.show(hero, SpellSprite.VISION);
        hero.sprite.operate(hero.pos);

        Char ally = PowerOfMany.getPoweredAlly();
        if (ally != null && ally.buff(LifeLinkSpell.LifeLinkSpellBuff.class) != null) {
            Buff.prolong(ally, DivineSenseTracker.class, 30f);
            SpellSprite.show(ally, SpellSprite.VISION);
        }

        onSpellCast(tome, hero);
    }

    public String desc() {
        return Messages.get(this, "desc", 4 + 4 * Dungeon.hero.pointsInTalent(Talent.DIVINE_SENSE)) + "\n\n" + Messages.get(this, "charge_cost", (int) chargeUse(Dungeon.hero));
    }

    public static class DivineSenseTracker extends FlavourBuff {

        public static final float DURATION = 30f;

        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.HOLY_SIGHT;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public void detach() {
            super.detach();
            Dungeon.observe();
            GameScene.updateFog();
        }
    }
}
