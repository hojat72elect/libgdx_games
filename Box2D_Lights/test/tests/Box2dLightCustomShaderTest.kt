package tests

import box2dLight.ChainLight
import box2dLight.ConeLight
import box2dLight.DirectionalLight
import box2dLight.Light
import box2dLight.PointLight
import box2dLight.RayHandler
import box2dLight.RayHandlerOptions
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.QueryCallback
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.joints.MouseJoint
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.FitViewport

class Box2dLightCustomShaderTest : InputAdapter(), ApplicationListener {
    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: FitViewport
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont

    /**
     * our box2D world
     */
    var world: World? = null

    /**
     * our boxes
     */
    var balls: ArrayList<Body> = ArrayList<Body>(BALLS_NUM)

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
    var lights: ArrayList<Light> = ArrayList<Light>(BALLS_NUM)
    var sunDirection: Float = -90f
    var bg: Texture? = null
    var bgN: Texture? = null
    var objectReg: TextureRegion? = null
    var objectRegN: TextureRegion? = null
    var normalFbo: FrameBuffer? = null
    var assetArray: Array<DeferredObject> = Array<DeferredObject>()
    var marble: DeferredObject? = null
    var lightShader: ShaderProgram? = null
    var normalShader: ShaderProgram? = null
    var drawNormals: Boolean = false
    var bgColor: Color = Color()
    var physicsTimeLeft: Float = 0f
    var aika: Long = 0
    var times: Int = 0

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
    var target: Vector2 = Vector2()

    /**
     * Type of lights to use:
     * 0 - PointLight
     * 1 - ConeLight
     * 2 - ChainLight
     * 3 - DirectionalLight
     */
    var lightsType: Int = 0
    var once: Boolean = true

