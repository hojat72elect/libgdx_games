/*
 * This file is part of Jump Don't Die.
 * Copyright (C) 2015 Dani Rodríguez <danirod@outlook.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.danirod.jddprototype.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.viewport.FitViewport
import es.danirod.jddprototype.game.entities.EntityFactory
import es.danirod.jddprototype.game.entities.FloorEntity
import es.danirod.jddprototype.game.entities.PlayerEntity
import es.danirod.jddprototype.game.entities.SpikeEntity

/**
 * This is the main screen for the game. All the fun happen here.
 */
class GameScreen(game: MainGame) : BaseScreen(game) {
    /**
     * Stage instance for Scene2D rendering.
     */
    private val stage: Stage

    /**
     * World instance for Box2D engine.
     */
    private val world: World

    /**
     * Player entity.
     */
    private var player: PlayerEntity? = null

    /**
     * List of floors attached to this level.
     */
    private val floorList: MutableList<FloorEntity> = ArrayList<FloorEntity>()

    /**
     * List of spikes attached to this level.
     */
    private val spikeList: MutableList<SpikeEntity> = ArrayList<SpikeEntity>()

    /**
     * Jump sound that has to play when the player jumps.
     */
    private val jumpSound: Sound

    /**
     * Die sound that has to play when the player collides with a spike.
     */
    private val dieSound: Sound

    /**
     * Background music that has to play on the background all the time.
     */
    private val backgroundMusic: Music

    /**
     * Initial position of the camera. Required for reseting the viewport.
     */
    private val position: Vector3

    /**
     * Create the screen. Since this constructor cannot be invoked before libGDX is fully started,
     * it is safe to do critical code here such as loading assets and setting up the stage.
     */
    init {
        // Create a new Scene2D stage for displaying things.
        stage = Stage(FitViewport(640f, 360f))
        position = Vector3(stage.camera.position)

        // Create a new Box2D world for managing things.
        world = World(Vector2(0f, -10f), true)
        world.setContactListener(GameContactListener())

        // Get the sound effect references that will play during the game.
        jumpSound = game.getManager().get<Sound>("audio/jump.ogg")
        dieSound = game.getManager().get<Sound>("audio/die.ogg")
        backgroundMusic = game.getManager().get<Music>("audio/song.ogg")
    }

    /**
     * This method will be executed when this screen is about to be rendered.
     * Here, I use this method to set up the initial position for the stage.
     */
    override fun show() {
        val factory = EntityFactory(game!!.getManager())

        // Create the player. It has an initial position.
        player = factory.createPlayer(world, Vector2(1.5f, 1.5f))

        // This is the main floor. That is why is so long.
        floorList.add(factory.createFloor(world, 0f, 1000f, 1f))

        // Now generate some floors over the main floor. Needless to say, that on a real game
        // this should be better engineered. For instance, have all the information for floors
        // and spikes in a data structure or even some level file and generate them without
        // writing lines of code.
        floorList.add(factory.createFloor(world, 15f, 10f, 2f))
        floorList.add(factory.createFloor(world, 30f, 8f, 2f))

        // Generate some spikes too.
        spikeList.add(factory.createSpikes(world, 8f, 1f))
        spikeList.add(factory.createSpikes(world, 23f, 2f))
        spikeList.add(factory.createSpikes(world, 35f, 2f))
        spikeList.add(factory.createSpikes(world, 50f, 1f))

        // All add the floors and spikes to the stage.
        for (floor in floorList) stage.addActor(floor)
        for (spike in spikeList) stage.addActor(spike)

        // Add the player to the stage too.
        stage.addActor(player)

        // Reset the camera to the left. This is required because we have translated the camera
        // during the game. We need to put the camera on the initial position so that you can
        // use it again if you replay the game.
        stage.camera.position.set(position)
        stage.camera.update()

        // Everything is ready, turn the volume up.
        backgroundMusic.volume = 0.75f
        backgroundMusic.play()
    }

