package com.nopalsoft.zombiekiller.scene2d

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.Settings

class TouchPadControls : Table() {

    var isMovingUp: Boolean = false
    var isMovingDown: Boolean = false
    var isMovingLeft: Boolean = false
    var isMovingRight: Boolean = false
    var widthButtons: Float = 0f
    var btUp: Button? = null
    var btDown: Button? = null
    var btLeft: Button? = null
    var btRight: Button? = null

    init {

        btUp = Button(Assets.btUp, Assets.btUpPress, Assets.btUpPress)
        btUp!!.addListener(object : ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                isMovingUp = true
                btUp!!.setChecked(true)
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                isMovingUp = false
                btUp!!.setChecked(false)
            }
        })

        btDown = Button(Assets.btDown, Assets.btDownPress, Assets.btDownPress)
        btDown!!.addListener(object : ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                isMovingDown = true
                btDown!!.setChecked(true)
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                isMovingDown = false
                btDown!!.setChecked(false)
            }
        })

        btLeft = Button(Assets.btLeft, Assets.btLeftPress, Assets.btLeftPress)
        btLeft!!.addListener(object : ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                isMovingLeft = true
                btLeft!!.setChecked(true)
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                isMovingLeft = false
                btLeft!!.setChecked(false)
            }
        })

        btRight = Button(Assets.btRight, Assets.btRightPress, Assets.btRightPress)
        btRight!!.addListener(object : ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                isMovingRight = true
                btRight!!.setChecked(true)
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                isMovingRight = false
                btRight!!.setChecked(false)
            }
        })

        setNewSize(Settings.padSize)
    }

    fun setNewSize(width: Float) {
        this.widthButtons = width
        clearChildren()

        defaults().size(widthButtons / 3f)

        add<Button?>(btUp).colspan(2).center()
        row()
        add<Button?>(btLeft).left()
        add<Button?>(btRight).right().padLeft(widthButtons / 3.5f)
        row()
        add<Button?>(btDown).colspan(2).center()
        pack()
    }
}
