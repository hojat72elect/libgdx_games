package com.salvai.snake.screens.helper

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.salvai.snake.SnakeIt
import com.salvai.snake.utils.Constants

class SpeedChooser(private val game: SnakeIt) {
    @JvmField
    var slider: Slider
    var label: Label?
    private var speedValue: Int

    init {
        speedValue = Constants.WORLD_TIME_MAX - game.worldTime

        label = Label("speed", game.skin)


        slider = Slider(0f, 20f, 5f, false, game.skin, "difficulty")
        slider.setValue(speedValue.toFloat())
        slider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                speedValue = slider.value.toInt()
                game.worldTime = Constants.WORLD_TIME_MAX - speedValue
            }
        })
    }
}
