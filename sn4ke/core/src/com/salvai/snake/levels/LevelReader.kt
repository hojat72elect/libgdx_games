package com.salvai.snake.levels

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Json
import com.salvai.snake.utils.Constants

class LevelReader {
    private val json: Json = Json()

    fun loadAllLevels(): Array<Level?> {
        val levels = Array<Level?>()

        for (i in 1..Constants.MAX_LEVEL) levels.add(loadLevel(i))
        return levels
    }

    private fun loadLevel(level: Int): Level? {
        return if (level % 20 == 0) {
            json.fromJson(
                Level::class.java,
                Gdx.files.internal("levels/level" + level / Constants.LEVELS_PRO_TAB + "-20.json")
            )
            //TODO find out how to read levels correctly
        } else {
            json.fromJson(
                Level::class.java,
                Gdx.files.internal("levels/level" + ((level / Constants.LEVELS_PRO_TAB) + 1) + "-" + level % Constants.LEVELS_PRO_TAB + ".json")
            ) //TODO find out how to read levels correctly}
        }
    }
}
