package com.bitfire.uracer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Rectangle
import com.bitfire.uracer.URacer
import com.bitfire.uracer.game.screens.GameScreensFactory.ScreenType
import com.bitfire.uracer.screen.TransitionFactory.TransitionType
import com.bitfire.uracer.screen.TransitionFactory.getTransition

class ScreenManager(viewport: Rectangle, private val screenFactory: ScreenFactory) {

    private val transitionManager = TransitionManager(viewport, URacer.Game.isDesktop(), true, true)
    private var current: Screen? = null
    private var next = ScreenType.NoScreen
    private var quitPending = false
    private var doSetScreenImmediate = false
    private var justTransitioned = false
    private val gl = Gdx.gl20

    fun dispose() {
        current?.dispose()
        current = null

        transitionManager.dispose()
    }

    fun begin(): Boolean {
        if (quitPending) return false

        var switchedScreen = false
        if (transitionManager.isActive && transitionManager.isComplete) {
            current = transitionManager.getTransition().nextScreen()
            current?.enable() // When you are exiting the game, current will be null.
            next = ScreenType.NoScreen
            transitionManager.removeTransition()
            switchedScreen = true
        } else if (doSetScreenImmediate) {
            doSetScreenImmediate = false
            current = screenFactory.createScreen(next)
            switchedScreen = true
        }

        // switched to a null screen?
        if (switchedScreen && current == null) {
            quitPending = true
            Gdx.app.exit() // async exit
        }

        return true
    }

    fun end() {}

    /**
     * Switch to the screen identified by the specified screen type, using the specified transition type in its default
     * configuration. The screen change is scheduled to happen at the start of the next frame.
     */
    fun setScreen(screen: ScreenType, transitionType: TransitionType, transitionDurationMs: Long) {
        transitionManager.removeTransition()
        var transition: ScreenTransition? = null

        // if no transition or no duration avoid everything and pass a null reference
        if (transitionType != TransitionType.None && transitionDurationMs > 0) {
            transition = getTransition(transitionType)
            transition.setDuration(transitionDurationMs)
        }

        setScreen(screen, transition)
    }

    /**
     * Switch to the screen identified by the specified screen type, using the specified transition. The screen change is scheduled
     * to happen at the start of the next frame.
     */
    fun setScreen(screen: ScreenType, transition: ScreenTransition?) {
        transitionManager.removeTransition()
        doSetScreenImmediate = false
        next = screen

        // if no transition then just setup a screen switch
        if (transition != null) {
            current?.disable()
            transitionManager.start(current, screen, transition)
        } else
            doSetScreenImmediate = true

        // dispose the current screen
        if (current != null) {
            current!!.dispose()
            current = null
            System.gc()
        }
    }

    fun quit() = quitPending

    fun resize() {}

    fun tick() {
        if (transitionManager.isActive) return
        current?.tick()
    }

    fun tickCompleted() {
        if (transitionManager.isActive) return
        current?.tickCompleted()
    }

    fun render() {
        if (transitionManager.isActive) {
            transitionManager.update()
            transitionManager.render()
            justTransitioned = true
        } else {
            if (current != null) {
                if (justTransitioned) {
                    justTransitioned = false

                    // ensures default active texture is active
                    gl.glActiveTexture(GL20.GL_TEXTURE0)
                }

                current!!.render(null)
            }
        }
    }

    fun pause() {
        if (quitPending) return
        if (transitionManager.isActive) transitionManager.pause() else current?.pause()
    }

    fun resume() {
        if (quitPending) return
        if (transitionManager.isActive) transitionManager.resume() else current?.resume()
    }
}
