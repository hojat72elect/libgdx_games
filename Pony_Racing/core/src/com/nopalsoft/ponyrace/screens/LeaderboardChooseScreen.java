package com.nopalsoft.ponyrace.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ponyrace.PonyRacingGame;
import com.nopalsoft.ponyrace.menuobjetos.BotonNube;

public class LeaderboardChooseScreen extends BaseScreen {

    BotonNube btLeaderBoard, btAchievements, btBack;
    TextButton btSignOut;

    public LeaderboardChooseScreen(PonyRacingGame game) {
        super(game);

        cargarBotones();

        Table contenedor = new Table();
        contenedor.setPosition(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f);
        contenedor.add(btLeaderBoard).fillX();
        contenedor.row().pad(50, 0, 0, 0);
        contenedor.add(btAchievements).fillX();

        stage.addActor(contenedor);
    }

    private void cargarBotones() {
        btLeaderBoard = new BotonNube(assetsHandler.nube, "LeaderBoards",
                assetsHandler.fontChco);
        btLeaderBoard.setSize(290, 140);
        btLeaderBoard.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                btLeaderBoard.wasSelected = true;
                btLeaderBoard.addAction(Actions.sequence(Actions.delay(.2f),
                        btLeaderBoard.accionInicial));
            }
        });

        btAchievements = new BotonNube(assetsHandler.nube, "Achievements",
                assetsHandler.fontChco);
        btAchievements.setSize(290, 140);
        btAchievements.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                btAchievements.wasSelected = true;
                btAchievements.addAction(Actions.sequence(Actions.delay(.2f),
                        btAchievements.accionInicial));
            }
        });

        btBack = new BotonNube(assetsHandler.nube, "Back", assetsHandler.fontGde);
        btBack.setSize(150, 100);
        btBack.setPosition(645, 5);
        btBack.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                btBack.wasSelected = true;
                btBack.addAction(Actions.sequence(Actions.delay(.2f),
                        btBack.accionInicial, Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                LeaderboardChooseScreen.this.game
                                        .setScreen(new LoadingScreen(
                                                LeaderboardChooseScreen.this.game,
                                                MainMenuScreen.class));
                            }
                        })));
            }
        });

        TextButtonStyle stilo = new TextButtonStyle(assetsHandler.btSignInUp,
                assetsHandler.btSignInDown, null,
                assetsHandler.skin.getFont("default-font"));
        btSignOut = new TextButton("Sign out", stilo);
        btSignOut.setPosition(5, 5);
        btSignOut.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                LeaderboardChooseScreen.this.game
                        .setScreen(new LoadingScreen(
                                LeaderboardChooseScreen.this.game,
                                MainMenuScreen.class));
            }
        });


        stage.addActor(btSignOut);
        stage.addActor(btBack);
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
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
            this.game.setScreen(new LoadingScreen(game, MainMenuScreen.class));

            return true;
        }
        return false;
    }
}
