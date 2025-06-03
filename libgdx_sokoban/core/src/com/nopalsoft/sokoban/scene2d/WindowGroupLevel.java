package com.nopalsoft.sokoban.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.game.GameScreen;
import com.nopalsoft.sokoban.screens.Screens;

public class WindowGroupLevel extends WindowGroup {

    Button buttonPlay;
    Label labelBestMoves, labelBestTime;

    public WindowGroupLevel(Screens currentScreen) {
        super(currentScreen, 350, 300, 100);

        setCloseButton();
        setTitle("Puntuaciones", .75f);

        Table tableMenu = new Table();
        tableMenu.setFillParent(true);

        buttonPlay = new Button(Assets.buttonPlayDrawable, Assets.buttonPlayPressedDrawable);

        Image imageClock = new Image(Assets.clockDrawable);
        Image imageMoves = new Image(Assets.playerStand);

        labelBestMoves = new Label("0", new LabelStyle(Assets.fontRed, Color.WHITE));
        labelBestTime = new Label("0", new LabelStyle(Assets.fontRed, Color.WHITE));

        tableMenu.defaults().expandX();

        tableMenu.padLeft(30).padRight(30).padBottom(20).padTop(50);
        tableMenu.add(imageMoves).size(45);
        tableMenu.add(labelBestMoves);

        tableMenu.row().padTop(10);
        tableMenu.add(imageClock).size(45);
        tableMenu.add(labelBestTime);

        tableMenu.row().padTop(10);
        tableMenu.add(buttonPlay).colspan(2).size(60);

        addActor(tableMenu);
    }

    public void show(Stage stage, final int level, int bestMoves, int bestTime) {
        labelBestMoves.setText(bestMoves + "");
        labelBestTime.setText(bestTime + "");

        buttonPlay.clear();
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(GameScreen.class, level, screen.game);
            }
        });

        super.show(stage);
    }
}
