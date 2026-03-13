package com.salvai.centrum;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.salvai.centrum.enums.GameState;
import com.salvai.centrum.enums.GameType;
import com.salvai.centrum.levels.Level;
import com.salvai.centrum.screens.Background;
import com.salvai.centrum.screens.SplashScreen;
import com.salvai.centrum.utils.Constants;
import com.salvai.centrum.utils.MyAssetsManager;
import com.salvai.centrum.utils.Text;

public class CentrumGameClass extends Game {

    public SpriteBatch batch;
    public Preferences preferences;
    public OrthographicCamera camera;
    public Viewport viewport;
    public int score;
    public int highScore;
    public GameState gameState;
    public boolean showTutorial;
    public boolean soundOn;
    public MyAssetsManager assetsManager;
    public GameType gameType;
    public int level;
    public int[] levelStars; // -1 means not unlocked, 0-3 stars, 2 unlock next level
    public Array<Level> levels;
    public boolean levelSucceed;
    public Skin skin;
    private Background background;


    public CentrumGameClass() {
        super();
    }

    public void create() {
        levelStars = new int[Constants.MAX_LEVEL];

        preferences = getPreferences();
        loadPreferences();

        levels = new Array<Level>();

        batch = new SpriteBatch();

        levelSucceed = false;

        loadAssets();

        background = new Background(assetsManager.manager.get(Constants.STAR_IMAGE_NAME, Texture.class));

        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, camera);

        this.setScreen(new SplashScreen(this));
    }

    private void loadAssets() {
        assetsManager = new MyAssetsManager();
        assetsManager.loadSplashScreen();
        assetsManager.loadImages();
        assetsManager.loadSounds();
        assetsManager.loadParticleEffect();
        assetsManager.loadSkin();
    }

    public void drawBackground(float delta) {
        background.draw(delta, batch);
    }

    public void drawPause() {
        background.drawPause(batch);
    }

    public Level getCurrentLevel() {
        return levels.get(level);
    }


    public void savePreferences() {
        preferences.putBoolean("showTutorial", showTutorial);
        preferences.putBoolean("sound", soundOn);
        preferences.putString("gameType", gameType.name());
        preferences.putInteger("level", level); // to save current level
        preferences.putInteger("best", highScore);
        for (int i = 0; i < Constants.MAX_LEVEL; i++)
            preferences.putInteger("s" + i, levelStars[i]); //to save space stars for level
        preferences.flush();
    }

    private void loadPreferences() {
        showTutorial = preferences.getBoolean("showTutorial", true);
        soundOn = preferences.getBoolean("sound", true);
        gameType = GameType.valueOf(preferences.getString("gameType", "ENDLESS"));
        level = preferences.getInteger("level", 0);
        highScore = preferences.getInteger("best", 0);
        levelStars[0] = preferences.getInteger("s" + 0, 0); //first level always unlocked
        for (int i = 1; i < Constants.MAX_LEVEL; i++)
            levelStars[i] = preferences.getInteger("s" + i, -1); //to save space stars for level
    }


    private Preferences getPreferences() {
        if (preferences == null)
            preferences = Gdx.app.getPreferences(Text.GAME_NAME);
        return preferences;
    }

    @Override
    public void dispose() {
        savePreferences();
        batch.dispose();
        assetsManager.manager.dispose();
    }
}
