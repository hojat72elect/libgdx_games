package com.bitfire.uracer.game.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.bitfire.postprocessing.PostProcessor
import com.bitfire.uracer.Input
import com.bitfire.uracer.URacer
import com.bitfire.uracer.configuration.DebugUtils
import com.bitfire.uracer.game.GameEvents
import com.bitfire.uracer.game.GameLogic
import com.bitfire.uracer.game.events.GameRendererEvent
import com.bitfire.uracer.game.logic.gametasks.DisposableTasks
import com.bitfire.uracer.game.logic.gametasks.GameTask
import com.bitfire.uracer.game.logic.replaying.LapManager
import com.bitfire.uracer.game.logic.replaying.Replay
import com.bitfire.uracer.game.logic.replaying.ReplayManager
import com.bitfire.uracer.game.player.PlayerCar
import com.bitfire.uracer.game.rendering.GameRenderer
import com.bitfire.uracer.game.rendering.GameWorldRenderer
import com.bitfire.uracer.game.world.GameWorld
import com.bitfire.uracer.resources.Art
import com.bitfire.uracer.utils.AlgebraMath.fixup
import com.bitfire.uracer.utils.AlgebraMath.fixupTo
import com.bitfire.uracer.utils.ConvertUtils.mt2px
import com.bitfire.uracer.utils.ReplayUtils.ticksToSeconds
import com.bitfire.uracer.utils.ScaleUtils
import com.bitfire.uracer.utils.SpriteBatchUtils.drawString
import com.bitfire.utils.ItemsManager
import java.util.EnumSet

class DebugHelper(private val gameWorld: GameWorld, private val postProcessor: PostProcessor?, private val lapManager: LapManager, private val logic: GameLogic, private val input: Input) : GameTask(), DisposableTasks {
    private val renderables = ItemsManager<DebugRenderable>()
    private val idt = Matrix4()
    private val renderFlags: MutableSet<RenderFlags> = EnumSet.of<RenderFlags>(
        RenderFlags.VersionInfo,
        RenderFlags.FpsStats,
        RenderFlags.MeshStats,
        RenderFlags.PostProcessorInfo,
        RenderFlags.PerformanceGraph,
        RenderFlags.PlayerCarInfo,
        RenderFlags.MusicVolumes,
        RenderFlags.CarEngineVolumes,
        RenderFlags.Rankings,
        RenderFlags.Completion
    )
    private val stats: DebugStatistics
    private val uRacerInfo: String
    private val box2dWorld: World?
    private val ranks = Array<RankInfo>()
    private val b2drenderer: Box2DDebugRenderer
    private val dbg = ImmediateModeRenderer20(false, true, 0)
    private var enabled: Boolean
    private val renderListener: GameRendererEvent.Listener = object : GameRendererEvent.Listener {
        override fun handle(source: Any, type: GameRendererEvent.Type, order: GameRendererEvent.Order) {
            if (!isEnabled()) return

            val batch = GameEvents.gameRenderer.batch

            if (type == GameRendererEvent.Type.BatchDebug) {
                for (r in renderables) {
                    if (renderFlags.contains(r.flag)) r.renderBatch(batch!!)
                }

                // save original transform matrix
                val xform = batch!!.transformMatrix
                batch.transformMatrix = idt

                // render static debug information unscaled
                render(batch)

                // restore original matrix
                batch.transformMatrix = xform
            } else if (type == GameRendererEvent.Type.Debug) {
                if (renderFlags.contains(RenderFlags.BoundingBoxes3D)) {
                    renderBoundingBoxes(GameEvents.gameRenderer.camPersp!!)
                }

                if (renderFlags.contains(RenderFlags.Box2DWireframe)) {
                    b2drenderer.render(box2dWorld, GameEvents.gameRenderer.mtxOrthographicMvpMt)
                }

                for (r in renderables) {
                    if (renderFlags.contains(r.flag)) r.render()
                }
            }
        }
    }

    init {
        this.enabled = DebugUtils.UseDebugHelper
        this.box2dWorld = gameWorld.box2DWorld

        GameEvents.gameRenderer.addListener(renderListener, GameRendererEvent.Type.BatchDebug, GameRendererEvent.Order.PLUS_4)
        GameEvents.gameRenderer.addListener(renderListener, GameRendererEvent.Type.Debug, GameRendererEvent.Order.PLUS_4)

        b2drenderer = Box2DDebugRenderer()

        // extrapolate version information
        uRacerInfo = "URacer " + URacer.versionInfo

        // compute graphics stats size
        var updateHz = 60f
        if (!URacer.Game.isDesktop()) {
            updateHz = 5f
        }

        val sw = MathUtils.clamp(uRacerInfo.length * Art.DebugFontWidth, 100, 500)
        stats = DebugStatistics(sw, 100, updateHz)

        for (i in 0..<ReplayManager.MaxReplays + 1) {
            ranks.add(RankInfo())
        }
    }

