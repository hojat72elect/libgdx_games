package com.nopalsoft.slamthebird.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.SlamTheBirdGame;
import com.nopalsoft.slamthebird.screens.BaseScreen;

public class Dialog extends Group {
    public static final float ANIMATION_DURATION = .3f;
    BaseScreen screen;
    SlamTheBirdGame game;

    private boolean isVisible = false;

    public Dialog(BaseScreen currentScreen) {
        screen = currentScreen;
        game = currentScreen.game;
    }

    public void setBackGround() {
        Image img = new Image(Assets.fondoPuntuaciones);
        img.setSize(getWidth(), getHeight());
        addActor(img);
    }

    public void show(Stage stage) {

        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setX(BaseScreen.SCREEN_WIDTH / 2f - getWidth() / 2f);

        setScale(.5f);
        addAction(Actions.sequence(Actions.scaleTo(1, 1, ANIMATION_DURATION),
                Actions.run(this::endResize)));

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
