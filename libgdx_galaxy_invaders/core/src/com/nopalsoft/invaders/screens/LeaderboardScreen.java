package com.nopalsoft.invaders.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.invaders.Assets;
import com.nopalsoft.invaders.GalaxyInvadersGame;

public class LeaderboardScreen extends Screens {

    TextButton buttonLeaderBoard, buttonAchievements, buttonBack, buttonSignOut;
    Image leftEllipse;

    public LeaderboardScreen(final GalaxyInvadersGame game) {
        super(game);

        buttonBack = new TextButton(Assets.languagesBundle.get("back"), Assets.styleTextButtonBack);
        buttonBack.pad(0, 15, 35, 0);
        buttonBack.setSize(63, 63);
        buttonBack.setPosition(SCREEN_WIDTH - 63, SCREEN_HEIGHT - 63);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        buttonLeaderBoard = new TextButton(Assets.languagesBundle.get("leaderboard"), Assets.styleTextButtonMenu);
        buttonLeaderBoard.setHeight(50);// Height 50
        buttonLeaderBoard.setSize(50, 0);// We add 50 to the current width
        buttonLeaderBoard.setPosition(0, 245);
        buttonLeaderBoard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
            }
        });

        buttonAchievements = new TextButton(Assets.languagesBundle.get("achievements"), Assets.styleTextButtonMenu);
        buttonAchievements.setHeight(50);// Height 50
        buttonAchievements.setSize(50, 0);// We add 50 to the current width
        buttonAchievements.setPosition(0, 150);
        buttonAchievements.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
            }
        });

        buttonSignOut = new TextButton(Assets.languagesBundle.get("sign_out"), new TextButtonStyle(Assets.buttonSignInUp, Assets.buttonSignInDown, null, Assets.font15));
        buttonSignOut.getLabel().setWrap(true);
        buttonSignOut.setWidth(140);
        buttonSignOut.setPosition(2, 2);
        buttonSignOut.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        leftEllipse = new Image(Assets.leftMenuEllipse);
        leftEllipse.setSize(18.5f, 250.5f);
        leftEllipse.setPosition(0, 105);

        stage.addActor(buttonSignOut);
        stage.addActor(buttonAchievements);
        stage.addActor(buttonLeaderBoard);
        stage.addActor(buttonBack);
        stage.addActor(leftEllipse);
    }

    @Override
    public void draw(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.disableBlending();
        Assets.backgroundLayer.render(delta);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public boolean keyDown(int keyPressed) {
        if (keyPressed == Keys.BACK || keyPressed == Keys.ESCAPE) {
            Assets.playSound(Assets.clickSound);
            game.setScreen(new MainMenuScreen(game));
            return true;
        }
        return false;
    }
}
