package com.nopalsoft.thetruecolor.scene2d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.nopalsoft.thetruecolor.Assets

class ProgressbarTimer(x: Float, y: Float) : Table() {
    private var totalTime = 0f
    private var actualTime = 0f
    var progressBarImage: Image

    var progressBarColor: Color? = null

    var isTimeOver: Boolean = false

    init {
        this.setBounds(x, y, WIDTH, HEIGHT)
        progressBarImage = Image(Assets.barTimerDrawable)
        addActor(progressBarImage)
    }

    fun initialize(color: Color?, totalTime: Float) {
        progressBarColor = color
        this.totalTime = totalTime
        actualTime = 0f
        progressBarImage.setSize(0f, 30f)
        progressBarImage.setColor(progressBarColor)
        isTimeOver = false
    }

    override fun act(delta: Float) {
        super.act(delta)

        if (!isTimeOver) {
            actualTime += Gdx.graphics.getRawDeltaTime()
            if (actualTime >= totalTime) {
                isTimeOver = true
                actualTime = totalTime
            }
            progressBarImage.setWidth(WIDTH * (actualTime / totalTime))
        }
    }

    companion object {
        var WIDTH: Float = 450f
        var HEIGHT: Float = 30f
    }
}
