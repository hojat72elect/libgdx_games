package com.nopalsoft.ponyrace.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.nopalsoft.ponyrace.AssetsHandler;
import com.nopalsoft.ponyrace.PonyRacingGame;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.game.GameScreen;

public abstract class BaseScreen extends InputAdapter implements Screen {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;

    public static final int WORLD_WIDTH = 80;
    public static final int WORLD_HEIGHT = 48;

    public PonyRacingGame game;
    public Stage stage;
    public SpriteBatch batch;

    public OrthographicCamera camera;

    public SkeletonRenderer skeletonRenderer;
    protected GlyphLayout glyphLayout;

    protected AssetsHandler assetsHandler;
    protected float screenLastStateTime;
    protected float ScreenStateTime;

    public BaseScreen(PonyRacingGame game) {
        assetsHandler = game.assetsHandler;
        stage = game.stage;
        stage.clear();
        batch = game.batch;
        glyphLayout = new GlyphLayout();
        InputMultiplexer input = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(input);
        this.game = game;
        camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        camera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);

        skeletonRenderer = new SkeletonRenderer();

        screenLastStateTime = ScreenStateTime = 0;
        if (this instanceof MainMenuScreen) {
            assetsHandler.fontGde.getData().setScale(1f);
            assetsHandler.fontChco.getData().setScale(.65f);
        } else if (this instanceof GameScreen) {
            assetsHandler.fontGde.getData().setScale(.625f);
            assetsHandler.fontChco.getData().setScale(.55f);
        } else if (this instanceof WorldMapTiledScreen) {
            assetsHandler.fontGde.getData().setScale(.8f);
            assetsHandler.fontChco.getData().setScale(.6f);
        } else if (this instanceof LeaderboardChooseScreen) {
            assetsHandler.fontGde.getData().setScale(.8f);
            assetsHandler.fontChco.getData().setScale(.65f);
        } else if (this instanceof ShopScreen) {
            assetsHandler.fontGde.getData().setScale(.68f);
            assetsHandler.fontChco.getData().setScale(.45f);
        }
    }

    @Override
    public void render(float delta) {
        screenLastStateTime = ScreenStateTime;
        ScreenStateTime += delta;

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        draw(delta);
    }

    public abstract void update(float delta);

    public abstract void draw(float delta);

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public abstract void show();

    @Override
    public void hide() {
        Settings.guardar();
    }

    @Override
    public void pause() {
        assetsHandler.pauseMusic();
    }

    @Override
    public void resume() {
        if (this instanceof GameScreen)
            assetsHandler.platMusicInGame();
        else
            assetsHandler.playMusicMenus();
    }

    @Override
    public void dispose() {
    }
}
