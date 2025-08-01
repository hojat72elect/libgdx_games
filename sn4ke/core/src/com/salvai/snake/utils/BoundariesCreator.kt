package com.salvai.snake.utils

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.salvai.snake.actors.Block

class BoundariesCreator(private val texture: Texture?, private val worldUtils: WorldUtils) {
    fun fullBoundaries(): Array<Block?> {
        val blocks = Array<Block?>()
        //horizontal
        for (i in 0..worldUtils.worldWidth) {
            blocks.add(Block(Vector2(i.toFloat(), 0f), texture, worldUtils))
            blocks.add(Block(Vector2(i.toFloat(), worldUtils.playableWorldHeigth.toFloat()), texture, worldUtils))
        }
        //vertical
        for (i in 1..<worldUtils.playableWorldHeigth) {
            blocks.add(Block(Vector2(0f, i.toFloat()), texture, worldUtils))
            blocks.add(Block(Vector2(worldUtils.worldWidth.toFloat(), i.toFloat()), texture, worldUtils))
        }
        return blocks
    }
}
