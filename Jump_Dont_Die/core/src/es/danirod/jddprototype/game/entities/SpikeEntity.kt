package es.danirod.jddprototype.game.entities

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
 * This is the actor that will make you lose if you touch it. Spikes are on the screen and you
 * have to avoid touching them. If you touch them, you lose at the moment, just like in the
 * original game.
 */
class SpikeEntity(
    /**
     * The world this spike is in.
     */
    private val world: World,
    /**
     * Spike texture.
     */
    private val texture: Texture?, x: Float, y: Float
) : Actor() {
    /**
     * The body assigned to this spike.
     */
    private val body: Body

    /**
     * The fixture assigned to this spike.
     */
    private val fixture: Fixture

    /**
     * Create some spike
     *
     * @param x       horizontal position for the center of the spike (meters)
     * @param y       vertical position for the base of the spike (meters)
     */
    init {
        // Create the body.
        val def = BodyDef() // (1) Give it some definition.
        def.position.set(x, y + 0.5f) // (2) Position the body on the world.
        body = world.createBody(def) // (3) Create the body.

        // Now give it a shape.
        val box = PolygonShape() // (1) We will make a polygon.
        val vertices = arrayOfNulls<Vector2>(3) // (2) However vertices will be manually added.
        vertices[0] = Vector2(-0.5f, -0.5f) // (3) Add the vertices for a triangle.
        vertices[1] = Vector2(0.5f, -0.5f)
        vertices[2] = Vector2(0f, 0.5f)
        box.set(vertices) // (4) And put them in the shape.
        fixture = body.createFixture(box, 1f) // (5) Create the fixture.
        fixture.setUserData("spike") // (6) And set the user data to enemy.
        box.dispose() // (7) Destroy the shape when you don't need it.

        // Position the actor in the screen by converting the meters to pixels.
        setPosition((x - 0.5f) * Constants.PIXELS_IN_METER, y * Constants.PIXELS_IN_METER)
        setSize(Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight())
    }

    fun detach() {
        body.destroyFixture(fixture)
        world.destroyBody(body)
    }
}
