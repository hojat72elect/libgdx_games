package com.nopalsoft.zombiewars.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Peripheral
import com.badlogic.gdx.math.MathUtils
import com.nopalsoft.zombiewars.Assets
import com.nopalsoft.zombiewars.MainZombieWars
import com.nopalsoft.zombiewars.Settings
import com.nopalsoft.zombiewars.screens.Screens

class GameScreen(game: MainZombieWars) : Screens(game) {

    private val oWorld = GameWorld(0)
    private val renderer = WorldGameRenderer2(batcher, oWorld)
    private var accelCamX = 0f

    override fun update(delta: Float) {
        updateRunning(delta)
    }

    override fun draw(delta: Float) {
        renderer.render()
        oCam.update()
        batcher.setProjectionMatrix(oCam.combined)

        batcher.begin()
        Assets.fontGrande.draw(batcher, "FPS: " + Gdx.graphics.framesPerSecond + "\nNew hero: E\nZoom: X,Z", 10f, 400f)
        batcher.end()
    }

    override fun keyDown(keycode: Int): Boolean {

        if (keycode == Input.Keys.ESCAPE) Gdx.app.exit()
        else if (keycode == Input.Keys.C) oWorld.attackAll()
        else if (keycode == Input.Keys.V) oWorld.dieAll()
        else if (keycode == Input.Keys.E) if (MathUtils.randomBoolean()) {
            oWorld.objectCreatorManager.creatHeroLumber()
        } else if (MathUtils.randomBoolean()) {
            oWorld.objectCreatorManager.creatHeroFarmer()
        } else {
            oWorld.objectCreatorManager.creatHeroForce()
        }
        return super.keyDown(keycode)
    }

    private fun updateRunning(delta: Float) {
        accelCamX = if (Gdx.input.isKeyPressed(Input.Keys.A)) -5f
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) 5f
        else if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer))
            Gdx.input.accelerometerY * 1.5f
        else 0f

        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            Settings.zoom += .025f
            if (Settings.zoom > 2.105f) Settings.zoom = 2.105f
        } else if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            Settings.zoom -= .025f
            if (Settings.zoom < 1) Settings.zoom = 1f
        }

        oWorld.update(delta, accelCamX)
    }

}