    /**
     * Adds a DebugRenderable
     */
    fun add(renderable: DebugRenderable) {
        renderables.add(renderable)
    }

    /**
     * Removes a DebugRenderable
     */
    fun remove(renderable: DebugRenderable) {
        renderables.remove(renderable)
    }

    fun toggleFlag(flag: RenderFlags) {
        if (renderFlags.contains(flag)) {
            renderFlags.remove(flag)
        } else {
            renderFlags.add(flag)
        }
    }

    fun isEnabled(): Boolean {
        return enabled && !renderFlags.isEmpty()
    }

    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    override fun dispose() {
        super.dispose()

        GameEvents.gameRenderer.removeListener(renderListener, GameRendererEvent.Type.BatchDebug, GameRendererEvent.Order.PLUS_4)
        GameEvents.gameRenderer.removeListener(renderListener, GameRendererEvent.Type.Debug, GameRendererEvent.Order.PLUS_4)

        dbg.dispose()
        b2drenderer.dispose()
        stats.dispose()
        disposeTasks()
    }

    private fun handleInput() {
        if (input.isPressed(com.badlogic.gdx.Input.Keys.W)) {
            toggleFlag(RenderFlags.Box2DWireframe)
        } else if (input.isPressed(com.badlogic.gdx.Input.Keys.B)) {
            toggleFlag(RenderFlags.BoundingBoxes3D)
        } else if (input.isPressed(com.badlogic.gdx.Input.Keys.S)) {
            toggleFlag(RenderFlags.TrackSectors)
        }
    }

    override fun onTick() {
        if (isEnabled()) {
            handleInput()
        }
    }

    override fun onTickCompleted() {
        stats.update()
        for (r in renderables) {
            r.tick()
        }
    }

    override fun onGameReset() {
        for (r in renderables) {
            r.reset()
        }
    }

    override fun onPlayer(player: PlayerCar?) {
        super.onPlayer(player)

        for (r in renderables) {
            r.player(player)
        }
    }

    override fun disposeTasks() {
        renderables.dispose()
    }

    private fun render(batch: SpriteBatch) {
        if (renderFlags.contains(RenderFlags.VersionInfo)) renderVersionInfo(batch)

        if (renderFlags.contains(RenderFlags.FpsStats)) renderFpsStats(batch, ScaleUtils.PlayHeight - Art.DebugFontHeight)

        if (renderFlags.contains(RenderFlags.PerformanceGraph)) renderGraphicalStats(batch)

        if (renderFlags.contains(RenderFlags.MemoryStats)) renderMemoryUsage(batch, ScaleUtils.PlayHeight - Art.DebugFontHeight * 4)

        if (renderFlags.contains(RenderFlags.PlayerInfo)) renderPlayerInfo(batch)

        if (renderFlags.contains(RenderFlags.PostProcessorInfo)) renderPostProcessorInfo(batch, ScaleUtils.PlayHeight - Art.DebugFontHeight)

        if (renderFlags.contains(RenderFlags.Rankings)) renderRankings(batch, if (renderFlags.contains(RenderFlags.PlayerInfo)) Art.DebugFontHeight * 10 else 0)

        if (renderFlags.contains(RenderFlags.Completion)) renderCompletion(batch, if (renderFlags.contains(RenderFlags.PlayerInfo)) Art.DebugFontHeight * 10 else 0)

        if (renderFlags.contains(RenderFlags.MeshStats)) {
            drawString(
                batch, "total meshes=" + GameWorld.TotalMeshes, 0f, (ScaleUtils.PlayHeight
                        - Art.DebugFontHeight * 3).toFloat()
            )
            drawString(
                batch, ("rendered meshes="
                        + (GameWorldRenderer.renderedTrees + GameWorldRenderer.renderedWalls) + ", trees=" + GameWorldRenderer.renderedTrees
                        + ", walls=" + GameWorldRenderer.renderedWalls + ", culled=" + GameWorldRenderer.culledMeshes), 0f,
                (ScaleUtils.PlayHeight - Art.DebugFontHeight * 2).toFloat()
            )
        }
    }

