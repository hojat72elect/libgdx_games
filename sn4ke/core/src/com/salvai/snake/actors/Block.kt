package com.salvai.snake.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.salvai.snake.utils.WorldUtils

class Block(worldPosition: Vector2?, texture: Texture?, worldUtils: WorldUtils) : GameObject(worldPosition!!, texture!!, worldUtils)
