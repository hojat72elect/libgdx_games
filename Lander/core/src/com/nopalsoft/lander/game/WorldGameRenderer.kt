package com.nopalsoft.lander.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.nopalsoft.lander.Assets
import com.nopalsoft.lander.game.objetos.Bomba
import com.nopalsoft.lander.game.objetos.Laser
import com.nopalsoft.lander.game.objetos.Nave
import com.nopalsoft.lander.screens.Screens

class WorldGameRenderer(val batcher: SpriteBatch, val oWorld: WorldGame) {


    var cameraMaxX = Assets.map!!.properties.get("tamanoMapaX", String::class.java).toFloat() * oWorld.unitScale * 32 - (WIDTH / 2f)
    var cameraMaxY = Assets.map!!.properties.get("tamanoMapaY", String::class.java).toFloat() * oWorld.unitScale * 32 - (HEIGHT / 2f)
    var camera = OrthographicCamera(WIDTH, HEIGHT)
    var tiledRenderer = OrthogonalTiledMapRenderer(Assets.map, oWorld.unitScale)
    var renderBox = Box2DDebugRenderer()

    init {
        this.camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0f)
    }

    fun render() {
        camera.position.x = oWorld.oNave!!.position.x
        camera.position.y = oWorld.oNave!!.position.y

        if (camera.position.y < CAM_MIN_Y) camera.position.y = CAM_MIN_Y
        if (camera.position.x < CAM_MIN_X) camera.position.x = CAM_MIN_X

        if (camera.position.y > cameraMaxY) camera.position.y = cameraMaxY

        if (camera.position.x > cameraMaxX) camera.position.x = cameraMaxX

        camera.update()
        batcher.setProjectionMatrix(camera.combined)

        renderTiled()

        batcher.enableBlending()
        batcher.begin()
        renderNave()
        renderGas()
        renderEstrella()
        renderLaser()
        renderBombas()
        batcher.end()

        if (Assets.isDebug) {
            renderBox.render(oWorld.oWorldBox, camera.combined)
        }
    }

    fun renderTiled() {
        tiledRenderer.setView(camera)
        tiledRenderer.render()
    }

    fun renderNave() {
        val obj = oWorld.oNave

        val keyframe: TextureRegion?

        if (obj!!.state == Nave.STATE_NORMAL) {
            keyframe = if (obj.isFlying) Assets.naveFly.getKeyFrame(obj.stateTime, true)
            else Assets.nave
            batcher.draw(
                keyframe,
                obj.position.x - Nave.DRAW_WIDTH / 2f,
                obj.position.y - 1.025f,
                Nave.DRAW_WIDTH / 2f,
                1.025f,
                Nave.DRAW_WIDTH,
                Nave.DRAW_HEIGHT,
                1f,
                1f,
                Math.toDegrees(obj.angleRad.toDouble()).toFloat()
            )
        } else {
            keyframe = Assets.explosion.getKeyFrame(obj.stateTime, false)
            batcher.draw(keyframe, obj.position.x - .5f, obj.position.y - .5f, .5f, .5f, 1f, 1f, 1f, 1f, Math.toDegrees(obj.angleRad.toDouble()).toFloat())
        }
    }

    fun renderGas() {
        for (obj in oWorld.arrGas) {
            batcher.draw(Assets.gas, obj.position.x - .25f, obj.position.y - .25f, .5f, .5f)
        }
    }

    fun renderEstrella() {
        for (obj in oWorld.arrEstrellas) {
            batcher.draw(Assets.star, obj.position.x - .25f, obj.position.y - .25f, .5f, .5f)
        }
    }

    fun renderLaser() {
        for (obj in oWorld.arrLaser) {
            if (obj.directionInteger == Laser.DIRECCION_HORIZONTAL) {
                if (obj.state == Laser.STATE_FIRE) batcher.draw(Assets.laser.getKeyFrame(obj.stateTime, true), obj.position.x - obj.width / 2f, obj.position.y - obj.height / 2f, obj.width, obj.height)
            } else {
                if (obj.state == Laser.STATE_FIRE) batcher.draw(
                    Assets.laserVertical.getKeyFrame(obj.stateTime, true),
                    obj.position.x - obj.width / 2f,
                    obj.position.y - obj.height / 2f,
                    obj.width,
                    obj.height
                )
            }
        }
    }

    fun renderBombas() {
        for (obj in oWorld.arrBombas) {
            val keyframe: TextureRegion?
            if (obj.state == Bomba.STATE_EXPLOSION) {
                keyframe = Assets.explosion.getKeyFrame(obj.stateTime, false)
                batcher.draw(keyframe, obj.position.x - .4f, obj.position.y - .4f, .8f, .8f)
            } else {
                keyframe = Assets.bomba
                batcher.draw(keyframe, obj.position.x - .25f, obj.position.y - .25f, .5f, .5f)
            }
        }
    }

    companion object {
        private const val WIDTH = Screens.WORLD_SCREEN_WIDTH
        private const val HEIGHT = Screens.WORLD_SCREEN_HEIGHT
        private const val CAM_MIN_X = WIDTH / 2F
        private const val CAM_MIN_Y = HEIGHT / 2F
    }
}
