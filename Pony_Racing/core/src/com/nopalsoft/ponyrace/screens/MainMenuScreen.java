package com.nopalsoft.ponyrace.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ponyrace.PonyRacingGame;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.menuobjetos.BotonNube;

public class MainMenuScreen extends BaseScreen {

    ImageButton btFacebook;
    ImageButton btSonido;
    ImageButton btMusica;
    BotonNube btJugar2, btMore, btLeaderBoard;

    public MainMenuScreen(PonyRacingGame game) {
        super(game);
        cargarBotones();

        MoveToAction actionLogoMenu = Actions.action(MoveToAction.class);
        actionLogoMenu.setInterpolation(Interpolation.swingOut);
        actionLogoMenu.setPosition(235, 270);
        actionLogoMenu.setDuration(.9f);

        Table contenedor = new Table();
        contenedor.setPosition(SCREEN_WIDTH / 2f, 140);
        contenedor.add(btJugar2).fillX();
        contenedor.add().width(130);
        contenedor.add(btMore).fillX();
        contenedor.row();
        contenedor.add(btLeaderBoard).colspan(3);

        // contenedor.debug();

        stage.addActor(contenedor);
        stage.addActor(btSonido);
        stage.addActor(btMusica);

        assetsHandler.skeletonMenuTitle.setX(400);
        assetsHandler.skeletonMenuTitle.setY(370);
    }

    public void cargarBotones() {
        btJugar2 = new BotonNube(assetsHandler.nube, "Play", assetsHandler.fontGde);
        btJugar2.setSize(200, 130);

        btJugar2.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                btJugar2.wasSelected = true;
                btJugar2.addAction(Actions.sequence(Actions.delay(.2f), btJugar2.accionInicial, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.game.setScreen(new LoadingScreen(game, WorldMapTiledScreen.class));
                    }
                })));
            }
        });

        btMore = new BotonNube(assetsHandler.nube, "More", assetsHandler.fontGde);
        btMore.setSize(200, 130);
        btMore.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                btMore.wasSelected = true;
                btMore.addAction(Actions.sequence(Actions.delay(.2f), btMore.accionInicial));
            }
        });

        btLeaderBoard = new BotonNube(assetsHandler.nube, "LeaderBoards", assetsHandler.fontChco);
        btLeaderBoard.setSize(290, 140);
        btLeaderBoard.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                btLeaderBoard.wasSelected = true;
                btLeaderBoard.addAction(Actions.sequence(Actions.delay(.2f), btLeaderBoard.accionInicial, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new LoadingScreen(game, LeaderboardChooseScreen.class));
                    }
                })));
            }
        });

        btFacebook = new ImageButton(assetsHandler.btnFacebook);
        btFacebook.setSize(50, 50);
        btFacebook.setPosition(750, 0);

        btSonido = new ImageButton(assetsHandler.btSonidoOff, null, assetsHandler.btSonidoON);
        btSonido.setSize(60, 60);
        btSonido.setPosition(5, 5);
        btSonido.setChecked(Settings.isSoundOn);
        btSonido.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isSoundOn = !Settings.isSoundOn;
                super.clicked(event, x, y);
            }
        });

        btMusica = new ImageButton(assetsHandler.btMusicaOff, null, assetsHandler.btMusicaON);
        btMusica.setSize(60, 60);
        btMusica.setPosition(70, 2);
        btMusica.setChecked(Settings.isMusicOn);
        btMusica.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isMusicOn = !Settings.isMusicOn;
                assetsHandler.playMusicMenus();
                super.clicked(event, x, y);
            }
        });
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.disableBlending();
        batch.begin();
        batch.draw(assetsHandler.fondoMainMenu, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        batch.enableBlending();
        renderFlagTitle(delta);
        batch.end();

        stage.act(delta);
        stage.draw();

        // Table.drawDebug(stage);
    }

    private void renderFlagTitle(float delta) {
        assetsHandler.animationMenuTitle.apply(assetsHandler.skeletonMenuTitle, screenLastStateTime, ScreenStateTime, true, null);
        assetsHandler.skeletonMenuTitle.updateWorldTransform();
        assetsHandler.skeletonMenuTitle.update(delta);
        skeletonRenderer.draw(batch, assetsHandler.skeletonMenuTitle);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
            Gdx.app.exit();
            return true;
        }
        return false;
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        super.hide();
    }
}
