package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Cubemap
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.mygdx.game.enums.CameraMode
import com.mygdx.game.shaders.CustomShaderProvider
import com.mygdx.game.terrains.HeightMapTerrain
import com.mygdx.game.terrains.Terrain
import com.mygdx.game.terrains.attributes.TerrainFloatAttribute
import com.mygdx.game.terrains.attributes.TerrainMaterialAttribute
import net.mgsx.gltf.loaders.gltf.GLTFLoader
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx
import net.mgsx.gltf.scene3d.scene.Scene
import net.mgsx.gltf.scene3d.scene.SceneAsset
import net.mgsx.gltf.scene3d.scene.SceneManager
import net.mgsx.gltf.scene3d.scene.SceneSkybox
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider
import net.mgsx.gltf.scene3d.utils.IBLBuilder
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class MyGdxGame : ApplicationAdapter(), AnimationListener, InputProcessor {
    private var sceneManager: SceneManager? = null
    private var sceneAsset: SceneAsset? = null
    private var playerScene: Scene? = null
    private var camera: PerspectiveCamera? = null
    private var diffuseCubemap: Cubemap? = null
    private var environmentCubemap: Cubemap? = null
    private var specularCubemap: Cubemap? = null
    private var brdfLUT: Texture? = null

    private var skybox: SceneSkybox? = null
    private var light: DirectionalLightEx? = null
    private var cameraController: FirstPersonCameraController? = null

    // Player Movement
    var speed: Float = 5f
    var rotationSpeed: Float = 80f
    private val playerTransform = Matrix4()
    private val moveTranslation = Vector3()
    private val currentPosition = Vector3()

    // Camera
    private var cameraMode = CameraMode.BEHIND_PLAYER
    private var camPitch = Settings.CAMERA_START_PITCH
    private var distanceFromPlayer = 35f
    private var angleAroundPlayer = 0f
    private var angleBehindPlayer = 0f

    private var terrain: Terrain? = null
    private var terrainScene: Scene? = null

    override fun create() {
        // create scene

        sceneAsset = GLTFLoader().load(Gdx.files.internal("models/Alien Slime.gltf"))
        playerScene = Scene(sceneAsset!!.scene)
        sceneManager = SceneManager(CustomShaderProvider(), PBRShaderProvider.createDefaultDepth(24))
        sceneManager!!.addScene(playerScene)

        camera = PerspectiveCamera(60f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera!!.near = 1f
        camera!!.far = 1000f
        sceneManager!!.setCamera(camera)
        camera!!.position[0f, 0f] = 4f

        cameraController = FirstPersonCameraController(camera)
        cameraController!!.setVelocity(100f)

        Gdx.input.isCursorCatched = true
        Gdx.input.inputProcessor = InputMultiplexer(cameraController, this)

        // setup light
        light = DirectionalLightEx()
        light!!.direction.set(1f, -3f, 1f).nor()
        light!!.color.set(Color.WHITE)
        sceneManager!!.environment.add(light)

        // setup quick IBL (image based lighting)
        val iblBuilder = IBLBuilder.createOutdoor(light)
        environmentCubemap = iblBuilder.buildEnvMap(1024)
        diffuseCubemap = iblBuilder.buildIrradianceMap(256)
        specularCubemap = iblBuilder.buildRadianceMap(10)
        iblBuilder.dispose()

        // This texture is provided by the library, no need to have it in your assets.
        brdfLUT = Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"))

        sceneManager!!.setAmbientLight(0.6f)
        sceneManager!!.environment.set(PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT))
        sceneManager!!.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap))
        sceneManager!!.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap))

        // setup skybox
        skybox = SceneSkybox(environmentCubemap)
        sceneManager!!.skyBox = skybox

        playerScene!!.animationController.setAnimation("idle", -1)
        createTerrain()
    }

    private fun createTerrain() {
        if (terrain != null) {
            terrain!!.dispose()
            sceneManager!!.removeScene(terrainScene)
        }

        terrain = HeightMapTerrain(Pixmap(Gdx.files.internal("textures/heightmap.png")), 60f)
        terrainScene = Scene(terrain!!.getModelInstance())
        sceneManager!!.addScene(terrainScene)
    }

    override fun resize(width: Int, height: Int) {
        sceneManager!!.updateViewport(width.toFloat(), height.toFloat())
    }

    override fun render() {
        val deltaTime = Gdx.graphics.deltaTime

        processInput(deltaTime)
        updateCamera(deltaTime)

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) playerScene!!.animationController.action("jump", 1, 1f, this, 0.5f)

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            createTerrain()
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F2)) {
            val mat = terrain!!.getModelInstance()!!.materials[0]
            val terrainMaterial = (mat[TerrainMaterialAttribute.TerrainMaterial] as TerrainMaterialAttribute).terrainMaterial
            val attr = terrainMaterial!!.get(TerrainFloatAttribute.MinSlope) as TerrainFloatAttribute?
            attr!!.value += 0.01f
            attr!!.value = min(attr.value.toDouble(), 0.9).toFloat()
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F3)) {
            val mat = terrain!!.getModelInstance()!!.materials[0]
            val terrainMaterial = (mat[TerrainMaterialAttribute.TerrainMaterial] as TerrainMaterialAttribute).terrainMaterial
            val attr = terrainMaterial!!.get(TerrainFloatAttribute.MinSlope) as TerrainFloatAttribute?
            attr!!.value -= 0.01f
        }

        // render
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        sceneManager!!.update(deltaTime)
        sceneManager!!.render()
    }

    private fun processInput(deltaTime: Float) {
        // Update the player transform
        playerTransform.set(playerScene!!.modelInstance.transform)

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveTranslation.z += speed * deltaTime
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveTranslation.z -= speed * deltaTime
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerTransform.rotate(Vector3.Y, rotationSpeed * deltaTime)
            angleBehindPlayer += rotationSpeed * deltaTime
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerTransform.rotate(Vector3.Y, -rotationSpeed * deltaTime)
            angleBehindPlayer -= rotationSpeed * deltaTime
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            when (cameraMode) {
                CameraMode.FREE_LOOK -> {
                    cameraMode = CameraMode.BEHIND_PLAYER
                    angleAroundPlayer = angleBehindPlayer
                }

                CameraMode.BEHIND_PLAYER -> cameraMode = CameraMode.FLY_MODE
                CameraMode.FLY_MODE -> cameraMode = CameraMode.FREE_LOOK
            }
        }

        // Apply the move translation to the transform
        playerTransform.translate(moveTranslation)

        // Set the modified transform
        playerScene!!.modelInstance.transform.set(playerTransform)

        // Update vector position
        playerScene!!.modelInstance.transform.getTranslation(currentPosition)

        currentPosition.y = terrain!!.getHeightAtWorldCoord(currentPosition.x, currentPosition.z)

        // Apply terrain height to the slime
        playerScene!!.modelInstance.transform.setTranslation(currentPosition)

        // Clear the move translation out
        moveTranslation[0f, 0f] = 0f
    }

    private fun updateCamera(delta: Float) {
        if (cameraMode == CameraMode.FLY_MODE) {
            cameraController!!.update(delta)
            return
        }
        val horDistance = calculateHorizontalDistance(distanceFromPlayer)
        val vertDistance = calculateVerticalDistance(distanceFromPlayer)

        calculatePitch()
        calculateAngleAroundPlayer()
        calculateCameraPosition(currentPosition, horDistance, vertDistance)

        val height = terrain!!.getHeightAtWorldCoord(camera!!.position.x, camera!!.position.z)
        if (camera!!.position.y < height + 10f) {
            camera!!.position.y = height + 10f
        }

        camera!!.lookAt(currentPosition)
        camera!!.up.set(Vector3.Y)
        camera!!.update()
    }

    private fun calculateCameraPosition(currentPosition: Vector3, horDistance: Float, vertDistance: Float) {
        val offsetX = (horDistance * sin(Math.toRadians(angleAroundPlayer.toDouble()))).toFloat()
        val offsetZ = (horDistance * cos(Math.toRadians(angleAroundPlayer.toDouble()))).toFloat()

        camera!!.position.x = currentPosition.x - offsetX
        camera!!.position.z = currentPosition.z - offsetZ
        camera!!.position.y = currentPosition.y + vertDistance
    }

    private fun calculateAngleAroundPlayer() {
        if (cameraMode == CameraMode.FREE_LOOK) {
            val angleChange = Gdx.input.deltaX * Settings.CAMERA_ANGLE_AROUND_PLAYER_FACTOR
            angleAroundPlayer -= angleChange
        } else {
            angleAroundPlayer = angleBehindPlayer
        }
    }

    private fun calculatePitch() {
        val pitchChange = -Gdx.input.deltaY * Settings.CAMERA_PITCH_FACTOR
        camPitch -= pitchChange

        if (camPitch < Settings.CAMERA_MIN_PITCH) camPitch = Settings.CAMERA_MIN_PITCH
        else if (camPitch > Settings.CAMERA_MAX_PITCH) camPitch = Settings.CAMERA_MAX_PITCH
    }

    private fun calculateVerticalDistance(distanceFromPlayer: Float): Float {
        return (distanceFromPlayer * sin(Math.toRadians(camPitch.toDouble()))).toFloat()
    }

    private fun calculateHorizontalDistance(distanceFromPlayer: Float): Float {
        return (distanceFromPlayer * cos(Math.toRadians(camPitch.toDouble()))).toFloat()
    }

    override fun dispose() {
        sceneManager!!.dispose()
        sceneAsset!!.dispose()
        environmentCubemap!!.dispose()
        diffuseCubemap!!.dispose()
        specularCubemap!!.dispose()
        brdfLUT!!.dispose()
        skybox!!.dispose()
    }

    override fun onEnd(animation: AnimationDesc) {
    }

    override fun onLoop(animation: AnimationDesc) {
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        val zoomLevel = amountY * Settings.CAMERA_ZOOM_LEVEL_FACTOR
        distanceFromPlayer += zoomLevel
        if (distanceFromPlayer < Settings.CAMERA_MIN_DISTANCE_FROM_PLAYER) distanceFromPlayer = Settings.CAMERA_MIN_DISTANCE_FROM_PLAYER
        return false
    }
}
