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
import com.nopalsoft.thetruecolor.game_objects.ColoredWord;
import com.nopalsoft.thetruecolor.scene2d.CountDown;
import com.nopalsoft.thetruecolor.scene2d.ProgressbarTimer;
import com.nopalsoft.thetruecolor.screens.BaseScreen;
import com.nopalsoft.thetruecolor.screens.MainMenuScreen;

public class GameScreen extends BaseScreen {
    public static int STATE_READY = 0;
    public static int STATE_RUNNING = 1;
    public static int STATE_GAMEOVER = 2;
    int state;

    public static float MINIMUM_TIME_PER_WORD = .62f;
    public static float INITIAL_TIME_PER_WORD = 5;
    float initialTimePerWord;

    Button buttonTrue, buttonFalse;

    Table tableMenu;
    Button buttonBack, buttonTryAgain, buttonShare;

    Label labelScore;

    int score;
    int previousScore;

    ColoredWord word;
    ProgressbarTimer wordTimer;

    public GameScreen(final MainTheTrueColor game) {
        super(game);

        word = new ColoredWord();

        labelScore = new Label("0", new LabelStyle(Assets.fontSmall, Color.WHITE));
        labelScore.setColor(Color.RED);
        labelScore.setPosition(10, 735);

        initialTimePerWord = INITIAL_TIME_PER_WORD;

        wordTimer = new ProgressbarTimer(SCREEN_WIDTH / 2f - ProgressbarTimer.WIDTH / 2f, 300);

        int buttonSize = 90;

        buttonTrue = new Button(Assets.buttonTrueDrawable);
        addPressEffect(buttonTrue);
        buttonTrue.setSize(buttonSize, buttonSize);
        buttonTrue.setPosition(240 + 80, 60);
        buttonTrue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                checarPalabra(true);
            }
        });

        buttonFalse = new Button(Assets.buttonFalseDrawable);
        addPressEffect(buttonFalse);
        buttonFalse.setSize(buttonSize, buttonSize);
        buttonFalse.setPosition(240 - 170, 60);
        buttonFalse.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                checarPalabra(false);
            }
        });

        buttonBack = new Button(Assets.buttonBackDrawable);
        addPressEffect(buttonBack);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!buttonBack.isDisabled()) {
                    changeScreenWithFadeOut(MainMenuScreen.class, game);
                }
            }
        });

        buttonTryAgain = new Button(Assets.buttonTryAgainDrawable);
        addPressEffect(buttonTryAgain);
        buttonTryAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!buttonTryAgain.isDisabled()) {
                    changeScreenWithFadeOut(GameScreen.class, game);
                }
            }
        });

        buttonShare = new Button(Assets.buttonShareDrawable);
        addPressEffect(buttonShare);

        tableMenu = new Table();
        tableMenu.setSize(SCREEN_WIDTH, 90);
        tableMenu.setPosition(0, 60);
        tableMenu.defaults().expandX().size(90);

        tableMenu.add(buttonBack);
        tableMenu.add(buttonTryAgain);

        if (Gdx.app.getType() != ApplicationType.iOS) {
            tableMenu.add(buttonShare);
        }

        stage.addActor(buttonTrue);
        stage.addActor(buttonFalse);
        stage.addActor(labelScore);

        setReady();

    }

    public void createNewPalabra() {
        word.initialize();

        wordTimer.remove();
        wordTimer.initialize(word.getColorActualPalabra(), initialTimePerWord);
        stage.addActor(wordTimer);
        stage.addActor(word.wordLabel);
    }

    private void checarPalabra(boolean seleccion) {
        if (state == STATE_RUNNING) {

            if ((word.color == word.wordText && seleccion) || (word.color != word.wordText && !seleccion)) {
                score++;
                Achievements.unlockScoreAchievements();

                if (score < 10) {
                    initialTimePerWord -= .14f;// 1.8seg menos
                } else if (score < 40) {
                    initialTimePerWord -= .05f;// 1.5seg menos
                } else if (score < 70) {
                    initialTimePerWord -= .015f;// .54seg menos
                } else {
                    initialTimePerWord -= .0075f;
                }

                if (initialTimePerWord < MINIMUM_TIME_PER_WORD) {
                    initialTimePerWord = MINIMUM_TIME_PER_WORD;
                }
                createNewPalabra();
            } else {
                setGameover();
            }
        }
    }

    @Override
    public void update(float delta) {

        if (score > previousScore) {
            previousScore = score;

            labelScore.setColor(ColoredWord.getRandomColor());
            labelScore.setText(previousScore + "");
        }

        if (wordTimer.isTimeOver) {
            setGameover();
        }
    }

    @Override
    public void draw(float delta) {
        batch.begin();
        batch.draw(Assets.header, 0, 780, 480, 20);
        batch.draw(Assets.header, 0, 0, 480, 20);

        batch.end();
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

            buttonFalse.addAction(Actions.sequence(Actions.alpha(0, animationTime), Actions.removeActor()));
            buttonTrue.addAction(Actions.sequence(Actions.alpha(0, animationTime), Actions.removeActor()));

            wordTimer.isTimeOver = true;
            wordTimer.addAction(Actions.sequence(Actions.alpha(0, animationTime), Actions.removeActor()));

            word.wordLabel.addAction(Actions.sequence(Actions.alpha(0, animationTime), Actions.removeActor()));

            String scoreText = Assets.languagesBundle.get("score");

            StringBuilder scoreTextColor = new StringBuilder();

            // HOT FIX PARA PONER ENTRE LAS LETRAS COLORES OBVIAMENTE ESTA MAL PERO nO SE ME OCURRIO OTRA COSA
            String[] apend = {"[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                    "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                    "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]", "[ORANGE]", "[RED]", "[BLUE]",
                    "[ORANGE]"};
            for (int i = 0; i < scoreText.length(); i++) {
                scoreTextColor.append(apend[i]);
                scoreTextColor.append(scoreText.charAt(i));
            }
            scoreTextColor.append(apend[scoreText.length()]);

            Label lblScore = new Label(scoreTextColor + "\n" + score, new LabelStyle(Assets.fontSmall, Color.WHITE));
            lblScore.setAlignment(Align.center);
            lblScore.setFontScale(2.5f);
            lblScore.pack();
            lblScore.setPosition(SCREEN_WIDTH / 2f - lblScore.getWidth() / 2f, 380);
            lblScore.getColor().a = 0;

            lblScore.addAction(Actions.sequence(Actions.delay(1), Actions.alpha(1, animationTime)));

            tableMenu.getColor().a = 0;

            buttonBack.setDisabled(true);
            buttonTryAgain.setDisabled(true);
            buttonShare.setDisabled(true);

            tableMenu.addAction(Actions.sequence(Actions.delay(1), Actions.alpha(1, animationTime), Actions.run(new Runnable() {

                @Override
                public void run() {
                    buttonBack.setDisabled(false);
                    buttonTryAgain.setDisabled(false);
                    buttonShare.setDisabled(false);
                }
            })));

            stage.addActor(lblScore);
            stage.addActor(tableMenu);
            Settings.setNewScore(score);


            Settings.numberOfTimesPlayed++;

            Achievements.unlockTimesPlayedAchievements();
            Settings.save();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK | keycode == Keys.ESCAPE) {
            changeScreenWithFadeOut(MainMenuScreen.class, game);
            return true;
        }
        return super.keyDown(keycode);
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
