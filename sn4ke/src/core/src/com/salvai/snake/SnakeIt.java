package com.salvai.snake;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.salvai.snake.enums.BlockRatio;
import com.salvai.snake.enums.GameState;
import com.salvai.snake.enums.MovingDirection;
import com.salvai.snake.levels.Level;
import com.salvai.snake.levels.LevelReader;
import com.salvai.snake.screens.MenuScreen;
import com.salvai.snake.screens.SettingsScreen;
import com.salvai.snake.screens.SplashScreen;
import com.salvai.snake.screens.background.Background;
import com.salvai.snake.utils.Constants;
import com.salvai.snake.utils.MyAssetsManager;
import com.salvai.snake.utils.Text;
import com.salvai.snake.utils.WorldUtils;


public class SnakeIt extends Game {

    public GameState gameState;

    public int score;
    public int level;
    public int selectedLevelTab;
    public int selectedColor;
    public int worldTime;

    public boolean firstTimeOpen;
    public boolean soundOn;
    public boolean vibrationOn;

    public float worldWidth;
    public float worldHeight;

    public int[] highScores;
    public Array<Level> levels;
    public OrthographicCamera camera;
    public FitViewport viewport;
    public Stage stage;
    public Stage backgroundStage;
    public Stage topBarStage;
    public WorldUtils worldUtils;
    public MyAssetsManager assetsManager;
    public Skin skin;
    private Background background;
    private Preferences preferences;


    public SnakeIt() {
        super();
    }

    @Override
    public void create() {
        if (preferences == null) preferences = Gdx.app.getPreferences(Text.GAME_NAME);

        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, camera);

        setUpGlobalVariables();
        getPreferences();

        setBlockRatio();
        setUpStages();
        setUpAssetsManager();
        setUpBackground();

        //to test
//      firstTimeOpen = true;
        readAllLevels();
        setScreen(new SplashScreen(this));
    }

    private void setUpGlobalVariables() {
        highScores = new int[Constants.MAX_LEVEL];
        levels = new Array<Level>();
        worldHeight = viewport.getWorldHeight();
        worldWidth = viewport.getWorldWidth();
    }

    private void readAllLevels() {
        LevelReader levelReader = new LevelReader();
        levels = levelReader.loadAllLevels();
    }

    private void setUpStages() {
        FillViewport backgroundViewport = new FillViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, camera);
        stage = new Stage(viewport);
        topBarStage = new Stage(viewport);
        backgroundStage = new Stage(backgroundViewport);
    }

    private void setUpAssetsManager() {
        //ASSETS MANAGER
        assetsManager = new MyAssetsManager();
        assetsManager.loadSplashScreen();
        assetsManager.manager.finishLoading();
    }

    private void setUpBackground() {

        background = new Background(assetsManager.manager.get(Constants.BLOCK_IMAGE_NAME, Texture.class), backgroundStage, assetsManager.manager.get(Constants.BACKGROUND_IMAGE, Texture.class), selectedColor);
    }

    private void setBlockRatio() {
        switch (selectedLevelTab) {
            case 1:
                worldUtils = new WorldUtils(BlockRatio.MEDIUM);
                break;
            case 2:
                worldUtils = new WorldUtils(BlockRatio.BIG);
                break;
            default:
                worldUtils = new WorldUtils(BlockRatio.SMALL);
                break;
        }
    }

    public void drawBackground(float delta, MovingDirection direction) {
        background.draw(delta, direction, worldTime, false);
    }


    //to save space
    public void savePreferences() {
        preferences.putInteger("level", level); // to save current level
        preferences.putBoolean("sound", soundOn);
        preferences.putBoolean("vibration", vibrationOn);
        preferences.putInteger("worldTime", worldTime);
        preferences.putInteger("selectedColor", selectedColor);
        preferences.putInteger("selectedLevelTab", selectedLevelTab);
        preferences.putBoolean("firstTimeOpen", firstTimeOpen);
        for (int i = 0; i < Constants.MAX_LEVEL; i++)
            preferences.putInteger("highScore" + i, highScores[i]);
        preferences.flush();
    }

    public void getPreferences() {
        firstTimeOpen = preferences.getBoolean("firstTimeOpen", true);
        worldTime = preferences.getInteger("worldTime", 15);
        level = preferences.getInteger("level", 0);
        selectedLevelTab = preferences.getInteger("selectedLevelTab", 0);
        selectedColor = preferences.getInteger("selectedColor", 0);
        soundOn = preferences.getBoolean("sound", true);
        vibrationOn = preferences.getBoolean("vibration", true);
        highScores[0] = preferences.getInteger("highScore" + 0, 0);
        for (int i = 1; i < Constants.MAX_LEVEL; i++)
            highScores[i] = preferences.getInteger("highScore" + i, 0);
    }


    public Level getCurrentLevel() {
        return levels.get(level);
    }


    public void setUpTopBar(Constants.SCREEN screen) {
        topBarStage.clear();
        Button backButton = new Button(skin, "back");
        backButton.setBounds(worldWidth * 0.05f, worldHeight - worldWidth * 0.15f, worldWidth * 0.1f, worldWidth * 0.1f);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getScreen() instanceof MenuScreen) {
                    ((MenuScreen) getScreen()).playButton.addAction(Actions.fadeOut(Constants.FADE_TIME));
                    ((MenuScreen) getScreen()).exitDialog.show(stage);
                } else
                    stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            setScreen(new MenuScreen(SnakeIt.this));
                        }
                    })));
            }
        });

        // hide for ios
        if (Gdx.app.getType() != Application.ApplicationType.iOS || screen != Constants.SCREEN.MENU) {
            topBarStage.addActor(backButton);
        }

        if (screen != Constants.SCREEN.SETTINGS) {
            final Button settingsButton = new Button(skin, "settings");
            settingsButton.setBounds(worldWidth - (worldWidth * 0.15f), worldHeight - worldWidth * 0.15f, worldWidth * 0.1f, worldWidth * 0.1f);
            settingsButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    settingsButton.addAction(Actions.fadeOut(Constants.FADE_TIME));
                    stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            setScreen(new SettingsScreen(SnakeIt.this));
                        }
                    })));
                }
            });
            topBarStage.addActor(settingsButton);
        }
    }

    public void selectColor(int index) {
        selectedColor = index;
        changeBackground(selectedColor);
        savePreferences();
    }


    private void changeBackground(int index) {
        background.changeBackground(index);
    }


    public void draw(float delta) {
        background.draw(delta, MovingDirection.UP, worldTime, false);

        stage.act(delta);
        stage.getViewport().apply();
        stage.draw();

        topBarStage.act(delta);
        topBarStage.getViewport().apply();
        topBarStage.draw();
    }


    public void draw(float delta, MovingDirection direction, boolean newBest) {
        background.draw(delta, direction, worldTime, newBest);
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    public void setSelectedLevelTab(int selectedLevelTab) {
        this.selectedLevelTab = selectedLevelTab;
        setBlockRatio();
    }

    @Override
    public void dispose() {
        savePreferences();
        stage.dispose();
        topBarStage.dispose();
        backgroundStage.dispose();
        assetsManager.manager.dispose();
    }

    @Override
    public void resume() {
        super.resume();
    }
}