    private fun batchColorStart(batch: SpriteBatch, r: Float, g: Float, b: Float) {
        batch.end()
        batch.flush()
        batch.setColor(r, g, b, 1F)
        batch.begin()
    }

    private fun batchColorEnd(batch: SpriteBatch) {
        batch.end()
        batch.flush()
        batch.setColor(1F, 1F, 1F, 1F)
        batch.begin()
    }

    private fun timeString(seconds: Float) = String.format("%02.03f", seconds)

    private fun rankString(rank: Int) = (if (rank <= 9) "0" else "") + rank

    private fun isNextTarget(replay: Replay?): Boolean {
        val target = logic.getNextTarget()
        if (target != null)
            return replay != null && target.replay.id == replay.id

        return false
    }

    private fun drawString2X(batch: SpriteBatch, text: String, x: Float, y: Float) {
        drawString(batch, text, x, y, Art.DebugFontWidth.toFloat(), Art.DebugFontHeight.toFloat())
    }

    private fun renderRankings(batch: SpriteBatch, y: Int) {
        val scale = 1f
        val last = logic.lastRecordedInfo
        val discarded = !last.is_accepted

        val xoffset = 5f
        var coord = y
        drawString2X(batch, "CURRENT RANKINGS", xoffset, coord.toFloat())
        drawString2X(batch, "================", xoffset, coord + Art.DebugFontHeight * scale)
        coord += (2 * Art.DebugFontHeight * scale).toInt()

        var text: String?
        var rank = 1
        for (replay in lapManager.replays) {
            var lastAccepted = false

            if (last.is_accepted)
                lastAccepted = last.accepted.id == replay.id


            if (lastAccepted) {
                if (last.position == 1) batchColorStart(batch, 0f, 1f, 0f)
                else batchColorStart(batch, 1f, 1f, 0f)
            } else {
                if (isNextTarget(replay))
                    batchColorStart(batch, 0.5f, 0.9f, 1f)
            }

            text = rankString(rank) + (if (isNextTarget(replay)) "<" else " ") + " " + replay.userId + " " + replay.secondsStr
            drawString2X(batch, text, xoffset, coord.toFloat())
            batchColorEnd(batch)
            coord += (Art.DebugFontHeight * scale).toInt()
            rank++
        }

        // show discarded lap
        if (discarded && last.discarded.isValid) {
            val info = last.discarded
            batchColorStart(batch, 1f, 0f, 0f)

            text = rankString(lapManager.replays.size + 1) + "  " + info.getUserId() + " " + info.secondsStr
            drawString2X(batch, text, xoffset, coord.toFloat())
            batchColorEnd(batch)
        }
    }

    private fun renderCompletion(batch: SpriteBatch, y: Int) {
        val scale = 1F
        val xoffset = 130 * scale
        var coord = y
        for (i in 0..<ReplayManager.MaxReplays + 1)
            ranks.get(i).reset()


        var playerIndex = 0
        for (i in 0..<ReplayManager.MaxReplays) {
            val ghost = logic.getGhost(i)
            val ts = ghost.trackState
            if (ghost.hasReplay()) {
                val rank = ranks.get(i)
                rank.valid = true
                rank.uid = ghost.replay.userId
                rank.ticks = ghost.replay.ticks
                rank.isNextTarget = isNextTarget(ghost.replay)
                rank.player = false

                rank.completion = 0f
                if (ts.ghostArrived)
                    rank.completion = 1f
                else if (ghost.isPlaying)
                    rank.completion = gameWorld.gameTrack.getTrackCompletion(ghost)


                playerIndex++
            }
        }

        if (hasPlayer) {
            val rank = ranks.get(playerIndex)
            rank.valid = true
            rank.uid = logic.userProfile.userId
            rank.player = true
            rank.isNextTarget = false

            // getTrackTimeInt is computed as an int cast (int)(trackTimeSeconds * AMath.ONE_ON_CMP_EPSILON)
            rank.ticks = lapManager.currentReplayTicks
            rank.completion = if (logic.isWarmUp) 0f else gameWorld.gameTrack.getTrackCompletion(player)
        }

        ranks.sort()

        drawString2X(batch, "CURRENT COMPLETION", xoffset, coord.toFloat())
        drawString2X(batch, "==================", xoffset, coord + Art.DebugFontHeight * scale)
        coord += (2 * Art.DebugFontHeight * scale).toInt()

        var text: String?
        for (i in 0..<ranks.size) {
            val r = ranks.get(i)
            if (r.valid) {
                val c = fixupTo(fixup(r.completion), 1f)
                if (r.player) {
                    batchColorStart(batch, 0f, 0.6f, 1f)
                } else {
                    if (r.isNextTarget) {
                        batchColorStart(batch, 0.5f, 0.9f, 1f)
                    }
                }

                text = (rankString(i + 1) + (if (r.isNextTarget) "<" else " ") + " " + r.uid + " " + timeString(c) + " "
                        + String.format("%.03f", ticksToSeconds(r.ticks)))
                drawString2X(batch, text, xoffset, coord.toFloat())

                batchColorEnd(batch)

                coord += (Art.DebugFontHeight * scale).toInt()
            }
        }
    }

