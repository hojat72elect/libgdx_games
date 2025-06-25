

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

public class Radiance extends ClericSpell {

    public static final Radiance INSTANCE = new Radiance();

    @Override
    public int icon() {
        return HeroIcon.RADIANCE;
    }

    @Override
    public float chargeUse(Hero hero) {
        return 2;
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.subClass == HeroSubClass.PRIEST;
    }

    @Override
    public void onCast(HolyTome tome, Hero hero) {

        GameScene.flash(0x80FFFFFF);
        Sample.INSTANCE.play(Assets.Sounds.BLAST);

        if (Dungeon.level.viewDistance < 6) {
            Buff.prolong(hero, Light.class, Dungeon.isChallenged(Challenges.DARKNESS) ? 20 : 100);
        }

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {

                if (mob.buff(GuidingLight.Illuminated.class) != null) {
                    mob.damage(hero.lvl + 5, GuidingLight.class);
                } else {
                    Buff.affect(mob, GuidingLight.Illuminated.class);
                    Buff.affect(mob, GuidingLight.WasIlluminatedTracker.class);
                }
                Buff.affect(mob, Paralysis.class, 3f);
            }
        }

        hero.spend(1f);
        hero.busy();
        hero.sprite.operate(hero.pos);

        onSpellCast(tome, hero);
    }
}
