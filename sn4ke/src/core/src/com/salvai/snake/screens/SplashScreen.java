package com.salvai.snake.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.salvai.snake.SnakeIt;
import com.salvai.snake.enums.MovingDirection;
import com.salvai.snake.levels.LevelReader;
import com.salvai.snake.utils.Constants;

public class SplashScreen extends ScreenAdapter {

    public SnakeIt game;
    public Sprite splashSprite;
    private int countdownTime;
    private boolean toSetUp;

    public SplashScreen(SnakeIt gameClass) {
        super();
        this.game = gameClass;
        toSetUp = true;
        splashSprite = new Sprite(game.assetsManager.manager.get(Constants.APPLE_IMAGE_NAME, Texture.class));
        splashSprite.setSize(game.worldWidth * 0.6f, game.worldWidth * 0.6f);
        splashSprite.setPosition(game.worldWidth * 0.2f, game.worldHeight * 0.5f - splashSprite.getHeight() * 0.3f);
        splashSprite.setAlpha(1f);
        countdownTime = 41;

        game.assetsManager.loadSkin();
        game.assetsManager.loadImages();
//        game.assetsManager.loadSounds();
    }


    @Override
    public void render(float delta) {
        setupScreen();
        game.drawBackground(delta, MovingDirection.UP);

        game.backgroundStage.getBatch().begin();
        splashSprite.draw(game.backgroundStage.getBatch());
        game.backgroundStage.getBatch().end();

        if (game.assetsManager.manager.update()) {
            //load levels first
            if (countdownTime < 41) {
                countdownTime -= delta;
                splashSprite.setAlpha((float) countdownTime / 40f);
                if (countdownTime == 0) {
                    if (game.firstTimeOpen) {
                        game.setScreen(new GameScreen(game));
                        dispose();
                    } else {
                        game.setScreen(new MenuScreen(game));
                        dispose();
                    }
                }
            } else {
                if (toSetUp) {
                    game.skin = game.assetsManager.manager.get(Constants.SKIN_FILE_NAME, Skin.class);
                    game.setUpTopBar(Constants.SCREEN.MENU);

                    LevelReader levelReader = new LevelReader();
                    game.levels = levelReader.loadAllLevels();
                    toSetUp = false;
                }
                countdownTime -= 1;
            }
        }
    }


    private void setupScreen() {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.camera.update();
    }


    @Override
    public void resize(int width, int height) {
        // change the stage's viewport when teh screen size is changed
        game.viewport.update(width, height, true);
    }
}
