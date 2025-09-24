package com.bitfire.uracer.game.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.MathUtils
import com.bitfire.uracer.game.GameEvents
import com.bitfire.uracer.game.actors.Car
import com.bitfire.uracer.game.debug.DebugHelper.RenderFlags
import com.bitfire.uracer.game.logic.helpers.GameTrack
import com.bitfire.uracer.game.logic.helpers.GameTrack.TrackSector
import com.bitfire.uracer.game.player.PlayerCar

class GameTrackDebugRenderer(flag: RenderFlags, private val gameTrack: GameTrack) : DebugRenderable(flag) {

    private val shape = ShapeRenderer()
    private val sectors = gameTrack.sectors
    private val route = gameTrack.route
    private var car: Car? = null
    private var hasCar = false

    override fun dispose() {
        shape.dispose()
    }

    override fun tick() {}

    override fun player(player: PlayerCar?) {
        car = player
        hasCar = (car != null)
    }

    private fun drawSector(sector: TrackSector) {
        val p = sector.poly
        val vertices = p.transformedVertices
        shape.line(vertices[0], vertices[1], vertices[2], vertices[3])
        shape.line(vertices[2], vertices[3], vertices[4], vertices[5])
        shape.line(vertices[4], vertices[5], vertices[6], vertices[7])
        shape.line(vertices[6], vertices[7], vertices[0], vertices[1])
    }

    override fun render() {
        val alpha = 0.25F
        val carAlpha = alpha * 3
        var sectorCenterFactor = 0F
        var carSector = -1

        if (hasCar) {
            carSector = car!!.trackState.curr

            if (carSector > -1) {
                var d = MathUtils.clamp(gameTrack.distanceInSector(carSector, car!!.getWorldPosMt()), 0F, 1F)

                if (d < 0.5F) {
                    sectorCenterFactor = d / 0.5F
                } else {
                    d -= 0.5F
                    d *= 2F
                    sectorCenterFactor = MathUtils.clamp(1 - d, 0F, 1F)
                }
            }
        }

        // precompute alpha from center factor
        val scol = 1 - sectorCenterFactor
        val sa = alpha + (carAlpha - alpha) * sectorCenterFactor

        Gdx.gl.glDisable(GL20.GL_CULL_FACE)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        shape.projectionMatrix = GameEvents.gameRenderer.mtxOrthographicMvpMt

        // draw waypoint segments
        shape.begin(ShapeType.Line)
        for (i in sectors.indices) {
            val s = sectors[i]

            if (carSector == i)
                shape.setColor(1F, 1F, scol, sa)
            else
                shape.setColor(1F, 1F, 1F, alpha)

            shape.line(s.leading.x, s.leading.y, s.trailing.x, s.trailing.y)
        }
        shape.end()

        // draw dots
        shape.begin(ShapeType.Filled)
        for (p in route) {
            shape.setColor(1F, 1F, 1F, alpha)
            shape.circle(p.x, p.y, 0.5F, 100)
        }
        shape.end()

        // sectors
        shape.begin(ShapeType.Line)
        for (i in sectors.indices) {
            if (i == carSector) {
                continue
            }

            shape.setColor(1F, 1F, 1F, alpha)
            drawSector(sectors[i])
        }

        // car sector
        if (carSector > -1) {
            shape.setColor(1F, 1F, scol, sa)
            drawSector(sectors[carSector])
        }

        shape.end()

        // car sector's dots
        if (carSector > -1) {
            val s = sectors[carSector]

            shape.begin(ShapeType.Filled)

            shape.setColor(1F, 1F, scol, sa - alpha)
            shape.circle(s.leading.x, s.leading.y, 0.5F, 100)
            shape.circle(s.trailing.x, s.trailing.y, 0.5F, 100)

            shape.end()
        }

        Gdx.gl.glDisable(GL20.GL_BLEND)
    }
}
