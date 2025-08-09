package com.nopalsoft.ponyrace.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.esotericsoftware.spine.Animation
import com.esotericsoftware.spine.SkeletonRenderer
import com.nopalsoft.ponyrace.AssetsHandler
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.game_objects.Balloons
import com.nopalsoft.ponyrace.game_objects.BloodStone
import com.nopalsoft.ponyrace.game_objects.Bomb
import com.nopalsoft.ponyrace.game_objects.Candy
import com.nopalsoft.ponyrace.game_objects.Chili
import com.nopalsoft.ponyrace.game_objects.Coin
import com.nopalsoft.ponyrace.game_objects.Pony
import com.nopalsoft.ponyrace.game_objects.Wood
import com.nopalsoft.ponyrace.screens.BaseScreen

class WorldTiledRenderer(batch: SpriteBatch, oWorld: TileMapHandler) {
    val WIDTH: Float = BaseScreen.WORLD_WIDTH / 10f
    val HEIGHT: Float = BaseScreen.WORLD_HEIGHT / 10f
    var OrthoCam: OrthographicCamera
    var tiledRender: OrthogonalTiledMapRenderer
    var oWorld: TileMapHandler
    var batch: SpriteBatch
    var renderBox: Box2DDebugRenderer
    var skelrender: SkeletonRenderer
    var fondoLastTime: Float = 0f
    var fondoStateTime: Float = 0f

    init {
        this.OrthoCam = OrthographicCamera(WIDTH, HEIGHT)
        this.OrthoCam.position.set(4f, 2.4f, 0f)

        this.batch = batch
        this.oWorld = oWorld
        tiledRender = OrthogonalTiledMapRenderer(oWorld.game.assetsHandler!!.tiledMap, oWorld.m_units)
        renderBox = Box2DDebugRenderer()
        skelrender = SkeletonRenderer()
    }

    fun render(delta: Float) {
        OrthoCam.position.x = oWorld.oPony!!.position.x
        OrthoCam.position.y = oWorld.oPony!!.position.y

        val xMin: Float
        val xMax: Float
        val yMin: Float
        val yMax: Float

        xMin = 4.0f
        xMax = oWorld.tamanoMapaX - 4f // Menos 4 porque es la mitad de la camara
        yMin = 2.4f
        yMax = oWorld.tamanoMapaY - 2.4f

        // Actualizo la camara para que no se salga de los bounds
        if (OrthoCam.position.x < xMin) OrthoCam.position.x = xMin
        else if (OrthoCam.position.x > xMax) OrthoCam.position.x = xMax

        if (OrthoCam.position.y < yMin) OrthoCam.position.y = yMin
        else if (OrthoCam.position.y > yMax) OrthoCam.position.y = yMax

        OrthoCam.update()
        batch.setProjectionMatrix(OrthoCam.combined)

        if (Settings.isBackGroundEnabled) {
            if (OrthoCam.position.x == oWorld.oPony!!.position.x) {
                if (oWorld.oPony!!.aceleration.x > 0) {
                    fondoLastTime = fondoStateTime
                    fondoStateTime += delta
                } else if (oWorld.oPony!!.aceleration.x < 0 && fondoLastTime > 0) {
                    fondoLastTime = fondoStateTime
                    fondoStateTime -= delta
                }
            }
            batch.begin()
            oWorld.game.assetsHandler!!.fondoAnim!!.apply(oWorld.game.assetsHandler!!.fondoSkeleton, fondoLastTime, fondoStateTime, true, null)
            oWorld.game.assetsHandler!!.fondoSkeleton!!.setX(OrthoCam.position.x)
            oWorld.game.assetsHandler!!.fondoSkeleton!!.setY(1.55f)
            oWorld.game.assetsHandler!!.fondoSkeleton!!.updateWorldTransform()
            oWorld.game.assetsHandler!!.fondoSkeleton!!.update(delta)
            skelrender.draw(batch, oWorld.game.assetsHandler!!.fondoSkeleton!!)
            batch.end()
        }
        renderBackGround()

        batch.begin()
        batch.enableBlending()

        renderFuegos(delta)
        renderBloodStone(delta)
        renderPonyMalo(delta)
        renderPluma(delta)
        renderPony(delta)
        renderBombas(delta)
        renderWoods()
        renderMonedas(delta)
        renderChiles(delta)
        renderGlobos(delta)
        renderDulces(delta)

        batch.end()

        if (AssetsHandler.drawDebugLines) renderBox.render(oWorld.oWorldBox, OrthoCam.combined)
    }

