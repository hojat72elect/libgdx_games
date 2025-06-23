package com.nopalsoft.impossibledial.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.impossibledial.Achievements;
import com.nopalsoft.impossibledial.Assets;
import com.nopalsoft.impossibledial.MainGame;
import com.nopalsoft.impossibledial.Settings;
import com.nopalsoft.impossibledial.handlers.GoogleGameServicesHandler;
import com.nopalsoft.impossibledial.objetos.Arrow;
import com.nopalsoft.impossibledial.objetos.ArrowEasy;
import com.nopalsoft.impossibledial.objetos.ArrowHard;
import com.nopalsoft.impossibledial.scene2d.CountDown;
import com.nopalsoft.impossibledial.screens.MainMenuScreen;
import com.nopalsoft.impossibledial.screens.Screens;


public class GameScreen extends Screens {
    private final static int STATE_READY = 0;
    private final static int STATE_RUNNING = 1;
    private final static int STATE_GAMEOVER = 2;
    int state;

    public static int DIFICULTAD_EASY = 0;
    public static int DIFICULTAD_HARD = 1;
    int dificultad;


    Table tbMenu;
    Button btBack, btTryAgain, btShare;

    Label lbScore;

    int score;
    int scoreAnterior;

    Image circulo;
    Arrow oArrow;


    public GameScreen(final MainGame game, final int dificultad) {
        super(game);
        this.dificultad = dificultad;

        circulo = new Image();
        circulo.setSize(385, 385);
        circulo.setPosition(SCREEN_WIDTH / 2f - circulo.getWidth() / 2f, 200);

        if (dificultad == DIFICULTAD_EASY) {
            circulo.setDrawable(Assets.circle);
            oArrow = new ArrowEasy(SCREEN_WIDTH / 2f - Arrow.WIDTH / 2f, circulo.getY() + circulo.getHeight() / 2f);
        }
        else {
            circulo.setDrawable(Assets.circleHard);
            oArrow = new ArrowHard(SCREEN_WIDTH / 2f - Arrow.WIDTH / 2f, circulo.getY() + circulo.getHeight() / 2f);
        }

        oArrow.init();

        stage.addActor(circulo);

        lbScore = new Label("0", new LabelStyle(Assets.fontChico, Color.WHITE));
        lbScore.setColor(Color.RED);
        lbScore.setPosition(10, 735);

        int buttonSize = 90;
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
                    changeScreenWithFadeOut(GameScreen.class, game, dificultad);
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
        tbMenu.setPosition(0, 110);
        tbMenu.defaults().expandX().size(90);

        tbMenu.add(btBack);
        tbMenu.add(btTryAgain);

        // TODO fix que no se puede porque truena la app y no la aceptan en la tienda
        if (Gdx.app.getType() != ApplicationType.iOS) {
            tbMenu.add(btShare);
        }

        stage.addActor(lbScore);


        setReady();

        game.reqHandler.loadInterstitial();

    }


    /**
     * Esta variable indica si la flecha esta apuntando al cuadrante de su color, si no se preciona y se sale hacia otro cuadrante es gameOver
     */
    boolean entroASuCuadrante;

    @Override
    public void update(float delta) {
        if (Gdx.input.justTouched()) {
            if (oArrow.getFlechaApuntandoAlCuadrante() == oArrow.colorActual) {
                entroASuCuadrante = false;


                oArrow.didScore();
                score++;

                if (dificultad == DIFICULTAD_EASY) {
                    Achievements.unlockScoreAchievementsEasy(score);
                }
                else {
                    Achievements.unlockScoreAchievementsHard(score);
                }

            }
            else {
                setGameover();
            }
        }


        if (oArrow.getFlechaApuntandoAlCuadrante() == oArrow.colorActual) {
            entroASuCuadrante = true;
        }
        if (entroASuCuadrante) {
            if (oArrow.getFlechaApuntandoAlCuadrante() != oArrow.colorActual) {
                setGameover();
            }
        }

        {
            if (score > scoreAnterior) {
                scoreAnterior = score;

                lbScore.setColor(Arrow.getRandomColor());
                lbScore.setText(scoreAnterior + "");
            }
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
            stage.addActor(oArrow);

        }

    }

    private void setGameover() {
        if (state == STATE_RUNNING) {
            state = STATE_GAMEOVER;

            int bestScore;
            String leaderboardID;
            if (dificultad == DIFICULTAD_EASY) {
                Settings.setNewScoreEasy(score);
                bestScore = Settings.bestScoreEasy;
                if (game.gameServiceHandler instanceof GoogleGameServicesHandler) {
                    leaderboardID = "CgkIlp_HkPwMEAIQAg";
                }
                else {
                    leaderboardID = "BestScoreID";
                }

            }
            else {
                Settings.setNewScoreHard(score);
                bestScore = Settings.bestScoreHard;
                if (game.gameServiceHandler instanceof GoogleGameServicesHandler) {
                    leaderboardID = "CgkIlp_HkPwMEAIQCQ";
                }
                else {
                    leaderboardID = "bestScoreHardID";
                }

            }
            game.gameServiceHandler.submitScore(score, leaderboardID);


            float animationTime = .8f;

            oArrow.setGameOver();
            oArrow.addAction(Actions.sequence(Actions.alpha(0, animationTime), Actions.removeActor()));
            circulo.addAction(Actions.sequence(Actions.alpha(0, animationTime), Actions.removeActor()));


            String scoreText = Assets.idiomas.get("score");

            scoreText += "\n" + score + "\n Best\n" + bestScore;

            StringBuilder scoreTextColor = new StringBuilder();

            // HOT FIX PARA PONER ENTRE LAS LETRAS COLORES OBVIAMENTE ESTA MAL PERO nO SE ME OCURRIO OTRA COSA
            String apend[] = {"[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                    "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                    "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                    "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                    "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                    "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                    "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]"};
            for (int i = 0; i < scoreText.length(); i++) {
                scoreTextColor.append(apend[i]);
                scoreTextColor.append(scoreText.charAt(i));
            }
            scoreTextColor.append(apend[scoreText.length()]);

            Label lblScore = new Label(scoreTextColor.toString(), new LabelStyle(Assets.fontChico, Color.WHITE));
            lblScore.setAlignment(Align.center);
            lblScore.setFontScale(2.5f);
            lblScore.pack();
            lblScore.setPosition(SCREEN_WIDTH / 2f - lblScore.getWidth() / 2f, 300);
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


            game.reqHandler.showAdBanner();

            Settings.numVecesJugadas++;
            if (Settings.numVecesJugadas % 5f == 0 || score > 80) {
                game.reqHandler.showInterstitial();
            }

//            if (!game.facebookHandler.facebookIsSignedIn() && (Settings.numVecesJugadas == 5 || Settings.numVecesJugadas == 10)) {
//                new VentanaFacebook(this).show(stage);
//            }

            Achievements.unlockTimesPlayedAchievements();
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
