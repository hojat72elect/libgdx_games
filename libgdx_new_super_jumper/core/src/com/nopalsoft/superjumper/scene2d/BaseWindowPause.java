package com.nopalsoft.superjumper.scene2d;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.game.GameScreen;
import com.nopalsoft.superjumper.game.WorldGame;
import com.nopalsoft.superjumper.screens.MainMenuScreen;

public class BaseWindowPause extends BaseWindow {

    TextButton buttonMenu, buttonResume;
    WorldGame worldGame;

    public BaseWindowPause(final GameScreen currentScreen) {
        super(currentScreen, 350, 280, 300);
        worldGame = currentScreen.oWorld;

        Label labelShop = new Label("Pause", Assets.labelStyleLarge);
        labelShop.setFontScale(1.5f);
        labelShop.setAlignment(Align.center);
        labelShop.setPosition(getWidth() / 2f - labelShop.getWidth() / 2f, 230);
        addActor(labelShop);

        initializeButtons();

        Table content = new Table();

        content.defaults().expandX().uniform().fill();

        content.add(buttonResume);
        content.row().padTop(20);
        content.add(buttonMenu);

        content.pack();
        content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 50);

        addActor(content);
    }

    private void initializeButtons() {
        buttonMenu = new TextButton("Menu", Assets.textButtonStyleLarge);
        buttonMenu.pad(15);

        screen.addPressEffect(buttonMenu);
        buttonMenu.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                hide();
                screen.changeScreenWithFadeOut(MainMenuScreen.class, game);
            }
        });

        buttonResume = new TextButton("Resume", Assets.textButtonStyleLarge);
        buttonResume.pad(15);

        screen.addPressEffect(buttonResume);
        buttonResume.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                hide();
            }
        });
    }

    @Override
    public void show(Stage stage) {
        super.show(stage);
    }

    @Override
    public void hide() {
        ((GameScreen) screen).setRunning();
        super.hide();
    }
}
