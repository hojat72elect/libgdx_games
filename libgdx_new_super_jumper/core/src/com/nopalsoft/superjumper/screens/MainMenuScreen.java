package com.nopalsoft.superjumper.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.Settings;
import com.nopalsoft.superjumper.SuperJumperGame;
import com.nopalsoft.superjumper.game.GameScreen;

public class MainMenuScreen extends Screens {

    Image imageTitle;

    TextButton buttonShop, buttonPlay, buttonLeaderBoard, buttonRate;
    Label labelBestScore;

    public MainMenuScreen(final SuperJumperGame game) {
        super(game);

        imageTitle = new Image(Assets.title);
        imageTitle.setPosition(SCREEN_WIDTH / 2f - imageTitle.getWidth() / 2f, 800);

        imageTitle.addAction(Actions.sequence(Actions.moveTo(imageTitle.getX(), 600, 1, Interpolation.bounceOut), Actions.run(() -> stage.addActor(labelBestScore))));

        labelBestScore = new Label("Best score " + Settings.bestScore, Assets.labelStyleSmall);
        labelBestScore.setPosition(SCREEN_WIDTH / 2f - labelBestScore.getWidth() / 2f, 570);
        labelBestScore.getColor().a = 0;
        labelBestScore.addAction(Actions.alpha(1, .25f));

        buttonPlay = new TextButton("Play", Assets.textButtonStyleLarge);
        buttonPlay.setPosition(SCREEN_WIDTH / 2f - buttonPlay.getWidth() / 2f, 440);
        buttonPlay.pad(10);
        buttonPlay.pack();
        addPressEffect(buttonPlay);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(GameScreen.class, game);
            }
        });

        buttonShop = new TextButton("Shop", Assets.textButtonStyleLarge);
        buttonShop.setPosition(SCREEN_WIDTH / 2f - buttonShop.getWidth() / 2f, 340);
        buttonShop.pad(10);
        buttonShop.pack();
        addPressEffect(buttonShop);

        buttonRate = new TextButton("Rate", Assets.textButtonStyleLarge);
        buttonRate.setPosition(SCREEN_WIDTH / 2f - buttonRate.getWidth() / 2f, 340);
        buttonRate.pad(10);
        buttonRate.pack();
        addPressEffect(buttonRate);

        buttonLeaderBoard = new TextButton("Leaderboard", Assets.textButtonStyleLarge);
        buttonLeaderBoard.pad(10);
        buttonLeaderBoard.pack();
        buttonLeaderBoard.setPosition(SCREEN_WIDTH / 2f - buttonLeaderBoard.getWidth() / 2f, 240);

        addPressEffect(buttonLeaderBoard);

        stage.addActor(imageTitle);
        stage.addActor(buttonPlay);
        stage.addActor(buttonRate);
        stage.addActor(buttonLeaderBoard);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {
        batch.begin();
        batch.draw(Assets.background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.draw(Assets.platformBeigeBroken, 100, 100, 125, 45);
        batch.draw(Assets.platformBlue, 350, 280, 125, 45);
        batch.draw(Assets.platformRainbow, 25, 430, 125, 45);
        batch.draw(Assets.playerJump, 25, 270, 75, 80);
        batch.draw(Assets.happyCloud, 350, 500, 95, 60);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            Gdx.app.exit();
        }
        return super.keyDown(keycode);
    }
}
