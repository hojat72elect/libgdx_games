package com.bitfire.uracer.game.actors

import com.badlogic.gdx.math.Vector2
import com.bitfire.uracer.game.world.GameWorld
import com.bitfire.uracer.utils.AlgebraMath.clamp
import com.bitfire.uracer.utils.AlgebraMath.fixup
import com.bitfire.uracer.utils.NewConvert.mt2px

class CarState(private val world: GameWorld, val car: Car?) {

    private val carMaxSpeedSquared = if (car != null) { car.carModel.max_speed * car.carModel.max_speed } else 1F
    private val carMaxForce: Float = if (car != null) { car.carModel.max_force } else { 1F }
    var currTileX = -1
    var currTileY = -1

    @JvmField
    var tilePosition = Vector2()

    @JvmField
    var currVelocityLenSquared = 0F
    var currThrottle = 0F

    @JvmField
    var currSpeedFactor = 0F
    var currForceFactor = 0F

    fun dispose() {}

    fun reset() {
        currTileX = -1
        currTileY = -1
    }

    fun update(carDescriptor: CarDescriptor?) {
        if (carDescriptor != null) {
            updateFactors(carDescriptor)
        }

        updateTilePosition()
    }

    private fun updateFactors(carDescriptor: CarDescriptor) {
        currVelocityLenSquared = carDescriptor.velocityWorldCoordinates.len2()
        currThrottle = carDescriptor.throttle
        currSpeedFactor = fixup(clamp(currVelocityLenSquared / carMaxSpeedSquared, 0F, 1F))
        currForceFactor = fixup(clamp(currThrottle / carMaxForce, 0F, 1F))
    }

    /*
     * Keeps track of the car's tile position and triggers a TileChanged event whenever the car's world position translates to a
     * tile index that is different from the previous one.
     */
    private fun updateTilePosition() {
        tilePosition.set(world.pxToTile(mt2px(car!!.getBody().position.x), mt2px(car.getBody().position.y)))

        currTileX = tilePosition.x.toInt()
        currTileY = tilePosition.y.toInt()
    }
}
