package com.nopalsoft.donttap.dialogs;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.donttap.Assets;
import com.nopalsoft.donttap.MainDoNot;
import com.nopalsoft.donttap.Settings;
import com.nopalsoft.donttap.game.GameScreen;
import com.nopalsoft.donttap.game.WorldGame;
import com.nopalsoft.donttap.screens.MainMenuScreen;
import com.nopalsoft.donttap.screens.Screens;

public class VentanaGameOver extends Group {
    static public float fadeDuration = 0.25f;
    GameScreen screen;
    MainDoNot game;

    WorldGame oWorld;

    public VentanaGameOver(final GameScreen gameScreen) {
        screen = gameScreen;
        game = screen.game;
        oWorld = screen.oWorld;

        setSize(430, 460);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 160);

        Image background = new Image(Assets.fondoPuntuaciones);
        background.setSize(getWidth(), getHeight());
        addActor(background);
        getColor().a = 0;

        Label lbGameOver = new Label("Game over!", Assets.labelStyleBlack);
        lbGameOver.setAlignment(Align.center);
        lbGameOver.setFontScale(1.15f);
        lbGameOver.setPosition(getWidth() / 2f - lbGameOver.getWidth() / 2f,
                400);

        String txtMode, txtScore, txtNumScore;
        final String txtBestNumScore;


        switch (screen.mode) {
            case GameScreen.MODE_CLASSIC:
                txtMode = "Classic";
                txtScore = "Time";
                txtNumScore = game.formatter.format("%.1f", oWorld.time) + "s";
                if (Settings.bestTimeClassicMode >= 100100)
                    txtBestNumScore = "X";
                else
                    txtBestNumScore = game.formatter.format("%.1f", Settings.bestTimeClassicMode) + "s";

                break;
            case GameScreen.MODE_TIME:
                txtMode = "Time";
                txtScore = "Tiles";
                txtNumScore = oWorld.score + "";
                txtBestNumScore = Settings.bestScoreTimeMode + "";
                break;
            default:
            case GameScreen.MODE_ENDLESS:
                txtMode = "Endless";
                txtScore = "Tiles";
                txtNumScore = oWorld.score + "";
                txtBestNumScore = Settings.bestScoreEndlessMode + "";
                break;
        }

        Label lbMode = new Label(txtMode, Assets.labelStyleBlack);
        lbMode.setAlignment(Align.center);
        lbMode.setPosition(getWidth() / 2f - lbMode.getWidth() / 2f, 365);

        final Table scoreTable = new Table();
        scoreTable.setSize(getWidth(), 130);
        scoreTable.setY(230);
        scoreTable.padLeft(15).padRight(15);
        // scoreTable.debug();

        // ACTUAL SCORE / TIME
        Label lbScore = new Label(txtScore, Assets.labelStyleBlack);
        lbScore.setAlignment(Align.left);

        Label lblNumScore = new Label(txtNumScore, Assets.labelStyleBlack);
        lblNumScore.setAlignment(Align.right);

        // BEST TIME
        Label lbBestScore = new Label("Best", Assets.labelStyleBlack);
        lbBestScore.setAlignment(Align.left);

        Label lblNumBestScore = new Label(txtBestNumScore,
                Assets.labelStyleBlack);
        lblNumBestScore.setAlignment(Align.right);

        scoreTable.defaults().padLeft(30).padRight(30);
        scoreTable.add(lbScore).left();
        scoreTable.add(lblNumScore).right().expand();

        scoreTable.row().padTop(20);
        scoreTable.add(lbBestScore).left();
        scoreTable.add(lblNumBestScore).right().expand();

        // Facebook Twitter
        final Button btShareFacebook;
        final Button btShareTwitter;

        btShareTwitter = new Button(Assets.btTwitter);
        btShareTwitter.setSize(55, 55);
        btShareTwitter.setPosition(140, 160);
        screen.addEfectoPress(btShareTwitter);

        btShareFacebook = new Button(Assets.btFacebook);
        btShareFacebook.setSize(55, 55);
        btShareFacebook.setPosition(235, 160);
        screen.addEfectoPress(btShareFacebook);

        final TextButton btTryAgain = new TextButton("Try again",
                Assets.textButtonStyleChico);
        btTryAgain
                .setPosition(getWidth() / 2f - btTryAgain.getWidth() / 2f, 90);
        screen.addEfectoPress(btTryAgain);
        btTryAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(GameScreen.class, screen.game,
                        gameScreen.mode);
            }
        });

        final TextButton btMainMenu = new TextButton("Main menu",
                Assets.textButtonStyleChico);
        btMainMenu
                .setPosition(getWidth() / 2f - btMainMenu.getWidth() / 2f, 15);
        screen.addEfectoPress(btMainMenu);
        btMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(MainMenuScreen.class,
                        screen.game);
            }
        });

        addActor(lbGameOver);
        addActor(lbMode);
        addActor(scoreTable);
        addAction(Actions.sequence(Actions.alpha(1f, fadeDuration),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        addActor(btShareTwitter);
                        addActor(btShareFacebook);
                        addActor(btTryAgain);
                        addActor(btMainMenu);

                        btShareFacebook.remove();
                        btShareTwitter.remove();
                    }
                })));
    }

    public void show(Stage stage) {

        Image dim = new Image(Assets.pixelNegro);
        dim.setFillParent(true);
        dim.getColor().a = 0;
        dim.addAction(Actions.alpha(.7f, fadeDuration - .5f));

        stage.addActor(dim);

        stage.addActor(this);
    }
}
