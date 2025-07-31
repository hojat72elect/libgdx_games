package com.nopalsoft.zombiekiller.scene2d

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.nopalsoft.zombiekiller.Assets

class LabelDialog(text: String?, isInverted: Boolean) : Table() {
    var label: Label = Label(text, Assets.labelStyleHelpDialog)

    init {
        label.setWrap(true)

        var width = 75f
        var height = 100f

        if (label.getWidth() > width) width = label.getWidth()
        if (label.getHeight() > height) height = label.getHeight()

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

        add<Label?>(label).expand().fill()
    }

    override fun setPosition(x: Float, y: Float) {
        var x = x
        x -= 35f
        super.setPosition(x, y)
    }
}
