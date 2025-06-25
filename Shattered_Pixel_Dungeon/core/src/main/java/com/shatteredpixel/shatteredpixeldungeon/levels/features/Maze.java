

package com.shatteredpixel.shatteredpixeldungeon.levels.features;

import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class Maze {

    public static boolean EMPTY = false;
    public static boolean FILLED = true;

    public static boolean[][] generate(Room r) {
        boolean[][] maze = new boolean[r.width()][r.height()];

        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[0].length; y++) {
                if (x == 0 || x == maze.length - 1 ||
                        y == 0 || y == maze[0].length - 1) {

                    maze[x][y] = FILLED;
                }
            }
        }

        //set spaces where there are doors
        for (Room.Door d : r.connected.values()) {
            maze[d.x - r.left][d.y - r.top] = EMPTY;
        }

        return generate(maze);
    }

    public static boolean[][] generate(Rect r) {
        return generate(r.width() + 1, r.height() + 1);
    }

    public static boolean[][] generate(Rect r, int[] terrain, int width, int filledTerrainType) {
        boolean[][] maze = new boolean[r.width()][r.height()];
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[0].length; y++) {
                if (terrain[x + r.left + (y + r.top) * width] == filledTerrainType) {
                    maze[x][y] = FILLED;
                }
            }
        }

        return generate(maze);
    }

    public static boolean[][] generate(int width, int height) {
        return generate(new boolean[width][height]);
    }

    public static boolean[][] generate(boolean[][] maze) {
        int fails = 0;
        int x, y, moves;
        int[] mov;
        while (fails < 2500) {

            //find a random wall point
            do {
                x = Random.Int(maze.length);
                y = Random.Int(maze[0].length);
            } while (!maze[x][y]);

            //decide on how we're going to move
            mov = decideDirection(maze, x, y);
            if (mov == null) {
                fails++;
            } else {

                fails = 0;
                moves = 0;
                do {
                    x += mov[0];
                    y += mov[1];
                    maze[x][y] = FILLED;
                    moves++;
                } while (Random.Int(moves) == 0 && checkValidMove(maze, x, y, mov));
            }
        }

        return maze;
    }

    private static int[] decideDirection(boolean[][] maze, int x, int y) {

        //attempts to move up
        if (Random.Int(4) == 0 && //1 in 4 chance
                checkValidMove(maze, x, y, new int[]{0, -1})) {
            return new int[]{0, -1};
        }

        //attempts to move right
        if (Random.Int(3) == 0 && //1 in 3 chance
                checkValidMove(maze, x, y, new int[]{1, 0})) {
            return new int[]{1, 0};
        }

        //attempts to move down
        if (Random.Int(2) == 0 && //1 in 2 chance
                checkValidMove(maze, x, y, new int[]{0, 1})) {
            return new int[]{0, 1};
        }

        //attempts to move left
        if (
                checkValidMove(maze, x, y, new int[]{-1, 0})) {
            return new int[]{-1, 0};
        }

        return null;
    }

    public static boolean allowDiagonals = false;

    private static boolean checkValidMove(boolean[][] maze, int x, int y, int[] mov) {
        int sideX = 1 - Math.abs(mov[0]);
        int sideY = 1 - Math.abs(mov[1]);

        x += mov[0];
        y += mov[1];

        if (x <= 0 || x >= maze.length - 1 || y <= 0 || y >= maze[0].length - 1) {
            return false;
        } else if (maze[x][y] || maze[x + sideX][y + sideY] || maze[x - sideX][y - sideY]) {
            return false;
        }

        x += mov[0];
        y += mov[1];

        if (x <= 0 || x >= maze.length - 1 || y <= 0 || y >= maze[0].length - 1) {
            return false;
        } else if (maze[x][y]) {
            return false;
        } else return allowDiagonals || (!maze[x + sideX][y + sideY] && !maze[x - sideX][y - sideY]);
    }
}
