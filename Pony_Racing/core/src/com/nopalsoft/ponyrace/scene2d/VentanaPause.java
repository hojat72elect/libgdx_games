package com.nopalsoft.ponyrace.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ponyrace.game.GameScreenTileds;
import com.nopalsoft.ponyrace.menuobjetos.BotonNube;
import com.nopalsoft.ponyrace.screens.LoadingScreen;
import com.nopalsoft.ponyrace.screens.Screens;
import com.nopalsoft.ponyrace.screens.WorldMapTiledScreen;

public class VentanaPause extends Ventana {

    GameScreenTileds gameScreen;

    public VentanaPause(Screens currentScreen) {
        super(currentScreen);
        setSize(450, 300);
        setY(90);
        setBackGround();

        gameScreen = (GameScreenTileds) currentScreen;

        Label lbTitle = new Label("Paused", new LabelStyle(oAssets.fontGde,
                Color.WHITE));
        lbTitle.setPosition(getWidth() / 2f - lbTitle.getWidth() / 2f, 255);

        final BotonNube btResume = new BotonNube(oAssets.nube, "Resume",
                oAssets.fontChco);
        btResume.setSize(150, 100);
        btResume.setPosition(getWidth() / 2f - btResume.getWidth() / 2f, 150);
        btResume.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                btResume.wasSelected = true;
                btResume.addAction(Actions.sequence(Actions.delay(.2f),
                        btResume.accionInicial, Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                hide();
                                gameScreen.setRunning();
                            }
                        })));
            }
        });

        final BotonNube btTryAgain = new BotonNube(oAssets.nube, "Try again",
                oAssets.fontChco);
        btTryAgain.setSize(150, 100);
        btTryAgain.setPosition(60, 30);
        btTryAgain.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                btTryAgain.wasSelected = true;
                btTryAgain.addAction(Actions.sequence(Actions.delay(.2f),
                        btTryAgain.accionInicial, Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                hide();
                                game.setScreen(new LoadingScreen(game,
                                        GameScreenTileds.class,
                                        gameScreen.nivelTiled));
                            }
                        })));
            }
        });

        final BotonNube btMainMenu = new BotonNube(oAssets.nube, "Menu",
                oAssets.fontChco);
        btMainMenu.setSize(150, 100);
        btMainMenu.setPosition(240, 30);
        btMainMenu.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                btMainMenu.wasSelected = true;
                btMainMenu.addAction(Actions.sequence(Actions.delay(.2f),
                        btMainMenu.accionInicial, Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                hide();
                                game.setScreen(new LoadingScreen(game,
                                        WorldMapTiledScreen.class));
                                screen.dispose();
                            }
                        })));
            }
        });

        if (gameScreen.nivelTiled != 1000)// Si es el mundo secreto no agrego el try again
            addActor(btTryAgain);

        addActor(btResume);
        addActor(btMainMenu);
        addActor(lbTitle);
    }
}
