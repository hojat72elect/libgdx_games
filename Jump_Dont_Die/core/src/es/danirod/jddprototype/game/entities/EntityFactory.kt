package es.danirod.jddprototype.game.entities

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World

/**
 * This class creates entities using Factory Methods.
 */
class EntityFactory
/**
 * Create a new entity factory using the provided asset manager.
 *
 * @param manager the asset manager used to generate things.
 */(private val manager: AssetManager) {
    /**
     * Create a player using the default texture.
     *
     * @param world    world where the player will have to live in.
     * @param position initial position ofr the player in the world (meters,meters).
     * @return a player.
     */
    fun createPlayer(world: World, position: Vector2?): PlayerEntity {
        val playerTexture = manager.get<Texture?>("player.png")
        return PlayerEntity(world, playerTexture, position)
    }

    /**
     * Create floor using the default texture set.
     *
     * @param world world where the floor will live in.
     * @param x     horizontal position for the spikes in the world (meters).
     * @param width width for the floor (meters).
     * @param y     vertical position for the top of this floor (meters).
     * @return a floor.
     */
    fun createFloor(world: World, x: Float, width: Float, y: Float): FloorEntity {
        val floorTexture = manager.get<Texture?>("floor.png")
        val overfloorTexture = manager.get<Texture?>("overfloor.png")
        return FloorEntity(world, floorTexture, overfloorTexture, x, width, y)
    }

    /**
     * Create spikes using the default texture.
     *
     * @param world world where the spikes will live in.
     * @param x     horizontal position for the spikes in the world (meters).
     * @param y     vertical position for the base of the spikes in the world (meters).
     * @return some spikes.
     */
    fun createSpikes(world: World, x: Float, y: Float): SpikeEntity {
        val spikeTexture = manager.get<Texture?>("spike.png")
        return SpikeEntity(world, spikeTexture, x, y)
    }
}
