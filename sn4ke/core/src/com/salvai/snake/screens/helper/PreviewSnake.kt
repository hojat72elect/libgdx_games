package com.salvai.snake.screens.helper

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Array
import com.salvai.snake.utils.Colors.getBodyColor
import com.salvai.snake.utils.Colors.getHeadColor
import com.salvai.snake.utils.Constants.DURATION
import com.salvai.snake.utils.Constants.INTERPOLATION
import com.salvai.snake.utils.Constants.SCALE

class PreviewSnake(style: Int, amount: Int, texture: Texture, var active: Boolean) {
    var previews: Array<Image> = Array<Image>()

    init {

        for (i in 0..<amount - 1) {
            val body = Image(texture)
            body.setOrigin(body.getWidth() * 0.5f, body.getWidth() * 0.5f)
            body.setColor(getBodyColor(style))
            previews.add(body)
        }

        val head = Image(texture)
        head.setOrigin(head.getWidth() * 0.5f, head.getWidth() * 0.5f)
        head.setColor(getHeadColor(style))
        previews.add(head)

        if (active) for (preview in previews) preview.addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.scaleBy(SCALE, SCALE, DURATION, INTERPOLATION),
                    Actions.scaleBy(-SCALE, -SCALE, DURATION, INTERPOLATION)
                )
            )
        )
    }

    fun startAnimation() {
        if (!active) {
            active = true
            for (preview in previews) {
                preview.clearActions()
                preview.addAction(Actions.forever(Actions.sequence(Actions.scaleBy(SCALE, SCALE, DURATION, INTERPOLATION), Actions.scaleBy(-SCALE, -SCALE, DURATION, INTERPOLATION))))
            }
        }
    }

    fun stopAnimation() {
        if (active) {
            active = false
            for (preview in previews) {
                if (preview.hasActions()) {
                    preview.clearActions()
                    preview.addAction(Actions.scaleTo(1f, 1f, DURATION, INTERPOLATION))
                }
            }
        }
    }
}
