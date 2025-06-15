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

    TextButton btLeaderBoard, btAchievements, btBack, btSignOut;
    Image elipseIzq;

    public LeaderboardScreen(final GalaxyInvadersGame game) {
        super(game);

        btBack = new TextButton(Assets.languagesBundle.get("back"), Assets.styleTextButtonBack);
        btBack.pad(0, 15, 35, 0);
        btBack.setSize(63, 63);
        btBack.setPosition(SCREEN_WIDTH - 63, SCREEN_HEIGHT - 63);
        btBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        btLeaderBoard = new TextButton(Assets.languagesBundle.get("leaderboard"), Assets.styleTextButtonMenu);
        btLeaderBoard.setHeight(50);// Altura 50
        btLeaderBoard.setSize(50, 0);// Al ancho actual le agregamos 50
        btLeaderBoard.setPosition(0, 245);
        btLeaderBoard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
            }
        });

        btAchievements = new TextButton(Assets.languagesBundle.get("achievements"), Assets.styleTextButtonMenu);
        btAchievements.setHeight(50);// Altura 50
        btAchievements.setSize(50, 0);// Al ancho actual le agregamos 50
        btAchievements.setPosition(0, 150);
        btAchievements.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
            }
        });

        btSignOut = new TextButton(Assets.languagesBundle.get("sign_out"), new TextButtonStyle(Assets.buttonSignInUp, Assets.buttonSignInDown, null, Assets.font15));
        btSignOut.getLabel().setWrap(true);
        btSignOut.setWidth(140);
        btSignOut.setPosition(2, 2);
        btSignOut.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        elipseIzq = new Image(Assets.leftMenuEllipse);
        elipseIzq.setSize(18.5f, 250.5f);
        elipseIzq.setPosition(0, 105);

        stage.addActor(btSignOut);
        stage.addActor(btAchievements);
        stage.addActor(btLeaderBoard);
        stage.addActor(btBack);
        stage.addActor(elipseIzq);
    }

    @Override
    public void draw(float delta) {
        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        batcher.disableBlending();
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
