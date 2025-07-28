package com.nopalsoft.ponyrace.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.nopalsoft.ponyrace.AssetsHandler;
import com.nopalsoft.ponyrace.PonyRacingGame;
import com.nopalsoft.ponyrace.screens.BaseScreen;

public class Ventana extends Group {
    public static final float DURACION_ANIMATION = .3f;
    BaseScreen screen;
    PonyRacingGame game;
    AssetsHandler oAssetsHandler;

    private boolean isVisible = false;

    public Ventana(BaseScreen currentScreen) {
        screen = currentScreen;
        game = currentScreen.game;
        oAssetsHandler = game.assetsHandler;
    }

    public void setBackGround() {
        Image img = new Image(oAssetsHandler.fondoVentanas);
        img.setSize(getWidth(), getHeight());
        addActor(img);
    }

    public void show(Stage stage) {

        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setX(BaseScreen.SCREEN_WIDTH / 2f - getWidth() / 2f);

        setScale(.5f);
        addAction(Actions.sequence(Actions.scaleTo(1, 1, DURACION_ANIMATION),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        endResize();
                    }
                })));

        isVisible = true;
        stage.addActor(this);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void hide() {
        isVisible = false;
        remove();
    }

    protected void endResize() {

    }
}
