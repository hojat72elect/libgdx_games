package com.bitfire.uracer.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.bitfire.uracer.URacer
import com.bitfire.uracer.configuration.UserPreferences
import com.bitfire.uracer.configuration.UserPreferences.Preference
import com.bitfire.uracer.game.Game
import com.bitfire.uracer.game.logic.gametasks.SoundManager
import com.bitfire.uracer.resources.Art
import com.bitfire.uracer.utils.Dialog
import com.bitfire.uracer.utils.UIUtils.newButton
import com.bitfire.uracer.utils.UIUtils.newFittedStage
import com.bitfire.uracer.utils.UIUtils.newLabel
import com.bitfire.uracer.utils.UIUtils.newSlider
import com.bitfire.uracer.utils.UIUtils.newTable
import com.bitfire.uracer.utils.UIUtils.newWindow
import com.bitfire.uracer.utils.Window

class GameScreenUI(private val game: Game) {

    private val input = URacer.Game.getInputSystem()
    private var ui: Stage? = null
    private var win: Window? = null
    private var dlgQuit: Dialog? = null
    private var quitShown = false
    private var enabled = false

    init {
        constructUI()
    }

    fun dispose() {
        disable()
        ui!!.dispose()
    }

    private fun constructUI() {
        ui = newFittedStage()

        win = newWindow("OPTIONS")
        ui!!.addActor(win)

        val content = newTable()
        val bottom = newTable()

        win!!.row().fill().expand()
        win!!.add(content)
        win!!.row().fill().expand()
        win!!.add(bottom)
        content.row().expandX()

        val btnQuit: Button = newButton("QUIT RACE (Q)", object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (enabled && !quitShown) {
                    showQuit()
                }
            }
        })

        val btnResume: Button = newButton("RESUME GAME (ESC)", object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (enabled) {
                    disable()
                    enabled = false
                }
            }
        })
        bottom.add(btnResume).padRight(5f)
        bottom.add(btnQuit)
        bottom.right()

        dlgQuit = object : Dialog("Confirm", Art.scrSkin, "dialog") {
            override fun result(listener: Any?) {
                quitShown = false
                if (listener as Boolean? == true) {
                    quit()
                }
            }
        }.text("Your current lap will not be saved, are you sure you want to quit?").button("YES (Y)", true)
            .button("NO (N)", false).key(com.badlogic.gdx.Input.Keys.Y, true).key(com.badlogic.gdx.Input.Keys.N, false)

        val musicLabel = newLabel("Music volume", false)
        val sfxLabel = newLabel("Sound effects volume", false)

        val sfxSlider = newSlider(0f, 1f, 0.01f, SoundManager.SfxVolumeMul, object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor?) {
                val source = event.listenerActor as Slider
                SoundManager.SfxVolumeMul = source.value
                UserPreferences.real(Preference.SfxVolume, SoundManager.SfxVolumeMul)
            }
        })
        content.add(sfxLabel).left()
        content.add(sfxSlider).right()

        val musicSlider = newSlider(0f, 1f, 0.01f, SoundManager.MusicVolumeMul, object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor?) {
                val source = event.listenerActor as Slider
                SoundManager.MusicVolumeMul = source.value
                UserPreferences.real(Preference.MusicVolume, SoundManager.MusicVolumeMul)
            }
        })
        content.row()
        content.add(musicLabel).left()
        content.add(musicSlider).right()
    }

    private fun setup() {
        Dialog.fadeDuration = 0.4f
        quitShown = false

        win!!.setWidth(480f)
        win!!.setHeight(280f)
        win!!.setPosition((Gdx.graphics.width - win!!.getWidth()) / 2, (Gdx.graphics.height - win!!.getHeight()) / 2)
    }

    private fun showQuit() {
        dlgQuit!!.getContentTable().padBottom(10f)
        dlgQuit!!.pack()
        dlgQuit!!.show(ui!!)
        quitShown = true
    }

    private fun hideQuit() {
        quitShown = false
        dlgQuit!!.hide()
    }

    private fun quit() {
        UserPreferences.save()
        game.quit()
        game.tick()
    }

    private fun enable() {
        game.pause()
        Gdx.input.inputProcessor = ui
        setup()
    }

    private fun disable() {
        Gdx.input.inputProcessor = null
        Dialog.fadeDuration = 0f
        hideQuit()
        game.resume()
    }

    private fun handleInput() {
        // toggle in-game menu, this shortcut shall be always available
        if (input.isPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            if (quitShown) {
                hideQuit()
            } else {
                enabled = !enabled
                if (enabled) {
                    enable()
                } else {
                    disable()
                }
            }
        }

        if (enabled) {
            if (input.isPressed(com.badlogic.gdx.Input.Keys.Q) && !quitShown) {
                showQuit()
            }

            if (input.isPressed(com.badlogic.gdx.Input.Keys.R)) {
                Gdx.input.inputProcessor = null
                hideQuit()
                ui!!.dispose()
                Art.disposeScreensData()
                Art.loadScreensData()
                constructUI()
                Gdx.input.inputProcessor = ui
                setup()
            }
        }
    }

    fun tick() {
        handleInput()
    }

    fun render(dest: FrameBuffer?) {
        if (!enabled) return
        ui!!.act(URacer.Game.getLastDeltaSecs())

        val hasDest = (dest != null)
        if (hasDest) {
            ui!!.viewport.update(dest.getWidth(), dest.getHeight(), true)
            dest.begin()
        } else {
            ui!!.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        }

        ui!!.draw()

        if (hasDest) {
            dest.end()
        }
    }
}
