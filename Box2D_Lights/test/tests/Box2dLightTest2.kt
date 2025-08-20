package tests

import box2dLight.ChainLight
import box2dLight.ConeLight
import box2dLight.DirectionalLight
import box2dLight.Light
import box2dLight.LightData
import box2dLight.PointLight
import box2dLight.RayHandler
import box2dLight.RayHandlerOptions
import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.QueryCallback
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.joints.MouseJoint
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef

class Box2dLightTest2 : InputAdapter(), ApplicationListener {
    private lateinit var camera: OrthographicCamera
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    var textureRegion: TextureRegion? = null
    var bg: Texture? = null

    /**
     * our box2D world
     */
    var world: World? = null

    /**
     * our boxes
     */
    var balls: ArrayList<Body> = ArrayList(BALLS_NUM)

    /**
     * our ground box
     */
    var groundBody: Body? = null

    /**
     * our mouse joint
     */
    var mouseJoint: MouseJoint? = null

    /**
     * a hit body
     */
    var hitBody: Body? = null

    /**
     * pixel perfect projection for font rendering
     */
    var normalProjection: Matrix4 = Matrix4()
    var showText: Boolean = true

    /**
     * BOX2D LIGHT STUFF
     */
    var rayHandler: RayHandler? = null
    var lights = ArrayList<Light>(BALLS_NUM)
    var sunDirection = -90f
    var physicsTimeLeft = 0f
    var aika = 0L
    var times = 0

    /**
     * we instantiate this vector and the callback here so we don't irritate the
     * GC
     */
    var testPoint: Vector3 = Vector3()
    var callback: QueryCallback = object : QueryCallback {
        override fun reportFixture(fixture: Fixture): Boolean {
            if (fixture.body === groundBody) return true

            if (fixture.testPoint(testPoint.x, testPoint.y)) {
                hitBody = fixture.body
                return false
            } else return true
        }
    }

    /**
     * another temporary vector
     */
    var target = Vector2()

    /**
     * Type of lights to use:
     * 0 - PointLight
     * 1 - ConeLight
     * 2 - ChainLight
     * 3 - DirectionalLight
     */
    var lightsType = 0

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        MathUtils.random.setSeed(Long.Companion.MIN_VALUE)

        camera = OrthographicCamera(VIEW_PORT_WIDTH, VIEW_PORT_HEIGHT)
        camera.position.set(0f, VIEW_PORT_HEIGHT / 2f, 0f)
        camera.update()

        batch = SpriteBatch()
        font = BitmapFont()
        font.color = Color.RED

        textureRegion = TextureRegion(Texture(Gdx.files.internal("test/data/marble.png")))
        bg = Texture(Gdx.files.internal("test/data/bg.png"))

        createPhysicsWorld()
        Gdx.input.inputProcessor = this

        normalProjection.setToOrtho2D(0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        val options = RayHandlerOptions()
        options.isDiffuse = true
        options.gammaCorrection = true
        options.setPseudo3d(true)
        // BOX2D LIGHT STUFF BEGIN
        rayHandler = RayHandler(world, options)

        rayHandler!!.setShadows(true)
        initPointLights()

        // BOX2D LIGHT STUFF END
    }

    override fun render() {
        // Rotate directional light like sun :)

        if (lightsType == 3) {
            sunDirection += Gdx.graphics.deltaTime * 8f
            val degrees = (sunDirection % 360)
            lights[0].setDirection(degrees)
            lights[0].setHeight(degrees)
            Gdx.app.debug("Degrees:", degrees.toString() + "")
        }

        camera.update()

        val stepped = fixedStep(Gdx.graphics.deltaTime)
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BITS or GL20.GL_BLEND_SRC_ALPHA)

        batch.setProjectionMatrix(camera.combined)
        batch.disableBlending()
        batch.begin()
        run {
            batch.draw(bg, -VIEW_PORT_WIDTH / 2f, 0f, VIEW_PORT_WIDTH, VIEW_PORT_HEIGHT)
            batch.enableBlending()
            for (i in 0..<BALLS_NUM) {
                val ball = balls[i]
                val position = ball.getPosition()
                val angle = MathUtils.radiansToDegrees * ball.angle
                batch.draw(
                    textureRegion,
                    position.x - RADIUS, position.y - RADIUS,
                    RADIUS, RADIUS,
                    RADIUS * 2, RADIUS * 2,
                    1f, 1f,
                    angle
                )
            }
        }
        batch.end()

        // BOX2D LIGHT STUFF BEGIN
        rayHandler!!.setCombinedMatrix(camera)

        if (stepped) rayHandler!!.update()
        rayHandler!!.render()

        // BOX2D LIGHT STUFF END
        val time = System.nanoTime()

        val atShadow = rayHandler!!.pointAtShadow(
            testPoint.x,
            testPoint.y
        )
        aika += System.nanoTime() - time

