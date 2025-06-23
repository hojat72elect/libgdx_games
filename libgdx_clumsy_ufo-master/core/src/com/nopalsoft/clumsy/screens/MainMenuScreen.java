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
import com.nopalsoft.clumsy.MainClumsy;
import com.nopalsoft.clumsy.Settings;
import com.nopalsoft.clumsy.game.arcade.GameScreenArcade;
import com.nopalsoft.clumsy.game.classic.GameScreenClassic;
import com.nopalsoft.clumsy.objects.Ufo;

public class MainMenuScreen extends Screens {

    Ufo oBird;

    Image titulo;

    Button btPlayClassic, btPlayArcade, btScore;
    Button btRate, btRestorePurchases, btNoAds;

    public MainMenuScreen(final MainClumsy game) {
        super(game);
        oBird = new Ufo(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f + 50);

        titulo = new Image(Assets.tituloApp);
        titulo.setSize(321, 156);
        titulo.setPosition(SCREEN_WIDTH / 2f - 321 / 2f, 500);

        btPlayClassic = new Button(new TextureRegionDrawable(Assets.btPlayClassic));
        btPlayClassic.setSize(160, 95);
        btPlayClassic.setPosition(75, 280);
        btPlayClassic.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btPlayClassic.setPosition(75, 277);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btPlayClassic.setPosition(75, 280);
                Assets.playSound(Assets.swooshing);
                changeScreenWithFadeOut(GameScreenClassic.class, game);
            }

        });

        btPlayArcade = new Button(new TextureRegionDrawable(Assets.btPlayArcade));
        btPlayArcade.setSize(160, 95);
        btPlayArcade.setPosition(250, 280);
        btPlayArcade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btPlayArcade.setPosition(250, 277);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btPlayArcade.setPosition(250, 280);
                Assets.playSound(Assets.swooshing);
                changeScreenWithFadeOut(GameScreenArcade.class, game);
            }

        });

        btScore = new Button(new TextureRegionDrawable(Assets.btLeaderboard));
        btScore.setSize(160, 95);
        btScore.setPosition(160, 180);
        btScore.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btScore.setPosition(160, 177);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btScore.setPosition(160, 180);
                if (game.gameServiceHandler.isSignedIn())
                    game.gameServiceHandler.getLeaderboard();
                else
                    game.gameServiceHandler.signIn();

            }

        });

        btRate = new Button(new TextureRegionDrawable(Assets.btRate));
        btRate.setSize(60, 60);
        btRate.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btRate.setPosition(btRate.getX(), btRate.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btRate.setPosition(btRate.getX(), btRate.getY() + 3);
                game.reqHandler.showRater();

            }

        });

        btNoAds = new Button(new TextureRegionDrawable(Assets.btNoAds));
        if (Settings.didBuyNoAds)
            btNoAds.setVisible(false);
        btNoAds.setSize(60, 60);
        btNoAds.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btNoAds.setPosition(btNoAds.getX(), btNoAds.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btNoAds.setPosition(btNoAds.getX(), btNoAds.getY() + 3);
                game.reqHandler.comprarRemoveAds();

            }

        });

        btRestorePurchases = new Button(new TextureRegionDrawable(Assets.btRestorePurchases));
        btRestorePurchases.setSize(60, 60);
        btRestorePurchases.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btRestorePurchases.setPosition(btRestorePurchases.getX(), btRestorePurchases.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btRestorePurchases.setPosition(btRestorePurchases.getX(), btRestorePurchases.getY() + 3);
                game.reqHandler.restorePurchases();

            }

        });

        Table bottomMenu = new Table();
        bottomMenu.setPosition(1, 1);
        bottomMenu.defaults().padRight(2.5f);

        bottomMenu.add(btRate);
        bottomMenu.add(btRestorePurchases);
        bottomMenu.add(btNoAds);
        bottomMenu.pack();

        stage.addActor(bottomMenu);

        stage.addActor(btScore);
        stage.addActor(btPlayClassic);
        stage.addActor(btPlayArcade);
        stage.addActor(titulo);
    }

    @Override
    public void draw(float delta) {

        batcher.begin();
        batcher.draw(Assets.fondo, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batcher.end();

        Assets.parallaxFondo.render(delta);

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        batcher.enableBlending();
        batcher.begin();

        oBird.update(delta, null);
        batcher.draw(Assets.bird.getKeyFrame(oBird.stateTime, true), oBird.position.x - 27, oBird.position.y - 20, 58,
                40);
        batcher.end();

    }

    @Override
    public void update(float delta) {
        if (Settings.didBuyNoAds)
            btNoAds.setVisible(false);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK || keycode == Keys.ESCAPE)
            Gdx.app.exit();
        return false;
    }

}
