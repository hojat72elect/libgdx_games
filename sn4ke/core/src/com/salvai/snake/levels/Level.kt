package com.salvai.snake.levels

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.salvai.snake.actors.Block
import com.salvai.snake.utils.Constants
import com.salvai.snake.utils.WorldUtils
import java.util.Collections
import com.badlogic.gdx.utils.Array as GdxArray

class Level() {
    var snakeStartX = -1
    var snakeStartY = -1
    private lateinit var blocksPositions: kotlin.Array<IntArray>
    private var toReverse = true

    /**
     * 1 block, 2 snake 0 nothing
     */
    fun getBlocks(blockTexture: Texture, worldUtils: WorldUtils): GdxArray<Block> {
        val blocks = GdxArray<Block>()

        //reverse array other wise blocks are mirrored
        if (toReverse) {
            val blockList = listOf(*blocksPositions)
            Collections.reverse(blockList)
            blocksPositions = blockList.toTypedArray<IntArray>()
            toReverse = false
        }

        for (i in blocksPositions.indices) for (j in blocksPositions[0].indices) if (blocksPositions[i][j] == Constants.BLOCK_CELL) blocks.add(
            Block(
                Vector2((j + 1).toFloat(), (i + 1).toFloat()),
                blockTexture,
                worldUtils
            )
        ) // +1 because of border
        else if (blocksPositions[i][j] == Constants.SNAKE_HEAD_CELL) {
            snakeStartX = j + 1
            snakeStartY = i + 1
        }
        return blocks

    }
}