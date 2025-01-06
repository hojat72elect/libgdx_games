package com.bms.chemtris.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Class to manage score.
 */
public class ScoreManager {

    //preference to store score
    private static Preferences scoreManager = Gdx.app.getPreferences("com.bms.chemtris");

    //level strings
    public static final String LEVEL_1 = "level1",
            LEVEL_2 = "level2",
            LEVEL_3 = "level3",
            LEVEL_4 = "level4",
            LEVEL_5 = "level5",
            LEVEL_6 = "level6",
            H2O = "water",
            CH4 = "methane";

    //constructor
    public static void setup(){
        //initializing high scores for each level
        if(!scoreManager.contains(LEVEL_1)){
            scoreManager.putInteger(LEVEL_1,0);
        }
        if(!scoreManager.contains(LEVEL_2)){
            scoreManager.putInteger(LEVEL_2,0);
        }
        if(!scoreManager.contains(LEVEL_3)){
            scoreManager.putInteger(LEVEL_3,0);
        }
        if(!scoreManager.contains(LEVEL_4)){
            scoreManager.putInteger(LEVEL_4,0);
        }
        if(!scoreManager.contains(LEVEL_5)){
            scoreManager.putInteger(LEVEL_5,0);
        }
        if(!scoreManager.contains(LEVEL_6)){
            scoreManager.putInteger(LEVEL_6,0);
        }
        if(!scoreManager.contains(H2O)){
            scoreManager.putInteger(H2O,0);
        }
        if(!scoreManager.contains(CH4)){
            scoreManager.putInteger(CH4,0);
        }
    }

    //method to return high score of a level
    public static int getScore(String key){
        return scoreManager.getInteger(key);
    }

    //method to set high score of a level
    public static void setScore(String key, int score){
        scoreManager.putInteger(key,score);
        scoreManager.flush();
    }

    //method to check if ever scored
    public static boolean scored(){
        if(scoreManager.getInteger(LEVEL_1) > 0){
            return true;
        }
        else if(scoreManager.getInteger(LEVEL_2) > 0){
            return true;
        }
        else if(scoreManager.getInteger(LEVEL_3) > 0){
            return true;
        }
        else if(scoreManager.getInteger(LEVEL_4) > 0){
            return true;
        }
        else if(scoreManager.getInteger(LEVEL_5) > 0){
            return true;
        }
        else if(scoreManager.getInteger(LEVEL_6) > 0){
            return true;
        }
        else{
            return false;
        }
    }
}
