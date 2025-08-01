package com.salvai.snake.utils

import com.badlogic.gdx.Gdx

object UrlController {
    @JvmStatic
    fun openWebsite() {
        Gdx.net.openURI("https://simondalvai.com")
    }
}
