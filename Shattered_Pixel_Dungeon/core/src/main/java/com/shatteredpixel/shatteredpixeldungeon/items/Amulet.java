

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AmuletScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Game;

import java.io.IOException;
import java.util.ArrayList;

public class Amulet extends Item {

    private static final String AC_END = "END";

    {
        image = ItemSpriteSheet.AMULET;

        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (hero.buff(AscensionChallenge.class) != null) {
            actions.clear();
        } else {
            actions.add(AC_END);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_END)) {
            showAmuletScene(false);
        }
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {
        if (super.doPickUp(hero, pos)) {

            if (!Statistics.amuletObtained) {
                Statistics.amuletObtained = true;
                hero.spend(-hero.cooldown());

                //delay with an actor here so pickup behaviour can fully process.
                Actor.add(new Actor() {

                    {
                        actPriority = VFX_PRIO;
                    }

                    @Override
                    protected boolean act() {
                        Actor.remove(this);
                        showAmuletScene(true);
                        return false;
                    }
                });
            }

            return true;
        } else {
            return false;
        }
    }

    private void showAmuletScene(boolean showText) {
        AmuletScene.noText = !showText;
        Game.switchScene(AmuletScene.class, new Game.SceneChangeCallback() {
            @Override
            public void beforeCreate() {

            }

            @Override
            public void afterCreate() {
                Badges.validateVictory();
                Badges.validateChampion(Challenges.activeChallenges());
                try {
                    Dungeon.saveAll();
                    Badges.saveGlobal();
                } catch (IOException e) {
                    ShatteredPixelDungeon.reportException(e);
                }
            }
        });
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public String desc() {
        String desc = super.desc();

        if (Dungeon.hero == null || Dungeon.hero.buff(AscensionChallenge.class) == null) {
            desc += "\n\n" + Messages.get(this, "desc_origins");
        } else {
            desc += "\n\n" + Messages.get(this, "desc_ascent");
        }

        return desc;
    }
}