    override fun create() {
        bg = Texture(Gdx.files.internal("test/data/bg-deferred.png"))
        bgN = Texture(Gdx.files.internal("test/data/bg-deferred-n.png"))

        MathUtils.random.setSeed(Long.Companion.MIN_VALUE)

        camera = OrthographicCamera(VIEW_PORT_WIDTH, VIEW_PORT_HEIGHT)
        camera.update()

        viewport = FitViewport(VIEW_PORT_WIDTH, VIEW_PORT_HEIGHT, camera)

        batch = SpriteBatch()
        font = BitmapFont()
        font.color = Color.RED

        val marbleD = TextureRegion(Texture(Gdx.files.internal("test/data/marble.png")))

        val marbleN = TextureRegion(Texture(Gdx.files.internal("test/data/marble-n.png")))

        marble = DeferredObject(marbleD, marbleN)
        marble!!.width = RADIUS * 2
        marble!!.height = RADIUS * 2

        createPhysicsWorld()
        Gdx.input.inputProcessor = this

        normalProjection.setToOrtho2D(0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        // BOX2D LIGHT STUFF BEGIN
        normalShader = createNormalShader()

        lightShader = createLightShader()
        val options = RayHandlerOptions()
        options.isDiffuse = true
        options.gammaCorrection = true
        rayHandler = object : RayHandler(world, Gdx.graphics.width, Gdx.graphics.height, options) {
            override fun updateLightShaderPerLight(light: Light) {
                // light position must be normalized
                val x = (light.getX()) / VIEW_PORT_WIDTH
                val y = (light.getY()) / VIEW_PORT_HEIGHT
                lightShader!!.setUniformf("u_lightpos", x, y, 0.05f)
                lightShader!!.setUniformf("u_intensity", 5f)
            }
        }
        rayHandler!!.setLightShader(lightShader)
        rayHandler!!.setAmbientLight(0.1f, 0.1f, 0.1f, 0.5f)
        rayHandler!!.setBlurNum(0)

        initPointLights()


        // BOX2D LIGHT STUFF END
        objectReg = TextureRegion(Texture(Gdx.files.internal("test/data/object-deferred.png")))
        objectRegN = TextureRegion(Texture(Gdx.files.internal("test/data/object-deferred-n.png")))

        for (x in 0..3) {
            for (y in 0..2) {
                val deferredObject = DeferredObject(objectReg!!, objectRegN)
                deferredObject.x = 4 + x * (deferredObject.diffuse.getRegionWidth() * SCALE + 8)
                deferredObject.y = 4 + y * (deferredObject.diffuse.getRegionHeight() * SCALE + 7)
                deferredObject.color.set(MathUtils.random(0.5f, 1f), MathUtils.random(0.5f, 1f), MathUtils.random(0.5f, 1f), 1f)
                if (x > 0) deferredObject.rot = true
                deferredObject.rotation = MathUtils.random(90).toFloat()
                assetArray.add(deferredObject)
            }
        }
        once = false
        normalFbo = FrameBuffer(Pixmap.Format.RGB565, Gdx.graphics.width, Gdx.graphics.height, false)
    }

    private fun createLightShader(): ShaderProgram {
        // Shader adapted from https://github.com/mattdesl/lwjgl-basics/wiki/ShaderLesson6
        val vertexShader =
            ("attribute vec4 vertex_positions;\n"
                    + "attribute vec4 quad_colors;\n"
                    + "attribute float s;\n"
                    + "uniform mat4 u_projTrans;\n"
                    + "varying vec4 v_color;\n"
                    + "void main()\n"
                    + "{\n"
                    + "   v_color = s * quad_colors;\n"
                    + "   gl_Position =  u_projTrans * vertex_positions;\n"
                    + "}\n")

        val fragmentShader = ("#ifdef GL_ES\n"
                + "precision lowp float;\n"
                + "#define MED mediump\n"
                + "#else\n"
                + "#define MED \n"
                + "#endif\n"
                + "varying vec4 v_color;\n"
                + "uniform sampler2D u_normals;\n"
                + "uniform vec3 u_lightpos;\n"
                + "uniform vec2 u_resolution;\n"
                + "uniform float u_intensity = 1.0;\n"
                + "void main()\n"
                + "{\n"
                + "  vec2 screenPos = gl_FragCoord.xy / u_resolution.xy;\n"
                + "  vec3 NormalMap = texture2D(u_normals, screenPos).rgb; "
                + "  vec3 LightDir = vec3(u_lightpos.xy - screenPos, u_lightpos.z);\n"

                + "  vec3 N = normalize(NormalMap * 2.0 - 1.0);\n"

                + "  vec3 L = normalize(LightDir);\n"

                + "  float maxProd = max(dot(N, L), 0.0);\n"
                + "  gl_FragColor = v_color * maxProd * u_intensity;\n"
                + "}")

        ShaderProgram.pedantic = false
        val lightShader = ShaderProgram(
            vertexShader,
            fragmentShader
        )
        if (!lightShader.isCompiled) {
            Gdx.app.log("ERROR", lightShader.log)
        }

        lightShader.begin()
        lightShader.setUniformi("u_normals", 1)
        lightShader.setUniformf("u_resolution", Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        lightShader.end()

        return lightShader
    }

    private fun createNormalShader(): ShaderProgram {
        val vertexShader = ("attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
                + "uniform mat4 u_projTrans;\n"
                + "uniform float u_rot;\n"
                + "varying vec4 v_color;\n"
                + "varying vec2 v_texCoords;\n"
                + "varying mat2 v_rot;\n"
                + "\n"
                + "void main()\n"
                + "{\n"
                + "   vec2 rad = vec2(-sin(u_rot), cos(u_rot));\n"
                + "   v_rot = mat2(rad.y, -rad.x, rad.x, rad.y);\n"
                + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
                + "   v_color.a = v_color.a * (255.0/254.0);\n"
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
                + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
                + "}\n")

        val fragmentShader = ("#ifdef GL_ES\n"
                + "#define LOWP lowp\n"
                + "precision mediump float;\n"
                + "#else\n"
                + "#define LOWP \n"
                + "#endif\n"
                + "varying LOWP vec4 v_color;\n"
                + "varying vec2 v_texCoords;\n"
                + "varying mat2 v_rot;\n"
                + "uniform sampler2D u_texture;\n"
                + "void main()\n"
                + "{\n"
                + "  vec4 normal = texture2D(u_texture, v_texCoords).rgba;\n" // got to translate normal vector to -1, 1 range
                + "  vec2 rotated = v_rot * (normal.xy * 2.0 - 1.0);\n" // and back to 0, 1
                + "  rotated = (rotated.xy / 2.0 + 0.5 );\n"
                + "  gl_FragColor = vec4(rotated.xy, normal.z, normal.a);\n"
                + "}")

        val shader = ShaderProgram(vertexShader, fragmentShader)
        require(shader.isCompiled) { "Error compiling shader: " + shader.log }
        return shader
    }

    override fun render() {
        // Rotate directional light like sun :)

        if (lightsType == 3) {
            sunDirection += Gdx.graphics.deltaTime * 4f
            lights[0].setDirection(sunDirection)
        }

        camera.update()

        val stepped = fixedStep(Gdx.graphics.deltaTime)
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.setProjectionMatrix(camera.combined)
        for (deferredObject in assetArray) {
            deferredObject.update()
        }
        normalFbo!!.begin()
        batch.disableBlending()
        batch.begin()
        batch.setShader(normalShader)
        normalShader!!.setUniformf("u_rot", 0f)
        val bgWidth: Float = bgN!!.width * SCALE
        val bgHeight: Float = bgN!!.height * SCALE
        for (x in 0..5) {
            for (y in 0..5) {
                batch.draw(bgN, x * bgWidth, y * bgHeight, bgWidth, bgHeight)
            }
        }
        batch.enableBlending()
        for (deferredObject in assetArray) {
            normalShader!!.setUniformf("u_rot", MathUtils.degreesToRadians * deferredObject.rotation)
            deferredObject.drawNormal(batch)
            // flush batch or uniform wont change
            // TODO this is baaaad, maybe modify SpriteBatch to add rotation in the attributes? Flushing after each defeats the point of batch
            batch.flush()
        }
        for (i in 0..<BALLS_NUM) {
            val ball = balls[i]
            val position = ball.getPosition()
            val angle = MathUtils.radiansToDegrees * ball.angle
            marble!!.x = position.x - RADIUS
            marble!!.y = position.y - RADIUS
            marble!!.rotation = angle
            normalShader!!.setUniformf("u_rot", MathUtils.degreesToRadians * marble!!.rotation)
            marble!!.drawNormal(batch)
            batch.flush()
        }
        batch.end()
        normalFbo!!.end()

        val normals = normalFbo!!.colorBufferTexture

        batch.disableBlending()
        batch.begin()
        batch.setShader(null)
        if (drawNormals) {
            // draw flipped so it looks ok
            batch.draw(
                normals, 0f, 0f,  // x, y
                VIEW_PORT_WIDTH / 2, VIEW_PORT_HEIGHT / 2,  // origx, origy
                VIEW_PORT_WIDTH, VIEW_PORT_HEIGHT,  // width, height
                1f, 1f,  // scale x, y
                0f,  // rotation
                0, 0, normals.width, normals.height,  // tex dimensions
                false, true
            ) // flip x, y
        } else {
            for (x in 0..5) {
                for (y in 0..5) {
                    batch.setColor(bgColor.set(x / 5.0f, y / 6.0f, 0.5f, 1f))
                    batch.draw(bg, x * bgWidth, y * bgHeight, bgWidth, bgHeight)
                }
            }
            batch.setColor(Color.WHITE)
            batch.enableBlending()
            for (deferredObject in assetArray) {
                deferredObject.draw(batch)
            }
            for (i in 0..<BALLS_NUM) {
                val ball = balls[i]
                val position = ball.getPosition()
                val angle = MathUtils.radiansToDegrees * ball.angle
                marble!!.x = position.x - RADIUS
                marble!!.y = position.y - RADIUS
                marble!!.rotation = angle
                marble!!.draw(batch)
            }
        }
        batch.end()

        // BOX2D LIGHT STUFF BEGIN
        if (!drawNormals) {
            rayHandler!!.setCombinedMatrix(camera)
            if (stepped) rayHandler!!.update()
            normals.bind(1)
            rayHandler!!.render()
        }

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

            font.draw(batch, "F1 - PointLight", 0f, Gdx.graphics.height.toFloat())
            font.draw(batch, "F2 - ConeLight", 0f, Gdx.graphics.height - 15F)
            font.draw(batch, "F3 - ChainLight", 0f, Gdx.graphics.height - 30F)
            font.draw(batch, "F4 - DirectionalLight", 0f, Gdx.graphics.height - 45F)
            font.draw(batch, "F5 - random lights colors", 0f, Gdx.graphics.height - 75F)
            font.draw(batch, "F6 - random lights distance", 0f, Gdx.graphics.height - 90F)
            font.draw(batch, "F7 - toggle drawing of normals", 0f, Gdx.graphics.height - 105F)
            font.draw(batch, "F9 - default blending (1.3)", 0f, Gdx.graphics.height - 120F)
            font.draw(batch, "F10 - over-burn blending (default in 1.2)", 0f, Gdx.graphics.height - 135F)
            font.draw(batch, "F11 - some other blending", 0f, Gdx.graphics.height - 150F)

            font.draw(batch, "F12 - toggle help text", 0f, Gdx.graphics.height - 180F)

            font.draw(batch, ("${Gdx.graphics.framesPerSecond} mouse at shadows: $atShadow time used for shadow calculation: ${aika / ++times}ns"), 0f, 20f)

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
            rayHandler, 4 * RAYS_PER_BALL, null, sunDirection
        )
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

        val chainShape = ChainShape()
        chainShape.createLoop(
            arrayOf(
                Vector2(0f, 0f),
                Vector2(VIEW_PORT_WIDTH, 0f),
                Vector2(VIEW_PORT_WIDTH, VIEW_PORT_HEIGHT),
                Vector2(0f, VIEW_PORT_HEIGHT)
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
        val ballShape = CircleShape()
        ballShape.radius = RADIUS

        val def = FixtureDef()
        def.restitution = 0.9f
        def.friction = 0.01f
        def.shape = ballShape
        def.density = 1f
        val boxBodyDef = BodyDef()
        boxBodyDef.type = BodyType.DynamicBody

        for (i in 0..<BALLS_NUM) {
            // Create the BodyDef, set a random position above the
            // ground and create a new body
            boxBodyDef.position.x = 1 + (Math.random() * (VIEW_PORT_WIDTH - 2)).toFloat()
            boxBodyDef.position.y = 1 + (Math.random() * (VIEW_PORT_HEIGHT - 2)).toFloat()
            val boxBody = world!!.createBody(boxBodyDef)
            boxBody.createFixture(def)
            boxBody.isFixedRotation = true
            balls.add(boxBody)
        }
        ballShape.dispose()
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

        objectReg!!.getTexture().dispose()
        objectRegN!!.getTexture().dispose()

        normalFbo!!.dispose()
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
                for (light in lights) light.setDistance(MathUtils.random(LIGHT_DISTANCE * 0.5f, LIGHT_DISTANCE * 2f))
                return true
            }

            Input.Keys.F7 -> {
                drawNormals = !drawNormals
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

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun resume() {
    }

    class DeferredObject(var diffuse: TextureRegion, var normal: TextureRegion?) {
        var color: Color = Color(Color.WHITE)
        var x: Float = 0f
        var y: Float = 0f
        var width: Float = diffuse.getRegionWidth() * SCALE
        var height: Float = diffuse.getRegionHeight() * SCALE
        var rotation: Float = 0f
        var rot: Boolean = false

        fun update() {
            if (rot) {
                rotation += 1f
                if (rotation > 360) rotation = 0f
            }
        }

        fun drawNormal(batch: Batch) {
            batch.draw(normal, x, y, width / 2, height / 2, width, height, 1f, 1f, rotation)
        }

        fun draw(batch: Batch) {
            batch.color = color
            batch.draw(diffuse, x, y, width / 2, height / 2, width, height, 1f, 1f, rotation)
            batch.color = Color.WHITE
        }
    }

    companion object {
        const val SCALE = 1f / 16f
        const val VIEW_PORT_WIDTH = 48f
        const val VIEW_PORT_HEIGHT = 32f
        const val RAYS_PER_BALL = 64
        const val BALLS_NUM = 8
        const val LIGHT_DISTANCE = 16f
        const val RADIUS = 1f
        private const val MAX_FPS = 30
        const val TIME_STEP = 1f / MAX_FPS
        private const val MIN_FPS = 15
        private const val MAX_STEPS = 1f + MAX_FPS.toFloat() / MIN_FPS
        private const val MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS
        private const val VELOCITY_ITERS = 6
        private const val POSITION_ITERS = 2
    }
}
