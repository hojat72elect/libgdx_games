package com.salvai.snake.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.salvai.snake.utils.Constants;

public class LevelReader {

    private final Json json;

    public LevelReader() {
        json = new Json();
    }

    public Array<Level> loadAllLevels() {
        Array<Level> levels = new Array<Level>();


        for (int i = 1; i <= Constants.MAX_LEVEL; i++)
            levels.add(loadLevel(i));
        return levels;
    }

    private Level loadLevel(int level) {
        if (level % 20 == 0)
            return json.fromJson(Level.class, Gdx.files.internal("levels/level" + level / Constants.LEVELS_PRO_TAB + "-20.json")); //TODO find out how to read levels correctly
        else
            return json.fromJson(Level.class, Gdx.files.internal("levels/level" + ((level / Constants.LEVELS_PRO_TAB) + 1) + "-" + level % Constants.LEVELS_PRO_TAB + ".json")); //TODO find out how to read levels correctly
    }
}
