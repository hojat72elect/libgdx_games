package com.salvai.whatcolor.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.salvai.whatcolor.WhatColor
import com.salvai.whatcolor.global.BACKGROUND_COLOR
import com.salvai.whatcolor.input.CatchBackKeyProcessor

abstract class MyScreen(var game: WhatColor) : ScreenAdapter() {

    init {
        setUpInputMultiplexer()
    }

    fun changeScreen(screen: MyScreen) {
        game.screen = screen
        dispose()
    }

    fun setupScreen() {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }


    fun setUpInputMultiplexer() {
        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(game.stage)
        multiplexer.addProcessor(CatchBackKeyProcessor(game))
        Gdx.input.setInputProcessor(multiplexer)
    }

    override fun render(delta: Float) {
        setupScreen()
        game.stage.act()
        game.stage.draw()
    }


    override fun resize(width: Int, height: Int) {
        game.stage.viewport.update(width, height, true)
    }
}
