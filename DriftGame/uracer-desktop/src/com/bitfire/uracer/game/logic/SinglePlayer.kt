package com.bitfire.uracer.game.logic

import aurelienribon.tweenengine.equations.Quad.Companion.OUT
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.bitfire.postprocessing.PostProcessor
import com.bitfire.uracer.URacer
import com.bitfire.uracer.configuration.DebugUtils
import com.bitfire.uracer.configuration.GraphicsUtils
import com.bitfire.uracer.configuration.Storage
import com.bitfire.uracer.configuration.UserProfile
import com.bitfire.uracer.game.GameplaySettings
import com.bitfire.uracer.game.actors.GhostCar
import com.bitfire.uracer.game.debug.DebugCarEngineVolumes
import com.bitfire.uracer.game.debug.DebugHelper
import com.bitfire.uracer.game.debug.DebugHelper.RenderFlags
import com.bitfire.uracer.game.debug.DebugMusicVolumes
import com.bitfire.uracer.game.debug.GameTrackDebugRenderer
import com.bitfire.uracer.game.debug.player.DebugPlayer
import com.bitfire.uracer.game.logic.gametasks.messager.Message
import com.bitfire.uracer.game.logic.gametasks.sounds.effects.PlayerEngineSoundEffect
import com.bitfire.uracer.game.logic.gametasks.sounds.effects.PlayerTensiveMusic
import com.bitfire.uracer.game.logic.replaying.Replay
import com.bitfire.uracer.game.logic.replaying.ReplayManager.DiscardReason
import com.bitfire.uracer.game.logic.replaying.ReplayManager.ReplayResult
import com.bitfire.uracer.game.logic.types.helpers.CameraShaker
import com.bitfire.uracer.game.rendering.GameRenderer
import com.bitfire.uracer.game.screens.GameScreensFactory.ScreenType
import com.bitfire.uracer.game.world.GameWorld
import com.bitfire.uracer.utils.ConvertUtils.mt2px
import com.bitfire.uracer.utils.OrdinalUtils.getOrdinalFor
import com.bitfire.uracer.utils.ReplayUtils.getFullPath
import com.bitfire.uracer.utils.ReplayUtils.pruneReplay
import com.bitfire.uracer.utils.URacerRuntimeException
import com.bitfire.uracer.utils.dumpSpeedInfo

class SinglePlayer(userProfile: UserProfile, gameWorld: GameWorld, gameRenderer: GameRenderer) : BaseLogic(userProfile, gameWorld, gameRenderer) {

    private val camShaker = CameraShaker()
    private val lastRecorded = ReplayResult()
    private val canDisableTargetMode = false
    private var debug: DebugHelper? = null
    private var saving = false
    private var selectedBestReplayIdx = -1
    private var targetMode = !canDisableTargetMode // always enabled if can't be disabled, else no target by default

    init {
        setupDebug(gameRenderer.postProcessing.postProcessor)
    }

    override fun dispose() {
        destroyDebug()
        super.dispose()
    }

    private fun setupDebug(postProcessor: PostProcessor?) {
        if (DebugUtils.UseDebugHelper) {
            debug = DebugHelper(gameWorld, postProcessor, lapManager, this, inputSystem)
            debug!!.add(GameTrackDebugRenderer(RenderFlags.TrackSectors, gameWorld.gameTrack))
            debug!!.add(DebugPlayer(RenderFlags.PlayerCarInfo, gameTasksManager))
            debug!!.add(DebugMusicVolumes(RenderFlags.MusicVolumes, gameTasksManager.sound.get(PlayerTensiveMusic::class.java) as PlayerTensiveMusic))

            val se = (gameTasksManager.sound.get(PlayerEngineSoundEffect::class.java) as PlayerEngineSoundEffect)
            debug!!.add(DebugCarEngineVolumes(RenderFlags.CarEngineVolumes, se.soundSet))
            Gdx.app.debug("Game", "Debug helper initialized")
        }
    }

    private fun destroyDebug() {
        if (DebugUtils.UseDebugHelper) {
            debug!!.dispose()
        }
    }

    private fun findGhostFor(replay: Replay?): GhostCar? {
        for (ghost in ghostCars) {
            if (ghost != null && replay != null && ghost.replay.id == replay.id)
                return ghost
        }

        return null
    }

    private fun hasGhostFor(replay: Replay?): Boolean {
        return (findGhostFor(replay) != null)
    }

