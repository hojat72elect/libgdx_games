package com.salvai.whatcolor.screens

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.salvai.whatcolor.WhatColor
import com.salvai.whatcolor.global.FADE_TIME
import com.salvai.whatcolor.global.MENU_ANIMATION_TIME
import com.salvai.whatcolor.global.PATTERN_SCREEN_SIZE
import com.salvai.whatcolor.global.SCREEN_HEIGHT
import com.salvai.whatcolor.global.SCREEN_WIDTH
import com.salvai.whatcolor.global.SPLASH_IMAGE


class SplashScreen(game: WhatColor) : MyScreen(game) {

    init {
        //Splash image
        game.logo = Image(game.myAssetsManager.manager.get(SPLASH_IMAGE, Texture::class.java)).apply {
            this.setSize(PATTERN_SCREEN_SIZE, PATTERN_SCREEN_SIZE)
            this.setPosition(SCREEN_WIDTH * 0.5f - this.width * 0.5f, SCREEN_HEIGHT - this.height * 0.25f)
        }

        game.logo.addAction(Actions.moveBy(0f, -game.logo.height, MENU_ANIMATION_TIME, Interpolation.circle))
        game.stage.addActor(game.logo)
        game.stage.addAction(Actions.fadeIn(FADE_TIME))
        game.myAssetsManager.loadSkin()
        game.myAssetsManager.loadPatternData()
        game.myAssetsManager.loadImages()
        game.myAssetsManager.loadBundle()
    }

    override fun render(delta: Float) {
        super.render(delta)
        if (game.myAssetsManager.manager.update())
            changeScreen(GameScreen(game))
    }
}