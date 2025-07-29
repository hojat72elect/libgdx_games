package com.gamestudio24.martianrun.actors.menu

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.gamestudio24.martianrun.utils.AssetsManager.getTextureAtlas

abstract class GameButton(val bounds: Rectangle) : Button() {

    private val skin: Skin


    init {

        setWidth(bounds.width)
        setHeight(bounds.height)
        setBounds(bounds.x, bounds.y, bounds.width, bounds.height)
        skin = Skin()
        skin.addRegions(getTextureAtlas())
        loadTextureRegion()
        addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                touched()
                loadTextureRegion()
                return true
            }
        })
    }

    protected fun loadTextureRegion() {
        val style = ButtonStyle()
        style.up = skin.getDrawable(this.getRegionName())
        setStyle(style)
    }

    protected abstract fun getRegionName(): String?

    abstract fun touched()
}
