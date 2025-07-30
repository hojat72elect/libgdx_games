package es.danirod.jddprototype.game.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import es.danirod.jddprototype.game.Constants

/**
 * This is the body the user controls. It has to jump and don't die, like the title of the game
 * says. You can make it jump by touching the screen. Don't let the player touch any spike or
 * you will lose.
 */
class PlayerEntity(
    /**
     * The world instance this player is in.
     */
    private val world: World,
    /**
     * The player texture.
     */
    private val texture: Texture?, position: Vector2?
) : Actor() {
    /**
     * The body for this player.
     */
    private val body: Body

    /**
     * The fixture for this player.
     */
    private val fixture: Fixture

    // Getter and setter festival below here.
    /**
     * Is the player alive? If he touches a spike, is not alive. The player will only move and
     * jump if it's alive. Otherwise it is said that the user has lost and the game is over.
     */
    var isAlive: Boolean = true

    /**
     * Is the player jumping? If the player is jumping, then it is not possible to jump again
     * because the user cannot double jump. The flag has to be set when starting a jump and be
     * unset when touching the floor again.
     */
    private var jumping = false

    /**
     * Does the player have to jump? This flag is used when the player touches the floor and the
     * user is still touching the screen, to make a double jump. Remember that we cannot add
     * a force inside a ContactListener. We have to use this flag to remember that the player
     * had to jump after the collision.
     */
    private var mustJump = false

    init {
        // Create the player body.
        val def = BodyDef() // (1) Create the body definition.
        def.position.set(position) // (2) Put the body in the initial position.
        def.type = BodyDef.BodyType.DynamicBody // (3) Remember to make it dynamic.
        body = world.createBody(def) // (4) Now create the body.

        // Give it some shape.
        val box = PolygonShape() // (1) Create the shape.
        box.setAsBox(0.5f, 0.5f) // (2) 1x1 meter box.
        fixture = body.createFixture(box, 3f) // (3) Create the fixture.
        fixture.setUserData("player") // (4) Set the user data.
        box.dispose() // (5) Destroy the shape.

        // Set the size to a value that is big enough to be rendered on the screen.
        setSize(Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        // Always update the position of the actor when you are going to draw it, so that the
        // position of the actor on the screen is as accurate as possible to the current position
        // of the Box2D body.
        setPosition(
            (body.getPosition().x - 0.5f) * Constants.PIXELS_IN_METER,
            (body.getPosition().y - 0.5f) * Constants.PIXELS_IN_METER
        )
        batch.draw(texture, getX(), getY(), getWidth(), getHeight())
    }

    override fun act(delta: Float) {
        // Jump when you touch the screen.
        if (Gdx.input.justTouched()) {
            jump()
        }

        // Jump if we were required to jump during a collision.
        if (mustJump) {
            mustJump = false
            jump()
        }

        // If the player is alive, change the speed so that it moves.
        if (this.isAlive) {
            // Only change X speed. Do not change Y speed because if the player is jumping,
            // this speed has to be managed by the forces applied to the player. If we modify
            // Y speed, jumps can get very very weir.d
            val speedY = body.getLinearVelocity().y
            body.setLinearVelocity(Constants.PLAYER_SPEED, speedY)
        }

        // If the player is jumping, apply some opposite force so that the player falls faster.
        if (jumping) {
            body.applyForceToCenter(0f, -Constants.IMPULSE_JUMP * 1.15f, true)
        }
    }

    fun jump() {
        // The player must not be already jumping and be alive to jump.
        if (!jumping && this.isAlive) {
            jumping = true

            // Apply an impulse to the player. This will make change the velocity almost
            // at the moment unlike using forces, which gradually changes the force used
            // during the jump. We get the position becase we have to apply the impulse
            // at the center of mass of the body.
            val position = body.getPosition()
            body.applyLinearImpulse(0f, Constants.IMPULSE_JUMP.toFloat(), position.x, position.y, true)
        }
    }

    fun detach() {
        body.destroyFixture(fixture)
        world.destroyBody(body)
    }

    fun setJumping(jumping: Boolean) {
        this.jumping = jumping
    }

    fun setMustJump(mustJump: Boolean) {
        this.mustJump = mustJump
    }
}
