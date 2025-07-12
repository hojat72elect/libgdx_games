package com.nopalsoft.dosmil.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.dosmil.Assets;
import com.nopalsoft.dosmil.game.GameScreen;
import com.nopalsoft.dosmil.screens.MainMenuScreen;
import com.nopalsoft.dosmil.screens.Screens;

public class GamePausedDialog extends Group {

    Screens screen;

    public GamePausedDialog(final Screens screen) {
        this.screen = screen;
        setSize(420, 300);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 260);
        setScale(.5f);

        Image background = new Image(Assets.scoresBackgroundAtlasRegion);
        background.setSize(getWidth(), getHeight());
        addActor(background);

        Label labelPaused = new Label(Assets.languagesBundle.get("pause"), Assets.labelStyleLarge);
        labelPaused.setAlignment(Align.center);
        labelPaused.setFontScale(.85f);
        labelPaused.setPosition(getWidth() / 2f - labelPaused.getWidth() / 2f, 230);
        addActor(labelPaused);

        final Label labelResume = new Label(Assets.languagesBundle.get("resume"), Assets.labelStyleSmall);
        labelResume.setWrap(true);
        labelResume.setAlignment(Align.center);
        labelResume.setPosition(getWidth() / 2f - labelResume.getWidth() / 2f, 155);
        screen.addPressEffect(labelResume);
        labelResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreen gameScreen = (GameScreen) screen;
                remove();
                gameScreen.setRunning();
            }
        });

        final Label labelMainMenu = new Label(Assets.languagesBundle.get("menu"), Assets.labelStyleSmall);
        labelMainMenu.setWrap(true);
        labelMainMenu.setAlignment(Align.center);
        labelMainMenu.setPosition(getWidth() / 2f - labelMainMenu.getWidth() / 2f, 65);
        screen.addPressEffect(labelMainMenu);
        labelMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(MainMenuScreen.class, screen.game);
            }
        });

        addAction(Actions.sequence(Actions.scaleTo(1, 1, .2f),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        addActor(labelMainMenu);
                        addActor(labelResume);
                    }
                })));
    }
}
