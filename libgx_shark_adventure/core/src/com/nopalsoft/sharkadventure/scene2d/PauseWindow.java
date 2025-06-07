package com.nopalsoft.sharkadventure.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.sharkadventure.Assets;
import com.nopalsoft.sharkadventure.SharkAdventureGame;
import com.nopalsoft.sharkadventure.game.GameScreen;
import com.nopalsoft.sharkadventure.screens.Screens;

public class PauseWindow extends Group {
    public static final float ANIMATION_DURATION = .3f;

    protected GameScreen screen;
    protected SharkAdventureGame game;

    private boolean isVisible = false;

    Button buttonPlay, buttonRefresh, buttonHome;

    public PauseWindow(GameScreen currentScreen) {
        setSize(300, 300);
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 80);
        screen = currentScreen;
        game = currentScreen.game;
        setBackGround();

        Table titleTable = new Table();
        titleTable.setSize(getWidth() - 80, 50);
        titleTable.setPosition(getWidth() / 2f - titleTable.getWidth() / 2f, getHeight() - 30);
        titleTable.setBackground(Assets.backgroundTitulo);

        Label titleLabel = new Label("Paused", Assets.lblStyle);

        titleTable.add(titleLabel).fill().padBottom(10);
        addActor(titleTable);

        buttonPlay = new Button(Assets.btDer, Assets.btDerPress);
        buttonPlay.setSize(70, 70);
        buttonPlay.setPosition(getWidth() / 2f - buttonPlay.getWidth() / 2f, 170);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                screen.setRunning(false);
            }
        });

        buttonRefresh = new Button(Assets.btRefresh, Assets.btRefreshPress);
        buttonRefresh.setSize(70, 70);
        buttonRefresh.setPosition(getWidth() / 2f + 25, 80);
        buttonRefresh.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                screen.game.setScreen(new GameScreen(game, false));
            }
        });

        buttonHome = new Button(Assets.btHome, Assets.btHomePress);
        buttonHome.setSize(70, 70);
        buttonHome.setPosition(getWidth() / 2f - buttonHome.getWidth() - 25, 80);
        buttonHome.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                screen.game.setScreen(new GameScreen(game, true));
            }
        });

        addActor(buttonPlay);
        addActor(buttonRefresh);
        addActor(buttonHome);
    }

    private void setBackGround() {
        Image img = new Image(Assets.backgroundVentana);
        img.setSize(getWidth(), getHeight());
        addActor(img);
    }

    public void show(Stage stage) {

        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setX(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f);

        setScale(.5f);
        addAction(Actions.sequence(Actions.scaleTo(1, 1, ANIMATION_DURATION)));

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
}
