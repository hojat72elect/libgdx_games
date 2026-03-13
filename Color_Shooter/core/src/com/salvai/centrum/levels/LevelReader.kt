package com.salvai.centrum.levels

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Json

class LevelReader {
    private val json = Json()


    fun loadLevel(levelIndex: Int): Level {
        val level = json.fromJson(Level::class.java, Gdx.files.internal("levels/level$levelIndex.json"))
        val maxScore = calculateMaxScore(level)
        level.secondStarScore = maxScore / 2
        level.thirdStarScore = maxScore
        return level
    }


    private fun calculateMaxScore(level: Level): Int {
        var maxScore = 0
        for (ballInfo in level.ballInfos) if (ballInfo.color == 0) maxScore++
        return maxScore
    }
}