    /**
     * This method will be executed when this screen is no more the active screen.
     * I use this method to destroy all the things that have been used in the stage.
     */
    override fun hide() {
        // Clear the stage. This will remove ALL actors from the stage and it is faster than
        // removing every single actor one by one. This is not shown in the video but it is
        // an improvement.
        stage.clear()

        // Detach every entity from the world they have been living in.
        player!!.detach()
        for (floor in floorList) floor.detach()
        for (spike in spikeList) spike.detach()

        // Clear the lists.
        floorList.clear()
        spikeList.clear()
    }

    /**
     * This method is executed whenever the game requires this screen to be rendered. This will
     * display things on the screen. This method is also used to update the game.
     */
    override fun render(delta: Float) {
        // Do not forget to clean the screen.
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Update the stage. This will update the player speed.
        stage.act()

        // Step the world. This will update the physics and update entity positions.
        world.step(delta, 6, 2)

        // Make the camera follow the player. As long as the player is alive, if the player is
        // moving, make the camera move at the same speed, so that the player is always
        // centered at the same position.
        if (player!!.getX() > 150 && player!!.isAlive) {
            val speed = Constants.PLAYER_SPEED * delta * Constants.PIXELS_IN_METER
            stage.camera.translate(speed, 0f, 0f)
        }

        // Render the screen. Remember, this is the last step!
        stage.draw()
    }

    /**
     * This method is executed when the screen can be safely disposed.
     * I use this method to dispose things that have to be manually disposed.
     */
    override fun dispose() {
        // Dispose the stage to remove the Batch references in the graphics card.
        stage.dispose()

        // Dispose the world to remove the Box2D native data (C++ backend, invoked by Java).
        world.dispose()
    }

    /**
     * This is the contact listener that checks the world for collisions and contacts.
     * I use this method to evaluate when things collide, such as player colliding with floor.
     */
    private inner class GameContactListener : ContactListener {
        fun areCollided(contact: Contact, userB: Any?): Boolean {
            val userDataA = contact.fixtureA.getUserData()
            val userDataB = contact.fixtureB.getUserData()

            // This is not in the video! It is a good idea to check that user data is not null.
            // Sometimes you forget to put user data or you get collisions by entities you didn't
            // expect. Not preventing this will probably result in a NullPointerException.
            if (userDataA == null || userDataB == null) {
                return false
            }

            // Because you never know what is A and what is B, you have to do both checks.
            return (userDataA == "player" && userDataB == userB) ||
                    (userDataA == userB && userDataB == "player")
        }

        /**
         * This method is executed when a contact has started: when two fixtures just collided.
         */
        override fun beginContact(contact: Contact) {
            // The player has collided with the floor.
            if (areCollided(contact, "floor")) {
                player!!.setJumping(false)

                // If the screen is still touched, you have to jump again.
                if (Gdx.input.isTouched) {
                    jumpSound.play()

                    // You just can't add a force here, because while a contact is being handled
                    // the world is locked. Therefore you have to find a way to remember to make
                    // the player jump AFTER the collision has been handled. Here I update the
                    // flag value mustJump. This will make the player jump on next frame.
                    player!!.setMustJump(true)
                }
            }

            // The player has collided with something that hurts.
            if (areCollided(contact, "spike")) {
                // Check that is alive. Sometimes you bounce, you don't want to die more than once.

                if (player!!.isAlive) {
                    player!!.isAlive = false

                    // Sound feedback.
                    backgroundMusic.stop()
                    dieSound.play()

                    // Add an Action. Actions are cool because they let you add animations to your
                    // game. Here I add a sequence action so that two actions happens one after
                    // the other. One action is a delay action. It just waits for 1.5 seconds.
                    // The second actions is a run action. It executes some code. Here, we go
                    // to the game over screen when we die.
                    stage.addAction(
                        Actions.sequence(
                            Actions.delay(1.5f),
                            Actions.run(object : Runnable {
                                override fun run() {
                                    game?.setScreen(game?.gameOverScreen)
                                }
                            })
                        )
                    )
                }
            }
        }

        /**
         * This method is executed when a contact has finished: two fixtures are no more colliding.
         */
        override fun endContact(contact: Contact) {
            // The player is jumping and it is not touching the floor.
            if (areCollided(contact, "floor")) {
                if (player!!.isAlive) {
                    jumpSound.play()
                }
            }
        }

        // Here two lonely methods that I don't use but have to override anyway.
        override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
        }

        override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
        }
    }
}
