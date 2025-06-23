package com.salvai.centrum.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

public class LevelReader {

    private Json json;

    public LevelReader() {
        json = new Json();
    }


    public Level loadLevel(int levelIndex) {
        Level level = json.fromJson(Level.class, Gdx.files.internal("levels/level" + levelIndex + ".json"));
        int maxScore = calculateMaxScore(level);
        level.secondStarScore = maxScore / 2;
        level.thirdStarScore = maxScore;
        return level;
    }


    private int calculateMaxScore(Level level) {
        int maxScore = 0;
        for (BallInfo ballInfo : level.ballInfos)
            if (ballInfo.color == 0)
                maxScore++;
        return maxScore;
    }

}
