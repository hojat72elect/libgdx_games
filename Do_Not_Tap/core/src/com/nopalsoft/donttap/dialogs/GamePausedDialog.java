package com.nopalsoft.donttap.dialogs;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.donttap.Assets;
import com.nopalsoft.donttap.game.GameScreen;
import com.nopalsoft.donttap.screens.MainMenuScreen;
import com.nopalsoft.donttap.screens.Screens;

public class GamePausedDialog extends Group {
    static public float fadeDuration = 0.25f;
    GameScreen screen;

    public GamePausedDialog(final GameScreen gameScreen) {
        screen = gameScreen;
        setSize(420, 300);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 260);

        Image background = new Image(Assets.fondoPuntuaciones);
        background.setSize(getWidth(), getHeight());
        addActor(background);
        getColor().a = 0;

        Label lbPaused = new Label("Pause", Assets.labelStyleBlack);
        lbPaused.setAlignment(Align.center);
        lbPaused.setFontScale(1.15f);
        lbPaused.setPosition(getWidth() / 2f - lbPaused.getWidth() / 2f, 250);

        String txtMode;

        switch (screen.mode) {
            case GameScreen.MODE_CLASSIC:
                txtMode = "Classic";
                break;
            case GameScreen.MODE_TIME:
            case GameScreen.MODE_ENDLESS:
            default:
                txtMode = "Endless";
                break;
        }

        Label labelMode = new Label(txtMode, Assets.labelStyleBlack);
        labelMode.setAlignment(Align.center);
        labelMode.setPosition(getWidth() / 2f - labelMode.getWidth() / 2f, 210);

        final TextButton btResume = new TextButton("Resume",
                Assets.textButtonStyleChico);
        btResume.setPosition(getWidth() / 2f - btResume.getWidth() / 2f, 120);
        screen.addPressEffect(btResume);
        btResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.setRunning();
                hide();
            }
        });

        final TextButton buttonMainMenu = new TextButton("Main menu",
                Assets.textButtonStyleChico);
        buttonMainMenu
                .setPosition(getWidth() / 2f - buttonMainMenu.getWidth() / 2f, 20);
        screen.addPressEffect(buttonMainMenu);
        buttonMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(MainMenuScreen.class,
                        screen.game);
            }
        });

        addActor(lbPaused);
        addActor(labelMode);
        addAction(Actions.sequence(Actions.alpha(1f, fadeDuration),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        addActor(btResume);
                        addActor(buttonMainMenu);
                    }
                })));
    }

    Image dim;

    public void show(Stage stage) {

        dim = new Image(Assets.pixelNegro);
        dim.setFillParent(true);
        dim.getColor().a = 0;
        dim.addAction(Actions.alpha(.7f, fadeDuration - .5f));

        stage.addActor(dim);

        stage.addActor(this);
    }

    private void hide() {
        dim.remove();
        remove();
    }
}
