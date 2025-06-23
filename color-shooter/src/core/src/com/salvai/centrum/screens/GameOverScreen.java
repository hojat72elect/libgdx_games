package com.salvai.centrum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.salvai.centrum.CentrumGameClass;
import com.salvai.centrum.enums.GameType;
import com.salvai.centrum.input.CatchBackKeyProcessor;
import com.salvai.centrum.utils.Constants;
import com.salvai.centrum.utils.GameColorPalette;


public class GameOverScreen extends ScreenAdapter {

    public Stage stage;
    private CentrumGameClass game;
    private Table table;
    private Label scoreLabel;
    private float height;
    private float width;
    private Button nextLevelButton;
    private Button retryButton;
    private Button leftBottomButton;
    private Button homeButton;
    private Image leftStar;
    private Image centreStar;
    private Image rightStar;

    public GameOverScreen(CentrumGameClass gameClass) {
        game = gameClass;

        leftStar = new Image(game.assetsManager.manager.get(Constants.LEVEL_STARS_IMAGE_NAME, Texture.class));
        centreStar = new Image(game.assetsManager.manager.get(Constants.LEVEL_STARS_IMAGE_NAME, Texture.class));
        rightStar = new Image(game.assetsManager.manager.get(Constants.LEVEL_STARS_IMAGE_NAME, Texture.class));


        if (game.gameType == GameType.LEVEL) {
            levelUnlock();
            setStarsColor();
        } else
            assignHighscore();

        game.savePreferences();


        height = Gdx.graphics.getHeight() * 0.8f;
        width = Gdx.graphics.getWidth() * 0.6f;

        stage = new Stage();

        setUpMainButtons();

        if (game.gameType == GameType.ENDLESS)
            setUpEndlessTable();
        else
            setUpLevelTable();

        stage.addActor(table);


        Gdx.input.setCatchBackKey(true);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new CatchBackKeyProcessor(game, this)); // Your screen
        Gdx.input.setInputProcessor(multiplexer);

        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(Constants.FADE_TIME)));
    }

    private void assignHighscore() {
        boolean newHighscore = false;
        //save highscore
        if (game.highScore < game.score) {
            game.highScore = game.score;
            game.preferences.putInteger("best", game.score);
            game.preferences.flush();
            newHighscore = true;
        }
    }

    private void setStarsColor() {
        if (game.levelSucceed && game.score == game.getCurrentLevel().thirdStarScore) {
            leftStar.setColor(GameColorPalette.BASIC[2]);
            centreStar.setColor(GameColorPalette.BASIC[2]);
            rightStar.setColor(GameColorPalette.BASIC[2]);
        } else if (game.levelSucceed && game.score >= game.getCurrentLevel().secondStarScore) {
            leftStar.setColor(GameColorPalette.BASIC[2]);
            centreStar.setColor(GameColorPalette.BASIC[2]);
        } else if (game.levelSucceed)
            leftStar.setColor(GameColorPalette.BASIC[2]);
    }

    private void setUpLevelTable() {
        scoreLabel = new Label("LEVEL " + (game.level + 1), game.skin, "default");
        scoreLabel.setAlignment(Align.center);

        table = new Table(game.skin);
        table.setSize(width, height);
        table.setPosition(Gdx.graphics.getWidth() * 0.5f - width * 0.5f, Gdx.graphics.getHeight() * 0.5f - height * 0.5f);
        table.defaults().height(height * 0.1f).expandX();


        table.add(scoreLabel).colspan(6).fillX();
        table.row().spaceBottom(height * .05f);
        table.add(leftStar).colspan(2).width(height * 0.2f).height(height * 0.2f).padRight(-width * 0.1f);
        table.add(centreStar).colspan(2).width(height * 0.2f).height(height * 0.2f);
        table.add(rightStar).colspan(2).width(height * 0.2f).height(height * 0.2f).padLeft(-width * 0.1f);
        table.row().spaceBottom(height * .3f);
        table.add(retryButton).width(height * .2f).height(height * 0.2f).colspan(3);
        table.add(nextLevelButton).width(height * 0.2f).height(height * 0.2f).colspan(3);
        table.row();
        table.add(leftBottomButton).width(height * 0.1f).colspan(2);
        table.add(homeButton).width(height * 0.1f).colspan(2);
    }

    private void setUpEndlessTable() {
        scoreLabel = new Label("SCORE " + game.score, game.skin, "default");
        scoreLabel.setAlignment(Align.center);

        table = new Table(game.skin);
        table.setSize(width, height);
        table.setPosition(Gdx.graphics.getWidth() * 0.5f - width * 0.5f, Gdx.graphics.getHeight() * 0.5f - height * 0.5f);
        table.defaults().height(height * 0.1f).expandX();

        table.add(scoreLabel).colspan(3).spaceBottom(height * 0.2f).fillX();
        table.row();
        table.add(retryButton).width(height * 0.3f).height(height * 0.3f).colspan(3).spaceBottom(height * 0.25f);
        table.row();
        table.add(leftBottomButton).width(height * 0.1f);
        table.add(homeButton).width(height * 0.1f);
    }

    private void setUpMainButtons() {

        retryButton = new Button(game.skin, "replay");
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new GameScreen(game));
                        dispose();
                    }
                })));
            }
        });

        if (game.level + 1 < game.levelStars.length) {
            if (game.levelStars[game.level + 1] == -1)
                nextLevelButton = new Button(game.skin, "locked-white");
            else {
                nextLevelButton = new Button(game.skin, "play");
                nextLevelButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                game.level++;
                                game.setScreen(new GameScreen(game));
                                dispose();
                            }
                        })));

                    }
                });
            }
        }


        if (game.gameType == GameType.ENDLESS) {
            leftBottomButton = new Button(game.skin, "highscore");
            leftBottomButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // TODO use local highscore
                    // if (game.playServices.isSessionActive())
                    //     try {
                    //         game.playServices.showLeaderboards(Keyz.LEADERBOARD_ID);
                    //     } catch (GameServiceException e) {
                    //         e.printStackTrace();
                    //     }
                    // else
                    //     game.playServices.logIn();
                }
            });
        } else {
            leftBottomButton = new Button(game.skin, "levels");
            leftBottomButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            game.gameType = GameType.LEVEL;
                            game.setScreen(new LevelChooseScreen(game));
                            dispose();
                        }
                    })));
                }
            });
        }

        homeButton = new Button(game.skin, "home");
        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new MenuScreen(game));
                        dispose();
                    }
                })));
            }
        });

    }

    private void levelUnlock() {
        //unlock nextLevel
        if (game.levelSucceed) {
            if (game.level + 1 < Constants.MAX_LEVEL && game.levelStars[game.level + 1] == -1)
                game.levelStars[game.level + 1] = 0;
            //assign stars
            if (game.score == game.getCurrentLevel().thirdStarScore && game.levelStars[game.level] < 3)
                game.levelStars[game.level] = 3;
            else if (game.score >= game.getCurrentLevel().secondStarScore && game.levelStars[game.level] < 2)
                game.levelStars[game.level] = 2;
            else if (game.levelStars[game.level] < 1)
                game.levelStars[game.level] = 1;
        }
    }


    @Override
    public void render(float delta) {
        setupScreen();
        game.batch.begin();
        game.drawBackground(delta);
        game.batch.end();
        stage.act(delta);
        stage.draw();
    }

    private void setupScreen() {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