        // FONT
        if (showText) {
            batch.setProjectionMatrix(normalProjection)
            batch.begin()

            font.draw(
                batch,
                "F1 - PointLight",
                0f, Gdx.graphics.height.toFloat()
            )
            font.draw(
                batch,
                "F2 - ConeLight",
                0f, (Gdx.graphics.height - 15).toFloat()
            )
            font.draw(
                batch,
                "F3 - ChainLight",
                0f, (Gdx.graphics.height - 30).toFloat()
            )
            font.draw(
                batch,
                "F4 - DirectionalLight",
                0f, (Gdx.graphics.height - 45).toFloat()
            )
            font.draw(
                batch,
                "F5 - random lights colors",
                0f, (Gdx.graphics.height - 75).toFloat()
            )
            font.draw(
                batch,
                "F6 - random lights distance",
                0f, (Gdx.graphics.height - 90).toFloat()
            )
            font.draw(
                batch,
                "F9 - default blending (1.3)",
                0f, (Gdx.graphics.height - 120).toFloat()
            )
            font.draw(
                batch,
                "F10 - over-burn blending (default in 1.2)",
                0f, (Gdx.graphics.height - 135).toFloat()
            )
            font.draw(
                batch,
                "F11 - some other blending",
                0f, (Gdx.graphics.height - 150).toFloat()
            )

            font.draw(
                batch,
                "F12 - toggle help text",
                0f, (Gdx.graphics.height - 180).toFloat()
            )

            font.draw(
                batch,
                (Gdx.graphics.framesPerSecond
                    .toString() + "mouse at shadows: " + atShadow
                        + " time used for shadow calculation:"
                        + aika / ++times + "ns"), 0f, 20f
            )

            batch.end()
        }
    }

    fun clearLights() {
        if (!lights.isEmpty()) {
            for (light in lights) {
                light.remove()
            }
            lights.clear()
        }
        groundBody!!.isActive = true
    }

    fun initPointLights() {
        clearLights()
        for (i in 0..<BALLS_NUM) {
            val light = PointLight(
                rayHandler!!, RAYS_PER_BALL, null, LIGHT_DISTANCE, 0f, 0f
            )
            light.attachToBody(balls[i], RADIUS / 2f, RADIUS / 2f)
            light.setColor(
                MathUtils.random(),
                MathUtils.random(),
                MathUtils.random(),
                1f
            )
            light.setHeight((i + 2).toFloat())
            lights.add(light)
        }
    }

    fun initConeLights() {
        clearLights()
        for (i in 0..<BALLS_NUM) {
            val light = ConeLight(
                rayHandler, RAYS_PER_BALL, null, LIGHT_DISTANCE,
                0f, 0f, 0f, MathUtils.random(15f, 40f)
            )
            light.attachToBody(
                balls[i],
                RADIUS / 2f, RADIUS / 2f, MathUtils.random(0f, 360f)
            )
            light.setColor(
                MathUtils.random(),
                MathUtils.random(),
                MathUtils.random(),
                1f
            )
            lights.add(light)
        }
    }

    fun initChainLights() {
        clearLights()
        for (i in 0..<BALLS_NUM) {
            val light = ChainLight(
                rayHandler, RAYS_PER_BALL, null, LIGHT_DISTANCE, 1,
                floatArrayOf(-5f, 0f, 0f, 3f, 5f, 0f)
            )
            light.attachToBody(
                balls[i],
                MathUtils.random(0f, 360f)
            )
            light.setColor(
                MathUtils.random(),
                MathUtils.random(),
                MathUtils.random(),
                1f
            )
            lights.add(light)
        }
    }

    fun initDirectionalLight() {
        clearLights()

        groundBody!!.isActive = false
        sunDirection = MathUtils.random(0f, 360f)

        val light = DirectionalLight(
            rayHandler, 4 * RAYS_PER_BALL, Color(1f, 1f, 1f, 0.5f), sunDirection
        )
        light.setHeight(0f)
        lights.add(light)
    }

    private fun fixedStep(delta: Float): Boolean {
        physicsTimeLeft += delta
        if (physicsTimeLeft > MAX_TIME_PER_FRAME) physicsTimeLeft = MAX_TIME_PER_FRAME

        var stepped = false
        while (physicsTimeLeft >= TIME_STEP) {
            world!!.step(TIME_STEP, VELOCITY_ITERS, POSITION_ITERS)
            physicsTimeLeft -= TIME_STEP
            stepped = true
        }
        return stepped
    }

    private fun createPhysicsWorld() {
        world = World(Vector2(0f, 0f), true)

        val halfWidth: Float = VIEW_PORT_WIDTH / 2f
        val chainShape = ChainShape()
        chainShape.createLoop(
            arrayOf(
                Vector2(-halfWidth, 0f),
                Vector2(halfWidth, 0f),
                Vector2(halfWidth, VIEW_PORT_HEIGHT),
                Vector2(-halfWidth, VIEW_PORT_HEIGHT)
            )
        )
        val chainBodyDef = BodyDef()
        chainBodyDef.type = BodyType.StaticBody
        groundBody = world!!.createBody(chainBodyDef)
        groundBody!!.createFixture(chainShape, 0f)
        chainShape.dispose()
        createBoxes()
    }

    private fun createBoxes() {
        val polygonShape = PolygonShape()
        polygonShape.setAsBox(RADIUS, RADIUS)
        val def = FixtureDef()
        def.restitution = 0.9f
        def.friction = 0.01f
        def.shape = polygonShape
        def.density = 1f
        val boxBodyDef = BodyDef()
        boxBodyDef.type = BodyType.DynamicBody

        for (i in 0..<BALLS_NUM) {
            // Create the BodyDef, set a random position above the
            // ground and create a new body
            boxBodyDef.position.x = -20 + (Math.random() * 40).toFloat()
            boxBodyDef.position.y = 10 + (Math.random() * 15).toFloat()
            val boxBody = world!!.createBody(boxBodyDef)
            boxBody.createFixture(def).setUserData(LightData(1f, true))
            balls.add(boxBody)
        }
        polygonShape.dispose()
    }

    override fun touchDown(x: Int, y: Int, pointer: Int, newParam: Int): Boolean {
        // translate the mouse coordinates to world coordinates
        testPoint.set(x.toFloat(), y.toFloat(), 0f)
        camera.unproject(testPoint)

        // ask the world which bodies are within the given
        // bounding box around the mouse pointer
        hitBody = null
        world!!.QueryAABB(
            callback, testPoint.x - 0.1f, testPoint.y - 0.1f,
            testPoint.x + 0.1f, testPoint.y + 0.1f
        )

        // if we hit something we create a new mouse joint
        // and attach it to the hit body.
        if (hitBody != null) {
            val def = MouseJointDef()
            def.bodyA = groundBody
            def.bodyB = hitBody
            def.collideConnected = true
            def.target.set(testPoint.x, testPoint.y)
            def.maxForce = 1000.0f * hitBody!!.mass

            mouseJoint = world!!.createJoint(def) as MouseJoint?
            hitBody!!.isAwake = true
        }

        return false
    }

    override fun touchDragged(x: Int, y: Int, pointer: Int): Boolean {
        camera.unproject(testPoint.set(x.toFloat(), y.toFloat(), 0f))
        target.set(testPoint.x, testPoint.y)
        // if a mouse joint exists we simply update
        // the target of the joint based on the new
        // mouse coordinates
        if (mouseJoint != null) {
            mouseJoint!!.target = target
        }
        return false
    }

    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // if a mouse joint exists we simply destroy it
        if (mouseJoint != null) {
            world!!.destroyJoint(mouseJoint)
            mouseJoint = null
        }
        return false
    }

    override fun dispose() {
        rayHandler!!.dispose()
        world!!.dispose()
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.F1 -> {
                if (lightsType != 0) {
                    initPointLights()
                    lightsType = 0
                }
                return true
            }

            Input.Keys.F2 -> {
                if (lightsType != 1) {
                    initConeLights()
                    lightsType = 1
                }
                return true
            }

            Input.Keys.F3 -> {
                if (lightsType != 2) {
                    initChainLights()
                    lightsType = 2
                }
                return true
            }

            Input.Keys.F4 -> {
                if (lightsType != 3) {
                    initDirectionalLight()
                    lightsType = 3
                }
                return true
            }

            Input.Keys.F5 -> {
                for (light in lights) light.setColor(
                    MathUtils.random(),
                    MathUtils.random(),
                    MathUtils.random(),
                    1f
                )
                return true
            }

            Input.Keys.F6 -> {
                for (light in lights) light.setDistance(
                    MathUtils.random(
                        LIGHT_DISTANCE * 0.5f, LIGHT_DISTANCE * 2f
                    )
                )
                return true
            }

            Input.Keys.F9 -> {
                rayHandler!!.diffuseBlendFunc.reset()
                return true
            }

            Input.Keys.F10 -> {
                rayHandler!!.diffuseBlendFunc.set(
                    GL20.GL_DST_COLOR, GL20.GL_SRC_COLOR
                )
                return true
            }

            Input.Keys.F11 -> {
                rayHandler!!.diffuseBlendFunc.set(
                    GL20.GL_SRC_COLOR, GL20.GL_DST_COLOR
                )
                return true
            }

            Input.Keys.F12 -> {
                showText = !showText
                return true
            }

            else -> return false
        }
    }

    override fun mouseMoved(x: Int, y: Int): Boolean {
        testPoint.set(x.toFloat(), y.toFloat(), 0f)
        camera.unproject(testPoint)
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        camera.rotate(amount.toFloat() * 3f, 0f, 0f, 1f)
        return false
    }

    override fun pause() {
    }

    override fun resize(arg0: Int, arg1: Int) {
    }

    override fun resume() {
    }

    companion object {
        const val RAYS_PER_BALL = 128
        const val BALLS_NUM = 5
        const val LIGHT_DISTANCE = 16f
        const val RADIUS = 1f

        const val VIEW_PORT_WIDTH = 48f
        const val VIEW_PORT_HEIGHT = 32f
        private const val MAX_FPS = 30
        const val TIME_STEP = 1f / MAX_FPS
        private const val MIN_FPS = 15
        private const val MAX_STEPS = 1f + MAX_FPS / MIN_FPS
        private const val MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS
        private const val VELOCITY_ITERS = 6
        private const val POSITION_ITERS = 2
    }
}
