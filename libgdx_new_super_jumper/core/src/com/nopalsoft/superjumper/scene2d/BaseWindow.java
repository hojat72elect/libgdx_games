package com.nopalsoft.superjumper.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.superjumper.SuperJumperGame;
import com.nopalsoft.superjumper.screens.Screens;

public class BaseWindow extends Group {
    public static final float ANIMATION_DURATION = .3f;
    protected Screens screen;
    protected I18NBundle languagesBundle;
    protected SuperJumperGame game;

    private boolean isVisible = false;

    public BaseWindow(Screens currentScreen, float width, float height, float positionY) {
        screen = currentScreen;
        game = currentScreen.game;
        languagesBundle = game.languagesBundle;
        setSize(width, height);
        setY(positionY);
    }

    public void show(Stage stage) {

        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setX(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f);

        setScale(.5f);
        addAction(Actions.sequence(Actions.scaleTo(1, 1, ANIMATION_DURATION), Actions.run(this::endResize)));

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
