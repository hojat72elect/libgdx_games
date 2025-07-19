package tests

import box2dLight.PointLight
import box2dLight.RayHandler
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World

class SimpleTest : ApplicationAdapter() {
    /**
     * the camera
     */
    private lateinit var camera: OrthographicCamera
    private lateinit var rayHandler: RayHandler
    private lateinit var world: World

    override fun create() {
        camera = OrthographicCamera(48f, 32f)
        camera.update()
        world = World(Vector2(0f, -10f), true)
        rayHandler = RayHandler(world)
        PointLight(rayHandler, 32)
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        world.step(Gdx.graphics.deltaTime, 8, 3)
        rayHandler.setCombinedMatrix(camera)
        rayHandler.updateAndRender()
    }
}
