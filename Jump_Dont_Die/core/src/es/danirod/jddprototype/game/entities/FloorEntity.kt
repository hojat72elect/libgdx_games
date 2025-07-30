package es.danirod.jddprototype.game.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import es.danirod.jddprototype.game.Constants

/**
 * This entity represents the floor. The player and the spikes are above the floor. You cannot go
 * below the floor. And if you hit the left border of a floor when you are supposed to jump,
 * you lose.
 *
 * @param world The world instance this floor has to live in.
 */
class FloorEntity(
    private val world: World, private val floor: Texture?, private val overfloor: Texture?, x: Float, width: Float, y: Float
) : Actor() {

    /**
     * The bodies for the floor. You see here the main body for the floor, and the left border.
     */
    private val body: Body

    /**
     * The fixtures assigned to both bodies. This gives bodies shape.
     */
    private val fixture: Fixture

    /**
     * Create a new floor
     *
     * @param x         left border for the floor (meters)
     * @param width     how wide the floor is (meters)
     * @param y         top border for the floor (meters)
     */
    init {
        // Create the floor body.
        val def = BodyDef() // (1) Provide some definition.
        def.position.set(x + width / 2, y - 0.5f) // (2) Center the floor in the coordinates given
        body = world.createBody(def) // (3) Create the floor. Easy.

        // Give it a box shape.
        val box = PolygonShape() // (1) Create the polygon shape.
        box.setAsBox(width / 2, 0.5f) // (2) Give it some size.
        fixture = body.createFixture(box, 1f) // (3) Create a fixture.
        fixture.setUserData("floor") // (4) Set the user data for the fixture.
        box.dispose() // (5) Destroy the shape.

        // Now create the left body. This body is spiky, if you hit this body, you die. It is
        // on the left border of the floor and it is only used when you have to jump some stairs.
        // It works the same than the previous one.
        val leftDef = BodyDef()
        leftDef.position.set(x, y - 0.55f)
        val leftBody = world.createBody(leftDef)

        // As well as the fixture. Remember, use spike user data to make it act like an enemy.
        val leftBox = PolygonShape()
        leftBox.setAsBox(0.02f, 0.45f)
        val leftFixture = leftBody.createFixture(leftBox, 1f)
        leftFixture.setUserData("spike")
        leftBox.dispose()

        // Now place the actor in the stage by converting the coordinates given in meters to px.
        setSize(width * Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER)
        setPosition(x * Constants.PIXELS_IN_METER, (y - 1) * Constants.PIXELS_IN_METER)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        // Render both textures.
        batch.draw(floor, getX(), getY(), getWidth(), getHeight())
        batch.draw(overfloor, getX(), getY() + 0.9f * getHeight(), getWidth(), 0.1f * getHeight())
    }

    fun detach() {
        body.destroyFixture(fixture)
        world.destroyBody(body)
    }
}