    override fun handleExtraInput() {
        val i = inputSystem

        if (i.isPressed(com.badlogic.gdx.Input.Keys.O)) {
            removePlayer()
            restartGame()
            restartAllReplays()
        } else if (i.isPressed(com.badlogic.gdx.Input.Keys.P)) {
            addPlayer()
            restartGame()
        } else if (i.isPressed(com.badlogic.gdx.Input.Keys.D)) {
            val newstate = !gameRenderer.isDebugEnabled
            gameRenderer.setDebug(newstate)
            if (debug != null) {
                debug!!.setEnabled(newstate)
            }
        }
    }

    override val lastRecordedInfo = lastRecorded

    override  fun getNextTarget() :GhostCar? {
        val maxreplays = lapManager.replays.size
        if (maxreplays > 0 && selectedBestReplayIdx >= 0 && selectedBestReplayIdx < maxreplays) {
            return findGhostFor(lapManager.replays.get(selectedBestReplayIdx))
        }

        return null
    }

    private fun setGhostAlphasFor(isTargetMode: Boolean) {
        for (ghostCar in ghostCars) {
            ghostCar.tweenAlphaTo(
                if (isTargetMode)
                    GraphicsUtils.DefaultGhostCarOpacity
                else
                    GraphicsUtils.DefaultTargetCarOpacity, GraphicsUtils.DefaultGhostOpacityChangeMs * 1.5f, OUT
            )
        }
    }

    override fun chooseNextTarget(backward: Boolean) {
        val prevTarget = getNextTarget()
        val maxreplays = lapManager.replaysCount

        if (backward) {
            selectedBestReplayIdx--
        } else {
            selectedBestReplayIdx++
        }

        val limit = if (canDisableTargetMode) -1 else 0
        if (selectedBestReplayIdx < limit) selectedBestReplayIdx = maxreplays - 1
        if (selectedBestReplayIdx >= maxreplays) selectedBestReplayIdx = limit

        // target mode disabled
        if (selectedBestReplayIdx == -1) {
            if (canDisableTargetMode && targetMode) {
                targetMode = false
                gameWorldRenderer.setTopMostGhostCar(null)
                playerTasks.hudPlayer.unHighlightNextTarget()
                setGhostAlphasFor(targetMode)
                messager.show("Target mode disabled", 2f, Message.Type.Information, Message.Position.Top, Message.Size.Big)
                Gdx.app.log("SinglePlayer", "Target mode disabled")
            }
        } else {
            if (canDisableTargetMode && !targetMode) {
                targetMode = true
                setGhostAlphasFor(true)
                messager.show("Target mode enabled", 3f, Message.Type.Information, Message.Position.Top, Message.Size.Big)
                Gdx.app.log("SinglePlayer", "Target mode enabled")
            }

            if (!isWarmUp) {
                val next = getNextTarget()
                if (prevTarget != next && next != null && next.isPlaying) {
                    playerTasks.hudPlayer.highlightNextTarget(next)
                    gameWorldRenderer.setTopMostGhostCar(next)
                }
            }
        }

        Gdx.app.log("SinglePlayer", "Next target set to #$selectedBestReplayIdx - $targetMode")
    }

    private fun saveReplay(replay: Replay?) {
        if (saving) {
            Gdx.app.log("SinglePlayer", "(already saving, request ignored...")
            return
        }

        if (replay != null) {
            saving = true
            val savingThread = Thread {
                if (replay.save()) {
                    Gdx.app.log(
                        "SinglePlayer",
                        "Replay #" + replay.shortId + " saved to \"" + getFullPath(replay.getInfo()) + "\""
                    )
                }
            }

            savingThread.start()
            saving = false
        }
    }

    private val outOfTrackFactor: Float
        get() {
            val oot = MathUtils.clamp(outOfTrackTimer.elapsed().absSeconds, 0f, 0.5f) * 2
            val s = MathUtils.clamp(playerCar.carState.currSpeedFactor * 100f, 0f, 1f)
            return 0.075f * oot * s
        }

    override fun updateCameraPosition(positionPx: Vector2) {
        if (hasPlayer()) {
            // update player's headlights and move the world camera to follows it, if there is a player
            if (gameWorld.isNightMode) {
                gameWorldRenderer.updatePlayerHeadlights(playerCar)
            }
            positionPx.set(playerCar.state().position)
            if (!paused) {
                positionPx.add(camShaker.compute(collisionFactor))
                positionPx.add(camShaker.compute(this.outOfTrackFactor))
            }
        } else if (isGhostActive(0)) {
            positionPx.set(getGhost(0).state().position)
        } else {
            positionPx.set(mt2px(gameWorld.playerStart.position))
        }
    }

