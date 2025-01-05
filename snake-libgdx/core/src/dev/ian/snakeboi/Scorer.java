package dev.ian.snakeboi;

/**
 * Created by: Ian Parcon
 * Date created: Sep 01, 2018
 * Time created: 9:32 PM
 */
public class Scorer {

    private static int score;

    public static void score() {
        score += 10;
    }

    public static int getScore() {
        return score;
    }

    public static void reset() {
        score = 0;
    }
}
