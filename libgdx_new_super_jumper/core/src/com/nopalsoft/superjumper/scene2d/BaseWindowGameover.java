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

        Label lbShop = new Label("Game over!", Assets.labelStyleGrande);
        lbShop.setFontScale(1.5f);
        lbShop.setAlignment(Align.center);
        lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 350);
        addActor(lbShop);

        initButtons();

        final Table scoreTable = new Table();
        scoreTable.setSize(getWidth(), 130);
        scoreTable.setY(230);

        Label lbScore = new Label("Score", Assets.labelStyleChico);
        lbScore.setAlignment(Align.left);

        Label lblNumScore = new Label(worldGame.maxDistance + "", Assets.labelStyleChico);
        lblNumScore.setAlignment(Align.right);

        Label lbBestScore = new Label("Best Score", Assets.labelStyleChico);
        lbScore.setAlignment(Align.left);

        Label lbBestNumScore = new Label(Settings.bestScore + "", Assets.labelStyleChico);
        lblNumScore.setAlignment(Align.right);

        scoreTable.pad(10);
        scoreTable.add(lbScore).left();
        scoreTable.add(lblNumScore).right().expand();

        scoreTable.row();
        scoreTable.add(lbBestScore).left();
        scoreTable.add(lbBestNumScore).right().expand();

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

    private void initButtons() {
        buttonMenu = new TextButton("Menu", Assets.textButtonStyleGrande);
        buttonMenu.pad(15);

        screen.addEfectoPress(buttonMenu);
        buttonMenu.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                hide();
                screen.changeScreenWithFadeOut(MainMenuScreen.class, game);
            }
        });

        buttonTryAgain = new TextButton("Try again", Assets.textButtonStyleGrande);
        buttonTryAgain.pad(15);

        screen.addEfectoPress(buttonTryAgain);
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
