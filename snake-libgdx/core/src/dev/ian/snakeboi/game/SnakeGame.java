package dev.ian.snakeboi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import dev.ian.snakeboi.Scorer;
import dev.ian.snakeboi.asset.Asset;
import dev.ian.snakeboi.asset.SoundPlayer;
import dev.ian.snakeboi.entities.Board;
import dev.ian.snakeboi.entities.GameObject;
import dev.ian.snakeboi.entities.Snake;

/**
 * Created by: Ian Parcon
 * Date created: Aug 29, 2018
 * Time created: 10:24 AM
 */
public class SnakeGame {

    private static final int WIDTH = Gdx.graphics.getWidth();
    private static final int HEIGHT = Gdx.graphics.getHeight();

    private Board board;
    private Snake snake;
    private float timeState;
    private BitmapFont font;

    private GameObject food;
    private boolean isGameOver;

    public SnakeGame() {
        TextureAtlas atlas = Asset.instance().get(Asset.SNAKE_PACK);
        font = Asset.instance().get(Asset.PIXEL_FONT);
        snake = new Snake(atlas);
        board = new Board(snake, WIDTH, HEIGHT);
        food = board.generateFood();
        init();
    }

    private void init() {
        SoundPlayer.init();
        SoundPlayer.playMusic(Asset.MEMO_SOUND, false);
    }

    public void update(float delta) {
        if (snake.hasLive()) {
            timeState += delta;
            snake.handleEvents();
            if (timeState >= .09f) {
                snake.moveBody();
                timeState = 0;
            }
            if (snake.isCrash()) {
                snake.reset();
                snake.popLife();
                SoundPlayer.playSound(Asset.CRASH_SOUND, false);
            }
            if (snake.isFoodTouch(food)) {
                SoundPlayer.playSound(Asset.EAT_FOOD_SOUND, false);
                Scorer.score();
                snake.grow();
                food = board.generateFood();
            }
        } else {
            gameOver();
            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) start();
        }
    }

    private void gameOver() {
        isGameOver = true;
        SoundPlayer.stopMusic(Asset.MEMO_SOUND);
        SoundPlayer.playMusic(Asset.GAME_OVER_SOUND, false);
    }

    private void start() {
        SoundPlayer.playMusic(Asset.MEMO_SOUND, false);
        SoundPlayer.stopMusic(Asset.GAME_OVER_SOUND);

        isGameOver = false;
        snake.reset();
        snake.restoreHealth();
        food = board.generateFood();
        Scorer.reset();
    }

    public void render(SpriteBatch batch) {
        board.render(batch);
        food.draw(batch);
        snake.render(batch);
        font.setColor(Color.BLACK);

        if (isGameOver) {
            font.draw(batch, "GAME OVER", (WIDTH - 100) / 2, (HEIGHT + 100) / 2);
            font.draw(batch, "Press any key to continue", (WIDTH - 250) / 2, (HEIGHT + 50) / 2);
        }

        font.draw(batch, "Score: " + Scorer.getScore(), GameInfo.SCALE / 2, GameInfo.SCREEN_HEIGHT - 10);
        font.draw(batch, "Size: " + snake.getBody().size(), GameInfo.SCALE / 2, GameInfo.SCREEN_HEIGHT - 40);
    }

}