    private fun renderGraphicalStats(batch: SpriteBatch) {
        batch.enableBlending()
        batch.setColor(1f, 1f, 1f, 0.8f)
        batch.draw(stats.region, (ScaleUtils.PlayWidth - stats.width).toFloat(), 12f)
        batch.setColor(1f, 1f, 1f, 1f)
        batch.disableBlending()
    }

    private fun renderFpsStats(batch: SpriteBatch, y: Int) {
        val fps = String.format("%d", Gdx.graphics.framesPerSecond)
        val phy = String.format("%.04f", stats.meanPhysics.getMean())
        val gfx = String.format("%.04f", stats.meanRender.getMean())
        val ticks = String.format("%.02f", stats.meanTickCount.getMean())
        val text = "fps: $fps, phy: $phy, gfx: $gfx, ticks: $ticks"
        drawString(batch, text, (ScaleUtils.PlayWidth - text.length * Art.DebugFontWidth).toFloat(), y.toFloat())
    }

    private fun renderVersionInfo(batch: SpriteBatch) {
        drawString(
            batch, uRacerInfo, (ScaleUtils.PlayWidth - uRacerInfo.length * Art.DebugFontWidth).toFloat(), 0f,
            Art.DebugFontWidth.toFloat(), 12f
        )
    }

    private fun renderMemoryUsage(batch: SpriteBatch, y: Int) {
        val oneOnMb = 1f / 1048576f
        val javaHeapMb = Gdx.app.javaHeap * oneOnMb
        val nativeHeapMb = Gdx.app.nativeHeap.toFloat() * oneOnMb

        val text = ("java heap = " + String.format("%.02f", javaHeapMb) + "MB" + " - native heap = "
                + String.format("%.02f", nativeHeapMb) + "MB")

        drawString(batch, text, 0f, y.toFloat())
    }

    private fun renderPostProcessorInfo(batch: SpriteBatch, y: Int) {

        val text = if (postProcessor == null)
            "No post-processor is active"
        else
            "Post-processing fx count = " + postProcessor.getEnabledEffectsCount()

        drawString(batch, text, 0f, y.toFloat())
    }

    private fun renderPlayerInfo(batch: SpriteBatch) {
        if (!hasPlayer) return

        val carDesc = player.carDescriptor
        val body = player.getBody()
        val pos = GameRenderer.ScreenUtils.worldMtToScreen(body.position)
        val state = player.state()

        drawString(batch, "vel_wc len =" + carDesc.velocityWorldCoordinates.len(), 0f, 0f)
        drawString(batch, "vel_wc [x=" + carDesc.velocityWorldCoordinates.x + ", y=" + carDesc.velocityWorldCoordinates.y + "]", 0f, Art.DebugFontWidth.toFloat())
        drawString(batch, "steerangle=" + carDesc.steeringAngle, 0f, (Art.DebugFontWidth * 2).toFloat())
        drawString(batch, "throttle=" + carDesc.throttle, 0f, (Art.DebugFontWidth * 3).toFloat())
        drawString(batch, "screen x=" + pos.x + ",y=" + pos.y, 0f, (Art.DebugFontWidth * 4).toFloat())
        drawString(batch, "world-mt x=" + body.position.x + ",y=" + body.position.y, 0f, (Art.DebugFontWidth * 5).toFloat())
        drawString(
            batch,
            "world-px x=" + mt2px(body.position.x) + ",y=" + mt2px(body.position.y), 0f, (Art.DebugFontWidth * 6).toFloat()
        )
        drawString(batch, "orient=" + body.angle, 0f, Art.DebugFontWidth * 7F)
        drawString(batch, "render.interp=" + (state.position.x.toString() + "," + state.position.y), 0F, Art.DebugFontWidth * 8F)
    }

