package com.nopalsoft.zombiedash.scene2d

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.nopalsoft.zombiedash.Assets

class LabelDialog(text: String?, isInverted: Boolean) : Table() {
    var lbl: Label

    init {
        lbl = Label(text, Assets.labelStyleHelpDialog)
        lbl.setWrap(true)

        var width = 75f
        var height = 100f

        if (lbl.getWidth() > width) width = lbl.getWidth()
        if (lbl.getHeight() > height) height = lbl.getHeight()

        setSize(width, height)

        if (isInverted) {
            defaults().pad(45f, 10f, 10f, 10f)
            pad(0f)
            setBackground(Assets.helpDialogInverted)
        } else {
            defaults().pad(10f, 10f, 45f, 10f)
            pad(0f)
            setBackground(Assets.helpDialog)
        }

        add<Label?>(lbl).expand().fill()
    }

    override fun setPosition(x: Float, y: Float) {
        var x = x
        x -= 35f
        super.setPosition(x, y)
    }
}