    /**
     * Load from disk all the replays for the specified trackId, pruning while loading respecting the ReplayManager.MaxReplays
     * constant. Any previous Replay will be cleared from the lapManager instance.
     */
    private fun loadReplaysFromDiskFor() {
        lapManager.removeAllReplays()

        var reloaded = 0
        for (userdir in Gdx.files.external(Storage.REPLAYS_ROOT + gameWorld.levelId).list()) {
            if (userdir.isDirectory) {
                for (userreplay in userdir.list()) {
                    val replay = Replay.load(userreplay.path())
                    if (replay != null) {
                        // add replays even if slower
                        val ri = lapManager.addReplay(replay)
                        if (ri.is_accepted) {
                            pruneReplay(ri.pruned) // prune if needed
                            reloaded++
                            Gdx.app.log("SinglePlayer", "Loaded replay #" + ri.accepted.shortId)
                        } else {
                            var msg = ""
                            when (ri.reason) {
                                DiscardReason.Null -> msg = "null replay (" + userreplay.path() + ")"
                                DiscardReason.InvalidMinDuration -> msg = ("invalid lap (" + ri.discarded.secondsStr + "s < " + GameplaySettings.REPLAY_MIN_DURATION_SECONDS
                                        + "s) (#" + ri.discarded.shortId + ")")

                                DiscardReason.Invalid -> msg = "the specified replay is not valid. (" + userreplay.path() + ")"
                                DiscardReason.WrongTrack -> msg = "the specified replay belongs to another game track (#" + ri.discarded.shortId + ")"
                                DiscardReason.Slower -> {
                                    msg = "too slow! (#" + ri.discarded.shortId + ")"
                                    pruneReplay(ri.discarded)
                                }

                                DiscardReason.Accepted -> {}
                            }

                            Gdx.app.log("SinglePlayer", "Discarded at loading time, $msg")
                        }
                    }
                }
            }
        }

        Gdx.app.log("SinglePlayer", "Building opponents list:")

        rebindAllReplays()

        var pos = 1
        for (r in lapManager.replays) {
            Gdx.app.log(
                "SinglePlayer",
                "#" + pos + ", #" + r.shortId + ", secs=" + r.secondsStr + ", ct=" + r.creationTimestamp
            )
            pos++
        }

        Gdx.app.log("SinglePlayer", "Reloaded $reloaded opponents.")
    }

    override fun restartGame() {
        super.restartGame()
        Gdx.app.log("SinglePlayer", "Starting/restarting game")
        loadReplaysFromDiskFor()
    }

    override fun resetGame() {
        super.resetGame()
        Gdx.app.log("SinglePlayer", "Resetting game")
        loadReplaysFromDiskFor()
    }

    override fun warmUpStarted() {
        messager.show("Warm up!", 1.5f, Message.Type.Information, Message.Position.Top, Message.Size.Big)
    }

    override fun warmUpCompleted() {
        messager.show("GOOOO!!", 1.5f, Message.Type.Information, Message.Position.Top, Message.Size.Big)
    }

    override fun playerLapStarted() {
        playerCar.resetDistanceAndSpeed(true, false)
        lapManager.startRecording(playerCar, gameWorld.levelId, userProfile.userId)
        progressData.reset(true)
        setAccuDriftSeconds()

        rebindAllReplays()
        restartAllReplays()
    }

