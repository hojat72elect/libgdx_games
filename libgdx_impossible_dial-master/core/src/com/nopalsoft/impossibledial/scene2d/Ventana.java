package com.nopalsoft.impossibledial.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.impossibledial.Assets;
import com.nopalsoft.impossibledial.MainGame;
import com.nopalsoft.impossibledial.screens.Screens;


public class Ventana extends Group {
    public static final float DURACION_ANIMATION = .3f;

    private Image dim;
    protected Screens screen;
    protected I18NBundle idiomas;
    protected MainGame game;

    private boolean isShown = false;

    public Ventana(Screens currentScreen, float width, float height, float positionY) {
        screen = currentScreen;
        game = currentScreen.game;
        idiomas = Assets.idiomas;
        setSize(width, height);
        setY(positionY);

        dim = new Image(Assets.pixelNegro);
        dim.setSize(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);

        setBackGround(Assets.dialogVentana);

    }

    protected void setCloseButton(float positionX, float positionY, float size) {
        Button btClose = new Button(Assets.btFalse);
        btClose.setSize(size, size);
        btClose.setPosition(positionX, positionY);
        screen.addEfectoPress(btClose);
        btClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        addActor(btClose);

    }

    private void setBackGround(NinePatchDrawable imageBackground) {
        Image img = new Image(imageBackground);
        img.setSize(getWidth(), getHeight());
        addActor(img);

    }

    public void show(Stage stage) {

        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setX(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f);

        setScale(.5f);
        addAction(Actions.scaleTo(1, 1, DURACION_ANIMATION));

        dim.getColor().a = 0;
        dim.addAction(Actions.alpha(.7f, DURACION_ANIMATION));

        isShown = true;
        stage.addActor(dim);
        stage.addActor(this);

        game.reqHandler.showAdBanner();
    }

    public boolean isShown() {
        return isShown;
    }

    public void hide() {
        isShown = false;
        game.reqHandler.hideAdBanner();
        addAction(Actions.sequence(Actions.scaleTo(.5f, .5f, DURACION_ANIMATION), Actions.removeActor()));
        dim.addAction(Actions.sequence(Actions.alpha(0, DURACION_ANIMATION), Actions.removeActor()));

    }

}
