package com.nopalsoft.sokoban.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.screens.Screens;

public class WindowGroup extends Group {
    public static final float ANIMATION_DURATION = .3f;

    private final Image dim;
    protected Screens screen;
    protected I18NBundle languages;

    private boolean isShown = false;

    public WindowGroup(Screens currentScreen, float width, float height, float positionY) {
        screen = currentScreen;
        languages = currentScreen.game.languages;
        setSize(width, height);
        setY(positionY);

        dim = new Image(Assets.pixelNegro);
        dim.setSize(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);

        setBackGround(Assets.backgroundVentana);
    }

    protected void setCloseButton() {
        Button btClose = new Button(Assets.btClose, Assets.btClosePress);
        btClose.setSize((float) 60, (float) 60);
        btClose.setPosition((float) 290, (float) 250);
        btClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        addActor(btClose);
    }

    protected void setTitle(String text, float fontScale) {
        Table tbTitulo;
        tbTitulo = new Table();

        tbTitulo.setSize((float) 180, (float) 50);
        tbTitulo.setPosition(getWidth() / 2f - tbTitulo.getWidth() / 2f, getHeight() - tbTitulo.getHeight());

        Label lbTitulo = new Label(text, new LabelStyle(Assets.font, Color.WHITE));
        lbTitulo.setFontScale(fontScale);
        tbTitulo.add(lbTitulo);
        addActor(tbTitulo);
    }

    private void setBackGround(TextureRegionDrawable imageBackground) {
        Image img = new Image(imageBackground);
        img.setSize(getWidth(), getHeight());
        addActor(img);
    }

    public void show(Stage stage) {

        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setX(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f);

        setScale(.35f);
        addAction(Actions.scaleTo(1, 1, ANIMATION_DURATION));

        dim.getColor().a = 0;
        dim.addAction(Actions.alpha(.7f, ANIMATION_DURATION));

        isShown = true;
        stage.addActor(dim);
        stage.addActor(this);
    }

    public boolean isShown() {
        return isShown;
    }

    public void hide() {
        isShown = false;

        addAction(Actions
                .sequence(Actions.scaleTo(.35f, .35f, ANIMATION_DURATION), Actions.removeActor(dim), Actions.removeActor()));
        dim.addAction(Actions.alpha(0, ANIMATION_DURATION));
    }

}
