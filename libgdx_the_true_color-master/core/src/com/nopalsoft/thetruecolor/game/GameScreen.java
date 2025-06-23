package com.nopalsoft.thetruecolor.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.thetruecolor.Achievements;
import com.nopalsoft.thetruecolor.Assets;
import com.nopalsoft.thetruecolor.MainTheTrueColor;
import com.nopalsoft.thetruecolor.Settings;
import com.nopalsoft.thetruecolor.objetos.Palabra;
import com.nopalsoft.thetruecolor.scene2d.CountDown;
import com.nopalsoft.thetruecolor.scene2d.ProgressbarTimer;
import com.nopalsoft.thetruecolor.scene2d.VentanaFacebook;
import com.nopalsoft.thetruecolor.screens.MainMenuScreen;
import com.nopalsoft.thetruecolor.screens.Screens;

public class GameScreen extends Screens {
    public static int STATE_READY = 0;
    public static int STATE_RUNNING = 1;
    public static int STATE_GAMEOVER = 2;
    int state;

    public static float TIME_MINIMO_POR_PALABRA = .62f;
    public static float TIME_INICIAL_POR_PALABRA = 5;
    float timeInicialPorPalabra;

    Button btTrue, btFalse;

    Table tbMenu;
    Button btBack, btTryAgain, btShare;

    Label lbScore;

    int score;
    int scoreAnterior;

    Palabra oPalabra;
    ProgressbarTimer timerPalabra;

