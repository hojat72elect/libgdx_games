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

public class DialogLevel extends Dialog {

    Button btPlay;
    Label labelBestMoves, labelBestTime;

    public DialogLevel(Screens currentScreen) {
        super(currentScreen, 350, 300, 100);

        setCloseButton();
        setTitle("Puntuaciones", .75f);

        Table tbMenu = new Table();
        tbMenu.setFillParent(true);

        btPlay = new Button(Assets.btPlay, Assets.btPlayPress);

        Image imgClock = new Image(Assets.clock);
        Image imgMoves = new Image(Assets.playerStand);

        labelBestMoves = new Label("0", new LabelStyle(Assets.fontRed, Color.WHITE));
        labelBestTime = new Label("0", new LabelStyle(Assets.fontRed, Color.WHITE));

        tbMenu.defaults().expandX();

        tbMenu.padLeft(30).padRight(30).padBottom(20).padTop(50);
        tbMenu.add(imgMoves).size(45);
        tbMenu.add(labelBestMoves);

        tbMenu.row().padTop(10);
        tbMenu.add(imgClock).size(45);
        tbMenu.add(labelBestTime);

        tbMenu.row().padTop(10);
        tbMenu.add(btPlay).colspan(2).size(60);

        addActor(tbMenu);

    }

    public void show(Stage stage, final int level, int bestMoves, int bestTime) {
        labelBestMoves.setText(bestMoves + "");
        labelBestTime.setText(bestTime + "");

        btPlay.clear();
        btPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(GameScreen.class, level, screen.game);
            }
        });

        super.show(stage);
    }

}