    override fun playerLapCompleted() {
        if (lapManager.isRecording) {
            lastRecorded.reset()

            val replay = lapManager.stopRecording()

            if (canDisableTargetMode && targetMode) {
                // only replays that are better than the current target are permitted
                val target = getNextTarget()
                val slowerThanTarget = (target != null) && (replay.compareTo(target.replay) > -1)
                if (slowerThanTarget) {
                    val treplay = target.replay
                    val ri = replay.getInfo()

                    // early discard, slower than target
                    lastRecorded.is_accepted = false
                    lastRecorded.discarded.copy(ri)
                    lastRecorded.reason = DiscardReason.Slower

                    val diff = String.format("%.03f", (ri.milliseconds - treplay.milliseconds).toFloat() / 1000f)
                    Gdx.app.log("ReplayManager", "Discarded replay #" + ri.shortId + " for " + diff + "secs")
                } else {
                    // once added lastRecorded.new_replay should be used
                    lastRecorded.copy(lapManager.addReplay(replay))
                }
            } else {
                lastRecorded.copy(lapManager.addReplay(replay))
            }

            if (lastRecorded.is_accepted) {
                val ri = lastRecorded.accepted

                dumpSpeedInfo("SinglePlayer", "Replay #" + ri.shortId + " accepted, player", playerCar, ri.ticks)

                saveReplay(lastRecorded.new_replay)
                pruneReplay(lastRecorded.pruned) // prune if needed

                // show message
                val pos = lastRecorded.position
                messager.show(
                    pos.toString() + getOrdinalFor(pos) + " place!", 1.5f, Message.Type.Information, Message.Position.Top,
                    Message.Size.Big
                )
            } else {
                val ri = lastRecorded.discarded

                var msg = ""
                val id = "(#" + ri.shortId + ")"
                var duration = 1.5f

                when (lastRecorded.reason) {
                    DiscardReason.Null -> {
                        msg = "Discarding null replay $id"
                        duration = 3f
                    }

                    DiscardReason.InvalidMinDuration -> {
                        msg = "Invalid lap (" + ri.secondsStr + "s < " + GameplaySettings.REPLAY_MIN_DURATION_SECONDS + "s) " + id
                        duration = 10f
                    }

                    DiscardReason.Invalid -> {
                        msg = "The specified replay is not valid. (#" + ri.shortId + ") " + id
                        duration = 10f
                    }

                    DiscardReason.WrongTrack -> {
                        msg = "The specified replay belongs to another game track $id"
                        duration = 10f
                    }

                    DiscardReason.Slower -> msg = "Too slow!"
                    DiscardReason.Accepted -> {}
                }

                Gdx.app.log("SinglePlayer", msg)
                messager.show(msg, duration, Message.Type.Information, Message.Position.Top, Message.Size.Big)
            }
        }

        playerCar.resetDistanceAndSpeed(true, false)
    }

    override fun ghostLapCompleted(ghost: GhostCar) {
        dumpSpeedInfo("SinglePlayer", "GhostCar #" + ghost.id, ghost, ghost.replay.ticks)

        if (!hasPlayer()) {
            val last = lapManager.replays.peek()
            val ghostReplay = ghost.replay

            if (last != null && last.id == ghostReplay.id) {
                restartAllReplays()
            }
        } else {
            ghost.stop()
        }
    }

    override fun ghostReplayStarted(ghost: GhostCar) {
        if (selectedBestReplayIdx > -1 && ghost == findGhostFor(lapManager.replays.get(selectedBestReplayIdx))) {
            playerTasks.hudPlayer.highlightNextTarget(ghost)
        }
    }

    override fun ghostReplayEnded(ghost: GhostCar) {
        if (ghost == getNextTarget()) {
            playerTasks.hudPlayer.unHighlightNextTarget()
        }
    }

    override fun doQuit() {
        lapManager.abortRecording(false)
        URacer.Game.show(ScreenType.MainScreen)
        timeModulator.reset()
        URacer.Game.resetTimeModFactor()
    }

    /**
     * Restart all replays in the lap manager, if no next target set the best replay's car to it
     */
    private fun restartAllReplays() {
        for (r in lapManager.replays) {
            if (hasGhostFor(r)) {
                val ghost = findGhostFor(r)
                ghost!!.alpha = if (targetMode) GraphicsUtils.DefaultGhostCarOpacity else GraphicsUtils.DefaultTargetCarOpacity
                if (targetMode && getNextTarget() == ghost) {
                    ghost.alpha = GraphicsUtils.DefaultTargetCarOpacity
                    gameWorldRenderer.setTopMostGhostCar(ghost)
                }

                ghost.stop()
                ghost.start()
            }
        }
    }

    private fun rebindAllReplays() {
        if (lapManager.replays.size > ghostCars.size) {
            throw URacerRuntimeException("Replays count mismatch")
        }

        var g = 0
        for (r in lapManager.replays) {
            val ghost = ghostCars[g] ?: throw URacerRuntimeException("Ghost not ready (#$g)")

            ghost.setReplay(r)

            ghostLapMonitor[g].reset()

            g++
        }

        // auto-select the best replay in case target mode is mandatory and there is no target yet
        if (!canDisableTargetMode && getNextTarget() == null) {
            Gdx.app.log("SinglePlayer", "Automatically selecting best replay...")
            if (lapManager.replaysCount > 0) {
                selectedBestReplayIdx = 0
                Gdx.app.log("SinglePlayer", "Done selecting best replay!")
            } else {
                selectedBestReplayIdx = -1
                Gdx.app.log("SinglePlayer", "Couldn't find any replay for this track.")
            }
        }
    }
}
