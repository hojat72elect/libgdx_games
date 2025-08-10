package com.nopalsoft.lander.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.lander.Assets
import com.nopalsoft.lander.MainLander
import com.nopalsoft.lander.Settings
import com.nopalsoft.lander.game.GameScreen
import com.nopalsoft.lander.scene2d.PagedScrollPane

class LevelScreen(game: MainLander) : Screens(game) {

    private val scroll = PagedScrollPane()
    private val contenedor = Table()

    init {

        scroll.setFlingTime(.1f)
        scroll.setPageSpacing(60f) // Espacio entre cada pagina
        contenedor.setFillParent(true)

        var level = 0
        for (l in 0..9) { // Paginas

            val levels = Table().padBottom(100f) // Modificar para el margen
            levels.defaults().pad(20f, 20f, 20f, 20f)
            for (y in 0..3) { // Filas
                levels.row()
                for (x in 0..2) { // columnas
                    levels.add<Button?>(getLevelButton(level)).size(100f)
                    level++
                }
            }
            scroll.addPage(levels)
        }

        contenedor.add<PagedScrollPane?>(scroll).expand().fill().pad(32f).top()

        stage.addActor(contenedor)
    }

    fun getLevelButton(level: Int): Button {
        val button = TextButton("" + (level + 1), Assets.styleTextButtonLevels)

        val stars = Settings.arrEstrellasMundo[level]

        if (Settings.arrIsWorldLocked[level]) {
            button.row()
            button.add(Image(Assets.candado))
            button.setDisabled(true)
        }

        val starTable = Table()
        starTable.defaults().pad(5f)
        if (stars >= 0) {
            for (star in 0..2) {
                if (stars > star) {
                    starTable.add(Image(Assets.star)).width(20f).height(20f)
                } else {
                    starTable.add(Image(Assets.starOff)).width(20f).height(20f)
                }
            }
        }

        button.row()
        button.add(starTable).height(30f)

        button.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (button.isDisabled()) {
                    Gdx.app.log("Locked", "El mundo " + (level + 1) + "esta bloqueado")
                } else game.setScreen(GameScreen(game, level))
            }
        })

        return button
    }

    override fun draw(delta: Float) {
        oCam.update()
        batcher.setProjectionMatrix(oCam.combined)
        batcher.begin()
        batcher.disableBlending()
        batcher.draw(Assets.fondo, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batcher.end()
    }

    override fun update(delta: Float) {
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            game.setScreen(MainMenuScreen(game))
            return true
        }
        return false
    }
}
