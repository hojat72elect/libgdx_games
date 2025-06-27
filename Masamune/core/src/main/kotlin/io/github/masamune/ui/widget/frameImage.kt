package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Scaling
import ktx.scene2d.KGroup
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.image
import ktx.scene2d.scene2d

@Scene2dDsl
class FrameImage(
    private val skin: Skin,
    frameDrawable: Drawable,
    imageDrawable: Drawable? = null,
) : Stack(), KGroup {
    private val image: Image

    init {
        val frameImage = scene2d.image(frameDrawable) {
            setScaling(Scaling.contain)
        }
        image = scene2d.image(imageDrawable) {
            setScaling(Scaling.contain)
        }

        add(frameImage)
        add(image)
    }

    fun imageDrawable(drawable: String) {
        image.drawable = skin.getDrawable(drawable)
    }
}

@Scene2dDsl
fun <S> KWidget<S>.frameImage(
    skin: Skin,
    frameDrawableName: String,
    imageDrawableName: String? = null,
    init: (@Scene2dDsl FrameImage).(S) -> Unit = {},
): FrameImage {
    val imageDrawable = if (imageDrawableName == null) null else skin.getDrawable(imageDrawableName)
    return actor(FrameImage(skin, skin.getDrawable(frameDrawableName), imageDrawable), init)
}

@Scene2dDsl
fun <S> KWidget<S>.frameImage(
    skin: Skin,
    frameDrawable: Drawable,
    imageDrawable: Drawable? = null,
    init: (@Scene2dDsl FrameImage).(S) -> Unit = {},
): FrameImage = actor(FrameImage(skin, frameDrawable, imageDrawable), init)
