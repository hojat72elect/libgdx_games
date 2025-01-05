package dev.ian.snakeboi.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import dev.ian.snakeboi.game.GameInfo;
import dev.ian.snakeboi.asset.Asset;

import static dev.ian.snakeboi.game.GameInfo.SCALE;

/**
 * Created by: Ian Parcon
 * Date created: Aug 29, 2018
 * Time created: 10:09 AM
 */
public class Board {

    private Cell[][] cells;
    private Snake snake;
    private String[] foodTypes = {
            "green_icing_green_sprinkles", "blue_icing", "chocolate_icing", "chocolate_icing_chocolate_drizzle", "dark_red_icing",
            "orange_icing_chocolate_shaving", "pink_icing_sprinkles", "orange_icing_chocolate_shaving", "pink_icing_sprinkles",
            "pink_icing_white_drizzle", "red_icing_white_sprinkles",
            "white_icing", "white_icing_sprinkles", "yellow_icing_chocolate_drizzle"};

    public Board(Snake snake, int width, int height) {
        this.snake = snake;
        initBoard(width, height);
    }

    private void initBoard(int width, int height) {
        cells = new Cell[width / SCALE][height / SCALE];
        for (int rowGrass = 0; rowGrass < cells.length; rowGrass++) {
            for (int colGrass = 0; colGrass < cells[rowGrass].length; colGrass++) {
                Cell cell = new Cell(Asset.instance().getSprite(randomGrass(rowGrass, colGrass)), rowGrass * SCALE, colGrass * SCALE);
                cells[rowGrass][colGrass] = cell;
            }
        }
    }


    private String randomGrass(int row, int col) {
        if (col % 2 == 0) {
            if (row % 2 != 0) return "grass_01";
            if (row % 2 == 0) return "grass_02";
        } else if (col % 2 != 0) {
            if (row % 2 != 0) return "grass_02";
            if (row % 2 == 0) return "grass_01";
        }
        return "grass_02";
    }

    public void render(SpriteBatch batch) {
        for (Cell[] cell : cells) {
            for (Cell aCell : cell) {
                aCell.draw(batch);
            }
        }
    }

    public GameObject generateFood() {

        int foodType = MathUtils.random(foodTypes.length - 1);
        System.out.println("Food type: " + foodType);
        GameObject food = new GameObject(Asset.instance().getSprite(foodTypes[foodType]));
        food.setPosition(foodRandX(), foodRandY());

        for (Cell body : snake.getBody()) {
            while (food.getX() == body.getX() && body.getY() == food.getY()) {
                food.setPosition(foodRandX(), foodRandY());
            }
        }
        return food;
    }

    private float foodRandX() {
        return MathUtils.random(1, GameInfo.BOARD_WIDTH - 1) * SCALE;
    }

    private float foodRandY() {
        return MathUtils.random(1, GameInfo.BOARD_HEIGHT - 1) * SCALE;
    }

    public void reset() {
        generateFood();
    }
}
