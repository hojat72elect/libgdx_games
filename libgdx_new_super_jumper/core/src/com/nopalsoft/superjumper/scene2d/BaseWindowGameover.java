package com.nopalsoft.superjumper.scene2d;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.Settings;
import com.nopalsoft.superjumper.game.GameScreen;
import com.nopalsoft.superjumper.game.WorldGame;
import com.nopalsoft.superjumper.screens.MainMenuScreen;

public class BaseWindowGameover extends BaseWindow {

    TextButton buttonMenu, buttonTryAgain;
    WorldGame worldGame;

    public BaseWindowGameover(final GameScreen currentScreen) {
        super(currentScreen, 350, 400, 250);
        worldGame = currentScreen.oWorld;

        Label labelShop = new Label("Game over!", Assets.labelStyleLarge);
        labelShop.setFontScale(1.5f);
        labelShop.setAlignment(Align.center);
        labelShop.setPosition(getWidth() / 2f - labelShop.getWidth() / 2f, 350);
        addActor(labelShop);

        initializeButtons();

        final Table scoreTable = new Table();
        scoreTable.setSize(getWidth(), 130);
        scoreTable.setY(230);

        Label labelScore = new Label("Score", Assets.labelStyleSmall);
        labelScore.setAlignment(Align.left);

        Label labelNumScore = new Label(worldGame.maxDistance + "", Assets.labelStyleSmall);
        labelNumScore.setAlignment(Align.right);

        Label labelBestScore = new Label("Best Score", Assets.labelStyleSmall);
        labelScore.setAlignment(Align.left);

        Label labelBestNumScore = new Label(Settings.bestScore + "", Assets.labelStyleSmall);
        labelNumScore.setAlignment(Align.right);

        scoreTable.pad(10);
        scoreTable.add(labelScore).left();
        scoreTable.add(labelNumScore).right().expand();

        scoreTable.row();
        scoreTable.add(labelBestScore).left();
        scoreTable.add(labelBestNumScore).right().expand();

        addActor(scoreTable);

        Table content = new Table();

        content.defaults().expandX().uniform().fill();

        content.add(buttonTryAgain);
        content.row().padTop(20);
        content.add(buttonMenu);

        content.pack();
        content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 60);

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

        buttonTryAgain = new TextButton("Try again", Assets.textButtonStyleLarge);
        buttonTryAgain.pad(15);

        screen.addPressEffect(buttonTryAgain);
        buttonTryAgain.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                hide();
                screen.changeScreenWithFadeOut(GameScreen.class, game);
            }
        });
    }

    @Override
    public void show(Stage stage) {
        super.show(stage);
    }
}
