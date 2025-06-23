package com.salvai.snake.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.salvai.snake.SnakeIt;
import com.salvai.snake.actors.Block;
import com.salvai.snake.actors.snake.SnakeBody;
import com.salvai.snake.enums.MovingDirection;
import com.salvai.snake.input.GameGestureDetector;
import com.salvai.snake.input.GameInputProcessor;
import com.salvai.snake.screens.helper.SpeedChooser;
import com.salvai.snake.utils.Constants;
import com.salvai.snake.utils.GameFlowManager;

import static com.salvai.snake.enums.GameState.RUNNING;
import static com.salvai.snake.enums.GameState.STARTED;
import static com.salvai.snake.utils.Constants.BACKGROUND_COLOR;

/**
 * Created by mert on 1/30/18.
 */

public class GameScreen extends ScreenAdapter {

    public SnakeIt game;
    public boolean gameOver;
    public boolean gameOverDialogToShow;
    public boolean newHighscore;

    public Array<MovingDirection> userDirections;

    //SOUND
    public Sound pointSound;
    public Sound upSound;
    public Sound downSound;
    public Sound leftRightSound;
    public Sound gameOverSound;
    public Sound newBestSound;
    private int updateCountdown;
    private int gameOverEffectCount;
    private GameFlowManager gameFlowManager;
    private Label scoreLabel;
    private Container<Label> scoreContainer;
    private Image handImage;
    private Stage gameOverStage;
    private Table gameOverTable;
    private SpeedChooser speedChooser;


