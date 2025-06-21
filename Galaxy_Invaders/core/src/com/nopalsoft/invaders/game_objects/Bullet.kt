package com.nopalsoft.invaders.game_objects

class Bullet : DynamicGameObject {
    val SPEED: Float = 30f

    @JvmField
    var level: Int = 1
    var stateTime: Float

    @JvmField
    var state: Int

    /**
     * Space ship bullet
     */
    constructor(x: Float, y: Float, boostLevel: Int) : super(x, y, WIDTH, HEIGHT) {
        state = STATE_FIRED
        stateTime = 0f
        velocity[0f] = SPEED
        this.level += boostLevel
    }

    /**
     * Alien Bullet
     */
    constructor(x: Float, y: Float) : super(x, y, WIDTH, HEIGHT) {
        state = STATE_FIRED
        stateTime = 0f
        velocity[0f] = -SPEED
    }

    fun update(deltaTime: Float) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime)
        boundsRectangle!!.x = position.x - WIDTH / 2
        boundsRectangle.y = position.y - HEIGHT / 2
        stateTime += deltaTime
    }

    fun hitTarget(vidaTarget: Int) {
        level -= vidaTarget
        if (level <= 0) {
            velocity[0f] = 0f
            stateTime = 0f
            state = STATE_EXPLODING
        }
    }

    /**
     * In case the bullet goes out of the World.Height screen, then I call this method to remove it from the array.
     */
    fun destruirBala() {
        velocity[0f] = 0f
        stateTime = 0f
        state = STATE_EXPLODING
    }

    companion object {
        const val WIDTH: Float = 2.1f
        const val HEIGHT: Float = 1.5f

        const val STATE_FIRED: Int = 0
        const val STATE_EXPLODING: Int = 1
    }
}
