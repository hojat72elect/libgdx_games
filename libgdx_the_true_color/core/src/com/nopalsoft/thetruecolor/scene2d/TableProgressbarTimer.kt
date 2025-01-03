package com.nopalsoft.thetruecolor.scene2d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.nopalsoft.thetruecolor.Assets

class TableProgressbarTimer(x: Float, y: Float) : Table() {

    private var totalTime = 0F
    private var actualTime = 0F
    private val imageBar = Image(Assets.barTimer)
    private var color: Color? = null
    @JvmField
    var timeIsOver = false


    init {
        setBounds(x, y, WIDTH, HEIGHT)
        addActor(imageBar)
    }

    fun initialize(color: Color, totalTime: Float) {
        this.color = color
        this.totalTime = totalTime
        actualTime = 0F
        imageBar.setSize(0F, 30F)
        imageBar.color = this.color
        timeIsOver = false
    }

    override fun act(delta: Float) {
        super.act(delta)

        if (timeIsOver.not()) {
            actualTime += Gdx.graphics.deltaTime
            if (actualTime >= totalTime) {
                timeIsOver = true
                actualTime = totalTime
            }
            imageBar.width = WIDTH * (actualTime / totalTime)
        }
    }

    companion object {
        const val WIDTH = 450F
        const val HEIGHT = 30F
    }
}