    public GameScreen(final MainTheTrueColor game) {
        super(game);

        oPalabra = new Palabra();

        lbScore = new Label("0", new LabelStyle(Assets.fontChico, Color.WHITE));
        lbScore.setColor(Color.RED);
        lbScore.setPosition(10, 735);

        timeInicialPorPalabra = TIME_INICIAL_POR_PALABRA;

        timerPalabra = new ProgressbarTimer(SCREEN_WIDTH / 2f - ProgressbarTimer.WIDTH / 2f, 300);

        int buttonSize = 90;

        btTrue = new Button(Assets.btTrue);
        addEfectoPress(btTrue);
        btTrue.setSize(buttonSize, buttonSize);
        btTrue.setPosition(240 + 80, 60);
        btTrue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                checarPalabra(true);
            }
        });

        btFalse = new Button(Assets.btFalse);
        addEfectoPress(btFalse);
        btFalse.setSize(buttonSize, buttonSize);
        btFalse.setPosition(240 - 170, 60);
        btFalse.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                checarPalabra(false);

            }

        });

        btBack = new Button(Assets.btBack);
        addEfectoPress(btBack);
        btBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!btBack.isDisabled()) {
                    changeScreenWithFadeOut(MainMenuScreen.class, game);
                }
            }
        });

        btTryAgain = new Button(Assets.btTryAgain);
        addEfectoPress(btTryAgain);
        btTryAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!btTryAgain.isDisabled()) {
                    changeScreenWithFadeOut(GameScreen.class, game);
                }
            }
        });

        btShare = new Button(Assets.btShare);
        addEfectoPress(btShare);
        btShare.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!btShare.isDisabled()) {
                    game.reqHandler.shareAPK();
                }

            }
        });

        tbMenu = new Table();
        tbMenu.setSize(SCREEN_WIDTH, 90);
        tbMenu.setPosition(0, 60);
        tbMenu.defaults().expandX().size(90);

        tbMenu.add(btBack);
        tbMenu.add(btTryAgain);

        // TODO fix que no se puede porque truena la app y no la aceptan en la tienda
        if (Gdx.app.getType() != ApplicationType.iOS) {
            tbMenu.add(btShare);
        }

        stage.addActor(btTrue);
        stage.addActor(btFalse);
        stage.addActor(lbScore);

        setReady();

        game.reqHandler.loadInterstitial();

    }

    public void createNewPalabra() {
        oPalabra.init();

        timerPalabra.remove();
        timerPalabra.init(oPalabra.getColorActualPalabra(), timeInicialPorPalabra);
        stage.addActor(timerPalabra);
        stage.addActor(oPalabra.imagen);
    }

    private void checarPalabra(boolean seleccion) {
        if (state == STATE_RUNNING) {

            if ((oPalabra.color == oPalabra.texto && seleccion) || (oPalabra.color != oPalabra.texto && !seleccion)) {
                score++;
                Achievements.unlockScoreAchievements(score);

                if (score < 10) {
                    timeInicialPorPalabra -= .14f;// 1.8seg menos
                }
                else if (score < 40) {
                    timeInicialPorPalabra -= .05f;// 1.5seg menos
                }
                else if (score < 70) {
                    timeInicialPorPalabra -= .015f;// .54seg menos
                }
                else {
                    timeInicialPorPalabra -= .0075f;
                }

                if (timeInicialPorPalabra < TIME_MINIMO_POR_PALABRA) {
                    timeInicialPorPalabra = TIME_MINIMO_POR_PALABRA;
                }
                createNewPalabra();
            }
            else {
                setGameover();
            }
        }

    }

    @Override
    public void update(float delta) {

        if (score > scoreAnterior) {
            scoreAnterior = score;

            lbScore.setColor(Palabra.getRandomColor());
            lbScore.setText(scoreAnterior + "");
        }

        if (timerPalabra.timeIsOver) {
            setGameover();
        }
    }

    @Override
    public void draw(float delta) {
        batcher.begin();
        batcher.draw(Assets.header, 0, 780, 480, 20);
        batcher.draw(Assets.header, 0, 0, 480, 20);

        batcher.end();
    }

    private void setReady() {
        state = STATE_READY;
        stage.addActor(new CountDown(this));
    }

    public void setRunning() {
        if (state == STATE_READY) {
            state = STATE_RUNNING;
            createNewPalabra();
        }

    }

    private void setGameover() {
        if (state == STATE_RUNNING) {
            state = STATE_GAMEOVER;

            float animationTime = .8f;

            btFalse.addAction(Actions.sequence(Actions.alpha(0, animationTime), Actions.removeActor()));
            btTrue.addAction(Actions.sequence(Actions.alpha(0, animationTime), Actions.removeActor()));

            timerPalabra.timeIsOver = true;
            timerPalabra.addAction(Actions.sequence(Actions.alpha(0, animationTime), Actions.removeActor()));

            oPalabra.imagen.addAction(Actions.sequence(Actions.alpha(0, animationTime), Actions.removeActor()));

            String scoreText = Assets.idiomas.get("score");

            StringBuilder scoreTextColor = new StringBuilder();

            // HOT FIX PARA PONER ENTRE LAS LETRAS COLORES OBVIAMENTE ESTA MAL PERO nO SE ME OCURRIO OTRA COSA
            String apend[] = {"[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                    "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                    "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                    "[ORANGE]"};
            for (int i = 0; i < scoreText.length(); i++) {
                scoreTextColor.append(apend[i]);
                scoreTextColor.append(scoreText.charAt(i));
            }
            scoreTextColor.append(apend[scoreText.length()]);

            Label lblScore = new Label(scoreTextColor.toString() + "\n" + score, new LabelStyle(Assets.fontChico, Color.WHITE));
            lblScore.setAlignment(Align.center);
            lblScore.setFontScale(2.5f);
            lblScore.pack();
            lblScore.setPosition(SCREEN_WIDTH / 2f - lblScore.getWidth() / 2f, 380);
            lblScore.getColor().a = 0;

            lblScore.addAction(Actions.sequence(Actions.delay(1), Actions.alpha(1, animationTime)));

            tbMenu.getColor().a = 0;

            btBack.setDisabled(true);
            btTryAgain.setDisabled(true);
            btShare.setDisabled(true);

            tbMenu.addAction(Actions.sequence(Actions.delay(1), Actions.alpha(1, animationTime), Actions.run(new Runnable() {

                @Override
                public void run() {
                    btBack.setDisabled(false);
                    btTryAgain.setDisabled(false);
                    btShare.setDisabled(false);
                }
            })));

            stage.addActor(lblScore);
            stage.addActor(tbMenu);
            Settings.setNewScore(score);
            game.facebookHandler.facebookSubmitScore(score);
            game.gameServiceHandler.submitScore(score);

            game.reqHandler.showAdBanner();

            Settings.numVecesJugadas++;
            if (Settings.numVecesJugadas % 7f == 0 || score > 80) {
                game.reqHandler.showInterstitial();
            }


            if (!game.facebookHandler.facebookIsSignedIn() && (Settings.numVecesJugadas == 5 || Settings.numVecesJugadas == 10)) {
                new VentanaFacebook(this).show(stage);
            }

            Achievements.unlockTimesPlayedAchievements(Settings.numVecesJugadas);
            Settings.save();
        }
    }

    @Override
    public void hide() {
        super.hide();
        game.reqHandler.hideAdBanner();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK | keycode == Keys.ESCAPE) {
            changeScreenWithFadeOut(MainMenuScreen.class, game);
            return true;
        }
        return super.keyDown(keycode);
    }

}
