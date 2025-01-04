package dev.lonami.klooni.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.Theme

/**
 * A small wrapper to use themed image buttons more easily.
 */
class SoftButton(private val styleIndex: Int, imageName: String) : ImageButton(Klooni.theme.getStyle(styleIndex)) {

    @JvmField
    var image: Drawable? = null

    init {
        updateImage(imageName)
    }

    fun updateImage(imageName: String) {
        image = Theme.skin.getDrawable(imageName)
    }


    override fun draw(batch: Batch, parentAlpha: Float) {
        /*
        * Always update the style to make sure we're using the right image.
        * This might not always be the case since two buttons can be using
        * the "same" style (except for the image up, i.e. after coming from
        * the customize menu), so make sure to update it always.
        */
        Klooni.theme.updateStyle(style, styleIndex)
        style.imageUp = image
        getImage().color = Klooni.theme.foreground
        super.draw(batch, parentAlpha)
    }
}