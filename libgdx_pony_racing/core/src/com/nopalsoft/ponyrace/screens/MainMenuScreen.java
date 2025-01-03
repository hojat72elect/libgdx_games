package com.nopalsoft.ponyrace.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ponyrace.MainPonyRace;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.menuobjetos.BotonNube;

public class MainMenuScreen extends Screens {

    ImageButton btInfo, btFacebook, btSonido, btMusica;
    TextButton btAyuda;
    BotonNube btJugar2, btMore, btLeaderBoard;

    public MainMenuScreen(MainPonyRace game) {
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

        oAssets.skeletonMenuTitle.setX(400);
        oAssets.skeletonMenuTitle.setY(370);
    }

    public void cargarBotones() {
        btJugar2 = new BotonNube(oAssets.nube, "Play", oAssets.fontGde);
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

        btMore = new BotonNube(oAssets.nube, "More", oAssets.fontGde);
        btMore.setSize(200, 130);
        btMore.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                btMore.wasSelected = true;
                btMore.addAction(Actions.sequence(Actions.delay(.2f), btMore.accionInicial, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.reqHandler.showMoreGames();
                    }
                })));
            }
        });

        btLeaderBoard = new BotonNube(oAssets.nube, "LeaderBoards", oAssets.fontChco);
        btLeaderBoard.setSize(290, 140);
        btLeaderBoard.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                btLeaderBoard.wasSelected = true;
                btLeaderBoard.addAction(Actions.sequence(Actions.delay(.2f), btLeaderBoard.accionInicial, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        if (!game.gameServiceHandler.isSignedIn())
                            game.gameServiceHandler.signIn();
                        else
                            game.setScreen(new LoadingScreen(game, LeaderboardChooseScreen.class));
                    }
                })));
            }
        });

        btFacebook = new ImageButton(oAssets.btnFacebook);
        btFacebook.setSize(50, 50);
        btFacebook.setPosition(750, 0);
        btFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//				btFacebook.addAction(Actions.sequence(Actions.delay(1f), Actions.run(new Runnable() {
//					@Override
//					public void run() {
//						if (!Settings.seDioLike) {
//							Settings.seDioLike = true;
//							Settings.sumarMonedas(Settings.MONEDAS_REGALO_FACEBOOK);
//							Settings.guardar();
//						}
//					}
//				})));
//				game.reqHandler.showFacebook();

                game.reqHandler.shareOnFacebook("asd");

                // // --
                // Settings.borrarDatosGuardados();
                // game.reqHandler.resetAchievements();
            }
        });

        btSonido = new ImageButton(oAssets.btSonidoOff, null, oAssets.btSonidoON);
        btSonido.setSize(60, 60);
        btSonido.setPosition(5, 5);
        btSonido.setChecked(Settings.isSonidoON);
        btSonido.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isSonidoON = !Settings.isSonidoON;
                super.clicked(event, x, y);
            }
        });

        btMusica = new ImageButton(oAssets.btMusicaOff, null, oAssets.btMusicaON);
        btMusica.setSize(60, 60);
        btMusica.setPosition(70, 2);
        btMusica.setChecked(Settings.isMusicaON);
        btMusica.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isMusicaON = !Settings.isMusicaON;
                oAssets.playMusicMenus();
                super.clicked(event, x, y);
            }
        });
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {

        guiCam.update();
        batcher.setProjectionMatrix(guiCam.combined);

        batcher.disableBlending();
        batcher.begin();
        batcher.draw(oAssets.fondoMainMenu, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        batcher.enableBlending();
        renderFlagTitle(delta);
        batcher.end();

        stage.act(delta);
        stage.draw();

        // Table.drawDebug(stage);
    }

    private void renderFlagTitle(float delta) {
        oAssets.animationMenuTitle.apply(oAssets.skeletonMenuTitle, ScreenlastStatetime, ScreenStateTime, true, null);
        oAssets.skeletonMenuTitle.updateWorldTransform();
        oAssets.skeletonMenuTitle.update(delta);
        skelrender.draw(batcher, oAssets.skeletonMenuTitle);
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
