package com.nopalsoft.ponyrace.scene2d;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ponyrace.game.GameScreen;
import com.nopalsoft.ponyrace.menuobjetos.BotonNube;
import com.nopalsoft.ponyrace.screens.BaseScreen;
import com.nopalsoft.ponyrace.screens.LoadingScreen;
import com.nopalsoft.ponyrace.screens.WorldMapTiledScreen;

public class VentanaTimesUp extends Ventana {

    GameScreen gameScreen;

    public VentanaTimesUp(BaseScreen currentScreen) {
        super(currentScreen);
        setSize(450, 300);
        setY(90);
        setBackGround();

        gameScreen = (GameScreen) currentScreen;

        Image timeUp = new Image(oAssetsHandler.timeUp);
        timeUp.setPosition(getWidth() / 2f - timeUp.getWidth() / 2f, 250);
        addActor(timeUp);

        final BotonNube btTryAgain = new BotonNube(oAssetsHandler.nube, "Try again",
                oAssetsHandler.fontChco);
        btTryAgain.setSize(150, 100);
        btTryAgain.setPosition(getWidth() / 2f - btTryAgain.getWidth() / 2f,
                150);
        btTryAgain.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                btTryAgain.wasSelected = true;
                btTryAgain.addAction(Actions.sequence(Actions.delay(.2f),
                        btTryAgain.accionInicial, Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                hide();
                                game.setScreen(new LoadingScreen(game,
                                        GameScreen.class,
                                        gameScreen.nivelTiled));
                            }
                        })));
            }
        });

        final BotonNube btMainMenu = new BotonNube(oAssetsHandler.nube, "Menu",
                oAssetsHandler.fontChco);
        btMainMenu.setSize(150, 100);
        btMainMenu
                .setPosition(getWidth() / 2f - btMainMenu.getWidth() / 2f, 30);
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

        addActor(btMainMenu);
        addActor(timeUp);
    }
}
