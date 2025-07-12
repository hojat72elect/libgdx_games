package com.nopalsoft.dosmil.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.dosmil.Assets;
import com.nopalsoft.dosmil.Settings;
import com.nopalsoft.dosmil.screens.MainMenuScreen;
import com.nopalsoft.dosmil.screens.Screens;

public class GameOverDialog extends Group {

    Screens screen;

    public GameOverDialog(final Screens screen, boolean didWin, int time, long score) {
        this.screen = screen;
        setSize(420, 450);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 260);
        setScale(.5f);

        Image background = new Image(Assets.scoresBackgroundAtlasRegion);
        background.setSize(getWidth(), getHeight());
        addActor(background);

        String title = Assets.languagesBundle.get("gameOver");
        if (didWin)
            title = Assets.languagesBundle.get("congratulations");

        Label labelCongrats = new Label(title, Assets.labelStyleLarge);
        labelCongrats.setAlignment(Align.center);
        labelCongrats.setFontScale(.50f);
        labelCongrats.setPosition(getWidth() / 2f - labelCongrats.getWidth() / 2f, 365);
        addActor(labelCongrats);

        final Table scoreTable = new Table();
        scoreTable.setSize(getWidth(), 180);
        scoreTable.setY(170);
        scoreTable.padLeft(15).padRight(15);

        // ACTUAL TIME
        Label labelTime = new Label(Assets.languagesBundle.get("time"), Assets.labelStyleSmall);
        labelTime.setAlignment(Align.left);

        Label labelNumTime = new Label(time + Assets.languagesBundle.get("secondAbbreviation"), Assets.labelStyleSmall);
        labelNumTime.setAlignment(Align.right);

        // ACTUAL SCORE
        Label labelScore = new Label(Assets.languagesBundle.get("score"), Assets.labelStyleSmall);
        labelScore.setAlignment(Align.left);

        Label labelNumScore = new Label(score + "", Assets.labelStyleSmall);
        labelNumScore.setAlignment(Align.right);

        // BEST MOVES
        Label labelBestScore = new Label(Assets.languagesBundle.get("bestScore"), Assets.labelStyleSmall);
        labelBestScore.setAlignment(Align.left);

        Label labelBestNumScore = new Label(Settings.bestScore + "", Assets.labelStyleSmall);
        labelBestNumScore.setAlignment(Align.right);

        scoreTable.add(labelTime).left();
        scoreTable.add(labelNumTime).right().expand();

        scoreTable.row();
        scoreTable.add(labelScore).left();
        scoreTable.add(labelNumScore).right().expand();

        scoreTable.row();
        scoreTable.add(labelBestScore).left();
        scoreTable.add(labelBestNumScore).right().expand();

        // Facebook Twitter
        final Button buttonShareFacebook;
        final Button buttonShareTwitter;

        buttonShareTwitter = new Button(Assets.buttonTwitter);
        buttonShareTwitter.setSize(50, 50);
        buttonShareTwitter.setPosition(155, 110);
        screen.addPressEffect(buttonShareTwitter);


        buttonShareFacebook = new Button(Assets.buttonFacebook);
        buttonShareFacebook.setSize(50, 50);
        buttonShareFacebook.setPosition(225, 110);
        screen.addPressEffect(buttonShareFacebook);

        final Label lbMainMenu = new Label(Assets.languagesBundle.get("menu"), Assets.labelStyleLarge);
        lbMainMenu.setWidth(getWidth() - 10);
        lbMainMenu.setFontScale(.75f);
        lbMainMenu.setPosition(getWidth() / 2f - lbMainMenu.getWidth() / 2f, 30);
        lbMainMenu.setWrap(true);
        lbMainMenu.setAlignment(Align.center);
        screen.addPressEffect(lbMainMenu);
        lbMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(MainMenuScreen.class, screen.game);
            }
        });

        addAction(Actions.sequence(Actions.scaleTo(1, 1, .2f), Actions.run(new Runnable() {

            @Override
            public void run() {
                addActor(scoreTable);
                addActor(buttonShareTwitter);
                addActor(buttonShareFacebook);
                addActor(lbMainMenu);
            }
        })));
    }
}
