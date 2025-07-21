package com.nopalsoft.clumsy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.clumsy.Assets;
import com.nopalsoft.clumsy.ClumsyUfoGame;
import com.nopalsoft.clumsy.Settings;
import com.nopalsoft.clumsy.game.arcade.GameScreenArcade;
import com.nopalsoft.clumsy.game.classic.ClassicGameScreen;
import com.nopalsoft.clumsy.objects.Ufo;

public class MainMenuScreen extends Screens {

    Ufo player;

    Image titleImage;

    Button buttonPlayClassic, buttonPlayArcade, buttonScore;
    Button buttonRate, buttonRestorePurchases, buttonNoAds;

    public MainMenuScreen(final ClumsyUfoGame game) {
        super(game);
        player = new Ufo(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f + 50);

        titleImage = new Image(Assets.appTitle);
        titleImage.setSize(321, 156);
        titleImage.setPosition(SCREEN_WIDTH / 2f - 321 / 2f, 500);

        buttonPlayClassic = new Button(new TextureRegionDrawable(Assets.buttonPlayClassic));
        buttonPlayClassic.setSize(160, 95);
        buttonPlayClassic.setPosition(75, 280);
        buttonPlayClassic.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buttonPlayClassic.setPosition(75, 277);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                buttonPlayClassic.setPosition(75, 280);
                Assets.playSound(Assets.swooshing);
                changeScreenWithFadeOut(ClassicGameScreen.class, game);
            }
        });

        buttonPlayArcade = new Button(new TextureRegionDrawable(Assets.buttonPlayArcade));
        buttonPlayArcade.setSize(160, 95);
        buttonPlayArcade.setPosition(250, 280);
        buttonPlayArcade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buttonPlayArcade.setPosition(250, 277);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                buttonPlayArcade.setPosition(250, 280);
                Assets.playSound(Assets.swooshing);
                changeScreenWithFadeOut(GameScreenArcade.class, game);
            }
        });

        buttonScore = new Button(new TextureRegionDrawable(Assets.buttonLeaderboard));
        buttonScore.setSize(160, 95);
        buttonScore.setPosition(160, 180);
        buttonScore.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buttonScore.setPosition(160, 177);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                buttonScore.setPosition(160, 180);
            }
        });

        buttonRate = new Button(new TextureRegionDrawable(Assets.buttonRate));
        buttonRate.setSize(60, 60);
        buttonRate.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buttonRate.setPosition(buttonRate.getX(), buttonRate.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                buttonRate.setPosition(buttonRate.getX(), buttonRate.getY() + 3);
            }
        });

        buttonNoAds = new Button(new TextureRegionDrawable(Assets.buttonNoAds));
        if (Settings.didBuyNoAds)
            buttonNoAds.setVisible(false);
        buttonNoAds.setSize(60, 60);
        buttonNoAds.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buttonNoAds.setPosition(buttonNoAds.getX(), buttonNoAds.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                buttonNoAds.setPosition(buttonNoAds.getX(), buttonNoAds.getY() + 3);
            }
        });

        buttonRestorePurchases = new Button(new TextureRegionDrawable(Assets.buttonRestorePurchases));
        buttonRestorePurchases.setSize(60, 60);
        buttonRestorePurchases.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buttonRestorePurchases.setPosition(buttonRestorePurchases.getX(), buttonRestorePurchases.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                buttonRestorePurchases.setPosition(buttonRestorePurchases.getX(), buttonRestorePurchases.getY() + 3);
            }
        });

        Table bottomMenu = new Table();
        bottomMenu.setPosition(1, 1);
        bottomMenu.defaults().padRight(2.5f);

        bottomMenu.add(buttonRate);
        bottomMenu.add(buttonRestorePurchases);
        bottomMenu.add(buttonNoAds);
        bottomMenu.pack();

        stage.addActor(bottomMenu);

        stage.addActor(buttonScore);
        stage.addActor(buttonPlayClassic);
        stage.addActor(buttonPlayArcade);
        stage.addActor(titleImage);
    }

    @Override
    public void draw(float delta) {

        batch.begin();
        batch.draw(Assets.background0, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.end();

        Assets.parallaxBackground.render(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.enableBlending();
        batch.begin();

        player.update(delta, null);
        batch.draw(Assets.bird.getKeyFrame(player.stateTime, true), player.position.x - 27, player.position.y - 20, 58,
                40);
        batch.end();
    }

    @Override
    public void update(float delta) {
        if (Settings.didBuyNoAds)
            buttonNoAds.setVisible(false);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK || keycode == Keys.ESCAPE)
            Gdx.app.exit();
        return false;
    }
}
