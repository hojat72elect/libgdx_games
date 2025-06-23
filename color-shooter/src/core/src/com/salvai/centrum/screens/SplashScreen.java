package com.salvai.centrum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.salvai.centrum.CentrumGameClass;
import com.salvai.centrum.levels.LevelReader;
import com.salvai.centrum.utils.Constants;

public class SplashScreen extends ScreenAdapter {

    private Texture splashTexture;
    private Sprite splashSprite;
    private CentrumGameClass game;
    private int countdownTime;
    private int levelLoadCount;
    LevelReader levelReader;

    public SplashScreen(CentrumGameClass gameClass) {
        game = gameClass;

        splashTexture = game.assetsManager.manager.get(Constants.SPLASH_IMAGE_NAME, Texture.class);
        splashSprite = new Sprite(splashTexture);
        splashSprite.setScale(0.9f);
        splashSprite.setPosition(Constants.WIDTH_CENTER - splashSprite.getWidth() * 0.5f, Constants.HEIGHT_CENTER - splashSprite.getHeight() * 0.5f);
        splashSprite.setAlpha(1f);
        countdownTime = 40;
        levelLoadCount = 1;
        levelReader = new LevelReader();

    }

    @Override
    public void render(float delta) {
        setupScreen();
        game.batch.begin();

        if (countdownTime == 0) {
            game.skin = game.assetsManager.manager.get(Constants.SKIN_FILE_NAME, Skin.class);
            game.setScreen(new MenuScreen(game));
            dispose();
        }

        if (game.assetsManager.manager.update()) {
            if (levelLoadCount <= Constants.MAX_LEVEL)
                loadLevels(levelLoadCount);
            else {
                countdownTime -= delta;
                splashSprite.setAlpha((float) countdownTime / 40f);
            }
        }
        game.drawBackground(delta);
        splashSprite.draw(game.batch);
        game.batch.end();
    }

    private void loadLevels(int levelLoadCount) {
        game.levels.add(levelReader.loadLevel(levelLoadCount));
        this.levelLoadCount++;
    }


    private void setupScreen() {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);
    }


    @Override
    public void resize(int width, int height) {
        // change the stage's viewport when teh screen size is changed
        game.viewport.update(width, height, true);
    }


}
