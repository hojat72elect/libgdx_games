package com.salvai.snake.utils

import com.badlogic.gdx.math.Vector2
import com.salvai.snake.enums.BlockRatio

class WorldUtils(blockRatio: BlockRatio) {
    @JvmField
    var blockSize: Int = 0
    var blockRatio: BlockRatio?

    @JvmField
    var worldWidth: Int = 0
    var worldHeight: Int = 0

    @JvmField
    var worldWidthCenter: Int = 0

    @JvmField
    var worldHeightCenter: Int = 0
    var playableWorldWidth: Int = 0

    @JvmField
    var playableWorldHeigth: Int = 0

    init {
        this.blockRatio = blockRatio
        setBlockSize(blockRatio)
    }

    fun setBlockSize(blockRatio: BlockRatio) {
        this.blockRatio = blockRatio
        when (blockRatio) {
            BlockRatio.SMALL -> blockSize = Constants.SCREEN_BLOCK_SIZE_SMALL
            BlockRatio.MEDIUM -> blockSize = Constants.SCREEN_BLOCK_SIZE_MEDIUM
            BlockRatio.BIG -> blockSize = Constants.SCREEN_BLOCK_SIZE_BIG
        }

        worldWidth = Constants.SCREEN_WIDTH / blockSize - 1 //screen width / blocksize - 1
        worldHeight = Constants.SCREEN_HEIGHT / blockSize - 1 //screen height / blocksize - 1
        worldWidthCenter = worldWidth / 2
        worldHeightCenter = worldHeight / 2
        playableWorldWidth = worldWidth - (Constants.PLAY_HEIGHT_FACTOR_X * 2)

        when (blockRatio) {
            BlockRatio.SMALL -> playableWorldHeigth = worldHeight - Constants.PLAY_HEIGHT_FACTOR_Y_SMALL + 1
            BlockRatio.MEDIUM -> playableWorldHeigth = worldHeight - Constants.PLAY_HEIGHT_FACTOR_Y_MEDIUM + 1
            BlockRatio.BIG -> playableWorldHeigth = worldHeight - Constants.PLAY_HEIGHT_FACTOR_Y_BIG + 1
        }
    }

    fun worldToScreen(worldPosition: Vector2): Vector2 {
        return Vector2(worldPosition.x * blockSize, worldPosition.y * blockSize)
    }
}