    private fun renderBloodStone(delta: Float) {
        for (obj in oWorld.arrBloodStone) {
            if (!OrthoCam.frustum.sphereInFrustum(obj.position, 3f)) continue

            if (obj.type == BloodStone.Type.SMALL) {
                oWorld.game.assetsHandler!!.bloodStoneAnim!!.apply(oWorld.game.assetsHandler!!.bloodStoneSkeleton, obj.lastStateTime, obj.stateTime, true, null)
                oWorld.game.assetsHandler!!.bloodStoneSkeleton!!.setX(obj.position.x)
                oWorld.game.assetsHandler!!.bloodStoneSkeleton!!.setY(obj.position.y - .12f)
                oWorld.game.assetsHandler!!.bloodStoneSkeleton!!.updateWorldTransform()
                oWorld.game.assetsHandler!!.bloodStoneSkeleton!!.update(delta)
                skelrender.draw(batch, oWorld.game.assetsHandler!!.bloodStoneSkeleton!!)
            } else if (obj.type == BloodStone.Type.MEDIUM) {
                oWorld.game.assetsHandler!!.bloodStone2Anim!!.apply(oWorld.game.assetsHandler!!.bloodStone2Skeleton, obj.lastStateTime, obj.stateTime, true, null)
                oWorld.game.assetsHandler!!.bloodStone2Skeleton!!.setX(obj.position.x)
                oWorld.game.assetsHandler!!.bloodStone2Skeleton!!.setY(obj.position.y - .12f)
                oWorld.game.assetsHandler!!.bloodStone2Skeleton!!.updateWorldTransform()
                oWorld.game.assetsHandler!!.bloodStone2Skeleton!!.update(delta)
                skelrender.draw(batch, oWorld.game.assetsHandler!!.bloodStone2Skeleton!!)
            }
        }
    }

    private fun renderBackGround() {
        tiledRender.setView(OrthoCam)
        tiledRender.render()
    }

    private fun renderPony(delta: Float) {
        oWorld.oPony!!.animState.update(delta)

        oWorld.oPony!!.animState.apply(oWorld.oPony!!.ponySkel)

        if (oWorld.oPony!!.state == Pony.STATE_WALK_RIGHT) {
            oWorld.oPony!!.ponySkel.setFlipX(true)
            oWorld.oPony!!.ponySkel.getRootBone().rotation = Math.toDegrees(-oWorld.oPony!!.angleRad.toDouble()).toFloat()
        } else if (oWorld.oPony!!.state == Pony.STATE_WALK_LEFT) {
            oWorld.oPony!!.ponySkel.setFlipX(false)
            oWorld.oPony!!.ponySkel.getRootBone().rotation = Math.toDegrees(oWorld.oPony!!.angleRad.toDouble()).toFloat()
        }

        oWorld.oPony!!.ponySkel.setX(oWorld.oPony!!.position.x)
        oWorld.oPony!!.ponySkel.setY(oWorld.oPony!!.position.y - .26f)

        oWorld.oPony!!.ponySkel.updateWorldTransform()
        skelrender.draw(batch, oWorld.oPony!!.ponySkel)
    }

    private fun renderPonyMalo(delta: Float) {
        for (obj in oWorld.arrPonysMalos) {
            if (!OrthoCam.frustum.sphereInFrustum(obj!!.position, 1f)) continue

            obj.animState.update(delta)
            obj.animState.apply(obj.ponySkel)

            if (obj.aceleration.x >= 0) obj.ponySkel.setFlipX(true)
            else if (obj.aceleration.x < 0) obj.ponySkel.setFlipX(false)

            obj.ponySkel.setX(obj.position.x)
            obj.ponySkel.setY(obj.position.y - .26f)

            obj.ponySkel.updateWorldTransform()
            skelrender.draw(batch, obj.ponySkel)
        }
    }

    private fun renderBombas(delta: Float) {
        for (obj in oWorld.arrBombas) {
            if (!OrthoCam.frustum.sphereInFrustum(obj!!.position, .5f)) continue

            if (obj.state == Bomb.State.NORMAL) {
                oWorld.game.assetsHandler!!.bombAnim!!.apply(obj.skelBomb, obj.lastStatetime, obj.stateTime, true, null)
            } else {
                oWorld.game.assetsHandler!!.bombExAnim!!.apply(obj.skelBomb, obj.lastStatetime, obj.stateTime, true, null)
            }
            obj.skelBomb.setX(obj.position.x + .025f)
            obj.skelBomb.setY(obj.position.y - .15f)
            obj.skelBomb.updateWorldTransform()
            obj.skelBomb.update(delta)

            skelrender.draw(batch, obj.skelBomb)
        }
    }

    private fun renderWoods() {
        for (obj in oWorld.arrWoods) {
            if (!OrthoCam.frustum.sphereInFrustum(obj!!.position, .5f)) continue

            val keyFrame: TextureRegion?

            if (obj.tipo == Wood.Tipo.PLATANO) keyFrame = oWorld.game.assetsHandler!!.platano
            else keyFrame = oWorld.game.assetsHandler!!.tachuelas

            batch.draw(keyFrame, obj.position.x - .25f, obj.position.y - .1f, .5f, .25f)
        }
    }