    public GameScreen(final SnakeIt gameClass) {
        super();
        game = gameClass;
        game.gameState = RUNNING;
        game.score = 0;
        gameOverEffectCount = 8;

        speedChooser = new SpeedChooser(game);

        gameOverDialogToShow = true;
        newHighscore = false;

        game.stage.clear();
        gameOverStage = new Stage(game.viewport);


        loadSounds();

        game.savePreferences();

        gameOver = false;

        updateCountdown = game.worldTime;

        gameFlowManager = new GameFlowManager(this);
        userDirections = new Array<MovingDirection>();

        addActors();
        setUpTopBar();


        if (game.firstTimeOpen)
            setUpTutorial();

        setUpInputMultiplexer();

        game.stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(Constants.FADE_TIME)));
    }

    @Override
    public void render(float delta) {
        setupScreen();
        game.draw(delta, gameFlowManager.snake.snakeHead.direction, newHighscore);

        if (game.gameState == STARTED) {
            if (updateCountdown <= 0)
                if (gameOver)
                    gameOver();
                else
                    updateLogic();
            updateCountdown -= delta;
        }

        gameOverStage.act();
        gameOverStage.draw();
    }

    private void setUpTutorial() {
        handImage = new Image(game.assetsManager.manager.get(Constants.HAND_IMAGE_NAME, Texture.class));
        handImage.setBounds(game.worldWidth * 0.5f - 250, game.worldHeight * 0.5f - 150, 300, 300);
        handImage.addAction(Actions.forever(Actions.sequence(Actions.moveBy(450, 0, 1, Interpolation.pow2In), Actions.fadeOut(Constants.FADE_TIME), Actions.moveBy(-450, 0), Actions.delay(Constants.FADE_TIME * 6), Actions.fadeIn(Constants.FADE_TIME))));
        game.stage.addActor(handImage);
    }

    public void stopTutorial() {
        Action removeAction = new Action() {
            @Override
            public boolean act(float delta) {
                handImage.remove();
                return true;
            }
        };

        handImage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME * 2), removeAction));
    }

    private void setUpGameOverTable() {
        gameOverTable = new Table(game.skin);

        Button replayButton = new Button(game.skin, "play");
        replayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameOverStage.addAction(Actions.fadeOut(Constants.FADE_TIME));
                game.stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new GameScreen(game));
                        dispose();
                    }
                })));
            }
        });


        Button homeButton = new Button(game.skin, "home");
        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameOverStage.addAction(Actions.fadeOut(Constants.FADE_TIME));
                game.stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new LevelChooseScreen(game));
                        dispose();
                    }
                })));
            }
        });


        gameOverTable.setSize(game.worldWidth, game.worldHeight * 0.8f);
        gameOverTable.setPosition(0, game.worldHeight * 0.1f);

        gameOverTable.add(replayButton).spaceBottom(game.worldWidth * 0.15f).size(game.worldHeight * 0.35f).colspan(3);
        gameOverTable.row().spaceBottom(game.worldWidth * 0.1f);
        gameOverTable.add(speedChooser.slider).colspan(3).width(game.worldWidth * 0.45f).height(game.worldHeight * 0.08f);
        gameOverTable.row();
        gameOverTable.add(homeButton).size(Constants.DIALOG_BUTTON_SIZE * 0.7f).colspan(3);

        gameOverTable.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(Constants.FADE_TIME)));
        gameOverStage.addActor(gameOverTable);
    }

    private void loadSounds() {
        pointSound = game.assetsManager.manager.get(Constants.POINT_SFX, Sound.class);
        upSound = game.assetsManager.manager.get(Constants.UP_SFX, Sound.class);
        downSound = game.assetsManager.manager.get(Constants.DOWN_SFX, Sound.class);
        leftRightSound = game.assetsManager.manager.get(Constants.LEFT_RIGHT_SFX, Sound.class);
        gameOverSound = game.assetsManager.manager.get(Constants.GAME_OVER_SFX, Sound.class);
        newBestSound = game.assetsManager.manager.get(Constants.NEW_BEST_SFX, Sound.class);
    }


    private void setUpInputMultiplexer() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GameInputProcessor(this));
        inputMultiplexer.addProcessor(new GestureDetector(new GameGestureDetector(this)));
        inputMultiplexer.addProcessor(game.stage);
        inputMultiplexer.addProcessor(gameOverStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void addActors() {
        for (Block block : gameFlowManager.baseBlocks)
            game.stage.addActor(block);
        for (Block block : gameFlowManager.blocks)
            game.stage.addActor(block);
        game.stage.addActor(gameFlowManager.apple);
        game.stage.addActor(gameFlowManager.snake.snakeHead);
    }


    public void updateLogic() {
        SnakeBody newBody = gameFlowManager.update(userDirections.size > 0 ? userDirections.first() : null);
        if (newBody != null)
            game.stage.addActor(newBody);

        if (userDirections.size > 0)
            userDirections.removeIndex(0);

        updateCountdown = game.worldTime;
    }


    private void gameOver() {

        if (gameOverEffectCount > 0) {
            if (gameOverEffectCount % 2 == 0)
                gameFlowManager.snake.hide();
            else
                gameFlowManager.snake.show();
            gameOverEffectCount--;
        } else if (game.firstTimeOpen) {
            game.firstTimeOpen = false;
            gameOverDialogToShow = false;
            game.savePreferences();
            gameOverStage.addAction(Actions.fadeOut(Constants.FADE_TIME));
            game.stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new LevelChooseScreen(game));
                    dispose();
                }
            })));
        } else if (gameOverDialogToShow) {

            if (newHighscore) {
                scoreContainer.addAction(Actions.forever(Actions.sequence(Actions.scaleBy(Constants.SCORE_SCALE, Constants.SCORE_SCALE, Constants.HIGH_SCORE_DURATION,
                        Constants.INTERPOLATION), Actions.scaleBy(-Constants.SCORE_SCALE, -Constants.SCORE_SCALE, Constants.HIGH_SCORE_DURATION, Constants.INTERPOLATION))));

            }

            setUpGameOverTable();
            gameOverDialogToShow = false;
        }

        updateCountdown = Constants.WORLD_GAME_OVER_TIME;
    }


    private void setupScreen() {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.camera.update();
    }


    private void setUpTopBar() {
        scoreLabel = new Label("0", game.skin, "default");
        scoreLabel.setAlignment(Align.right);
        scoreLabel.setFontScale(2f);

        scoreContainer = new Container<Label>(scoreLabel);
        scoreContainer.setTransform(true);
        scoreContainer.setSize(game.worldWidth * 0.2f, game.worldWidth * 0.1f);
        scoreContainer.setOrigin(scoreContainer.getWidth() / 2, scoreContainer.getHeight() / 2);
        scoreContainer.setPosition(game.worldWidth * 0.5f - scoreContainer.getWidth() * 0.5f, game.worldHeight - game.worldWidth * 0.15f);
        game.stage.addActor(scoreContainer);
    }

    public void updateScoreLabel() {
        scoreContainer.addAction(Actions.sequence(Actions.scaleBy(Constants.SCORE_SCALE, Constants.SCORE_SCALE, Constants.SCORE_DURATION, Constants.INTERPOLATION), Actions.run(new Runnable() {
            @Override
            public void run() {
                scoreLabel.setText("" + game.score);
            }
        }), Actions.scaleBy(-Constants.SCORE_SCALE, -Constants.SCORE_SCALE, Constants.SCORE_DURATION, Constants.INTERPOLATION)));
    }


    @Override
    public void pause() {
        game.savePreferences();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void resume() {
        game.getPreferences();
        Gdx.input.setCatchBackKey(true);
    }
}