    private fun renderBoundingBoxes(camPersp: PerspectiveCamera) {
        val trees = gameWorld.trackTrees
        val walls = gameWorld.trackWalls

        for (i in trees.models.indices) {
            val m = trees.models[i]
            renderBoundingBox(camPersp, m.treeBoundingBox)
        }

        for (i in 0..<walls.count()) {
            val m = walls.models[i]
            renderBoundingBox(camPersp, m.boundingBox)
        }
    }

    /**
     * This is intentionally SLOW. Read it again!
     */
    private fun renderBoundingBox(camPersp: PerspectiveCamera, boundingBox: BoundingBox) {
        val alpha = .15F
        val r = 0F
        val g = 0F
        val b = 1F
        val offset = 0.5F // offset for the base, due to pixel-perfect model placement

        val corners = boundingBox.corners

        Gdx.gl.glDisable(GL20.GL_CULL_FACE)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        dbg.begin(camPersp.combined, GL20.GL_TRIANGLES)

        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[0]!!.x, corners[0]!!.y, corners[0]!!.z + offset)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[1]!!.x, corners[1]!!.y, corners[1]!!.z + offset)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[4]!!.x, corners[4]!!.y, corners[4]!!.z)

        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[1]!!.x, corners[1]!!.y, corners[1]!!.z + offset)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[4]!!.x, corners[4]!!.y, corners[4]!!.z)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[5]!!.x, corners[5]!!.y, corners[5]!!.z)

        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[1]!!.x, corners[1]!!.y, corners[1]!!.z + offset)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[2]!!.x, corners[2]!!.y, corners[2]!!.z + offset)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[5]!!.x, corners[5]!!.y, corners[5]!!.z)

        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[2]!!.x, corners[2]!!.y, corners[2]!!.z + offset)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[5]!!.x, corners[5]!!.y, corners[5]!!.z)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[6]!!.x, corners[6]!!.y, corners[6]!!.z)

        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[2]!!.x, corners[2]!!.y, corners[2]!!.z + offset)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[6]!!.x, corners[6]!!.y, corners[6]!!.z)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[3]!!.x, corners[3]!!.y, corners[3]!!.z + offset)

        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[3]!!.x, corners[3]!!.y, corners[3]!!.z)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[6]!!.x, corners[6]!!.y, corners[6]!!.z)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[7]!!.x, corners[7]!!.y, corners[7]!!.z)

        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[3]!!.x, corners[3]!!.y, corners[3]!!.z + offset)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[0]!!.x, corners[0]!!.y, corners[0]!!.z + offset)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[7]!!.x, corners[7]!!.y, corners[7]!!.z)

        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[7]!!.x, corners[7]!!.y, corners[7]!!.z)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[0]!!.x, corners[0]!!.y, corners[0]!!.z + offset)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[4]!!.x, corners[4]!!.y, corners[4]!!.z)

        // top cap
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[4]!!.x, corners[4]!!.y, corners[4]!!.z)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[5]!!.x, corners[5]!!.y, corners[5]!!.z)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[7]!!.x, corners[7]!!.y, corners[7]!!.z)

        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[5]!!.x, corners[5]!!.y, corners[5]!!.z)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[7]!!.x, corners[7]!!.y, corners[7]!!.z)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[6]!!.x, corners[6]!!.y, corners[6]!!.z)

        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[0]!!.x, corners[0]!!.y, corners[0]!!.z + offset)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[3]!!.x, corners[3]!!.y, corners[3]!!.z + offset)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[1]!!.x, corners[1]!!.y, corners[1]!!.z + offset)

        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[3]!!.x, corners[3]!!.y, corners[3]!!.z + offset)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[1]!!.x, corners[1]!!.y, corners[1]!!.z + offset)
        dbg.color(r, g, b, alpha)
        dbg.vertex(corners[2]!!.x, corners[2]!!.y, corners[2]!!.z + offset)

        dbg.end()

        Gdx.gl.glDisable(GL20.GL_BLEND)
    }

    // render flags for basic debug info
    enum class RenderFlags {
        VersionInfo,
        FpsStats,
        MemoryStats,
        MeshStats,
        PlayerInfo,
        PlayerCarInfo,
        PostProcessorInfo,
        PerformanceGraph,
        Box2DWireframe,
        BoundingBoxes3D,
        TrackSectors,
        MusicVolumes,
        CarEngineVolumes,
        Rankings,
        Completion
    }
}