    private fun renderMonedas(delta: Float) {
        for (obj in oWorld.arrMonedas) {
            if (!OrthoCam.frustum.sphereInFrustum(obj!!.position, 1f)) continue

            val anim: Animation?
            if (obj.state == Coin.State.IDLE) anim = oWorld.game.assetsHandler!!.monedaAnim
            else anim = oWorld.game.assetsHandler!!.monedaTomadaAnim

            anim!!.apply(obj.coinSkeleton, obj.lastStatetime, obj.stateTime, true, null)
            obj.coinSkeleton!!.setX(obj.position.x)
            obj.coinSkeleton!!.setY(obj.position.y)
            obj.coinSkeleton!!.updateWorldTransform()
            obj.coinSkeleton!!.update(delta)
            skelrender.draw(batch, obj.coinSkeleton!!)
        }
    }

    private fun renderChiles(delta: Float) {
        for (obj in oWorld.arrChiles) {
            if (!OrthoCam.frustum.sphereInFrustum(obj!!.position, 1f)) continue

            val anim: Animation?
            if (obj.state == Chili.State.IDLE) anim = oWorld.game.assetsHandler!!.chileAnim
            else anim = oWorld.game.assetsHandler!!.chileTomadaAnim

            anim!!.apply(obj.chiliSkeleton, obj.lastStateTime, obj.stateTime, true, null)
            obj.chiliSkeleton!!.setX(obj.position.x)
            obj.chiliSkeleton!!.setY(obj.position.y - .05f)
            obj.chiliSkeleton!!.updateWorldTransform()
            obj.chiliSkeleton!!.update(delta)
            skelrender.draw(batch, obj.chiliSkeleton!!)
        }
    }

    private fun renderGlobos(delta: Float) {
        for (obj in oWorld.arrGlobos) {
            if (!OrthoCam.frustum.sphereInFrustum(obj!!.position, 1f)) continue

            val anim: Animation?
            if (obj.state == Balloons.State.IDLE) anim = oWorld.game.assetsHandler!!.globoAnim
            else anim = oWorld.game.assetsHandler!!.globoTomadaAnim

            anim!!.apply(obj.balloonsSkeleton, obj.lastStateTime, obj.stateTime, true, null)
            obj.balloonsSkeleton!!.setX(obj.position.x)
            obj.balloonsSkeleton!!.setY(obj.position.y - .36f)
            obj.balloonsSkeleton!!.updateWorldTransform()
            obj.balloonsSkeleton!!.update(delta)
            skelrender.draw(batch, obj.balloonsSkeleton!!)
        }
    }

    private fun renderDulces(delta: Float) {
        for (obj in oWorld.arrDulces) {
            if (!OrthoCam.frustum.sphereInFrustum(obj!!.position, 1f)) continue

            val anim: Animation?
            if (obj.state == Candy.State.NORMAL) anim = oWorld.game.assetsHandler!!.dulceAnim
            else anim = oWorld.game.assetsHandler!!.dulceTomadaAnim

            anim!!.apply(obj.skeleton, obj.lastStateTime, obj.stateTime, true, null)
            obj.skeleton!!.setX(obj.position.x)
            obj.skeleton!!.setY(obj.position.y - .4f)
            obj.skeleton!!.updateWorldTransform()
            obj.skeleton!!.update(delta)
            skelrender.draw(batch, obj.skeleton!!)
        }
    }

    private fun renderPluma(delta: Float) {
        for (obj in oWorld.arrPlumas) {
            if (!OrthoCam.frustum.sphereInFrustum(obj!!.position, 1f)) continue
            oWorld.game.assetsHandler!!.plumaAnim!!.apply(oWorld.game.assetsHandler!!.plumaSkeleton, obj.lastStateTime, obj.stateTime, true, null)
            oWorld.game.assetsHandler!!.plumaSkeleton!!.setX(obj.position.x)
            oWorld.game.assetsHandler!!.plumaSkeleton!!.setY(obj.position.y - .2f)
            oWorld.game.assetsHandler!!.plumaSkeleton!!.updateWorldTransform()
            oWorld.game.assetsHandler!!.plumaSkeleton!!.update(delta)
            skelrender.draw(batch, oWorld.game.assetsHandler!!.plumaSkeleton!!)
        }
    }

    private fun renderFuegos(delta: Float) {
        for (obj in oWorld.arrFogatas) {
            if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1f)) continue
            oWorld.game.assetsHandler!!.fogataAnim!!.apply(oWorld.game.assetsHandler!!.fogataSkeleton, obj.lastStateTime, obj.stateTime, true, null)
            oWorld.game.assetsHandler!!.fogataSkeleton!!.setX(obj.position.x)
            oWorld.game.assetsHandler!!.fogataSkeleton!!.setY(obj.position.y)
            oWorld.game.assetsHandler!!.fogataSkeleton!!.updateWorldTransform()
            oWorld.game.assetsHandler!!.fogataSkeleton!!.update(delta)
            skelrender.draw(batch, oWorld.game.assetsHandler!!.fogataSkeleton!!)
        }
    }
}
