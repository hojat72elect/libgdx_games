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

/**
 * The base class for all dialogs we create in this game. It has a background, a title and a close button.
 * It also has an animation effect when appearing and disappearing.
 */
public class Dialog extends Group {
    public static final float ANIMATION_DURATION = .3f;
    protected Screens screen;
    protected I18NBundle languages;
    private final Image dim;
    private boolean isShown = false;

    public Dialog(Screens currentScreen, float width, float height, float positionY) {
        screen = currentScreen;
        languages = currentScreen.game.languages;
        setSize(width, height);
        setY(positionY);

        dim = new Image(Assets.pixelBlack);
        dim.setSize(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);

        setBackGround(Assets.windowBackground);

    }

    protected void setCloseButton() {
        Button btClose = new Button(Assets.btClose, Assets.buttonClosePress);
        btClose.setSize(60F, 60F);
        btClose.setPosition(290F, 250F);
        btClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        addActor(btClose);

    }

    protected void setTitle(String text, float fontScale) {
        Table tableTitle;
        tableTitle = new Table();

        tableTitle.setSize((float) 180, (float) 50);
        tableTitle.setPosition(getWidth() / 2f - tableTitle.getWidth() / 2f, getHeight() - tableTitle.getHeight());

        Label labelTitle = new Label(text, new LabelStyle(Assets.fontDefault, Color.WHITE));
        labelTitle.setFontScale(fontScale);
        tableTitle.add(labelTitle);
        addActor(tableTitle);

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
                .sequence(Actions.scaleTo(.35f, .35f, ANIMATION_DURATION), Actions.run(this::hideCompleted), Actions.removeActor(dim), Actions.removeActor()));
        dim.addAction(Actions.alpha(0, ANIMATION_DURATION));
    }

    /**
     * the callback that will be called when the dialog is closed.
     */
    public void hideCompleted() {

    }

}
