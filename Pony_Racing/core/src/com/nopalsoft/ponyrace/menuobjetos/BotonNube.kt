package com.nopalsoft.ponyrace.menuobjetos

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor

class BotonNube(var animation: Animation<TextureRegion?>, var texto: String?, var font: BitmapFont) : Actor() {
    @JvmField
    var accionInicial: IniciarAnimacion

    @JvmField
    var wasSelected: Boolean = false
    var stateTime: Float = 0f
    var glyphLayout: GlyphLayout

    init {
        accionInicial = IniciarAnimacion()
        glyphLayout = GlyphLayout()
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (wasSelected) {
            stateTime += delta
            if (stateTime > animation.animationDuration + .5f) { // Despues de que se le da click al boton y se termina la animacion se resetea el boton
                reset()
            }
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.setColor(1f, 1f, 1f, 1f)
        val keyFrame = animation.getKeyFrame(stateTime, false)

        // Para cuando se termine la animacion desaparezca y dude poquito tiempo sin la nube.. por eso al tiempoDuracionAnimacion se
        // le agregaron .3f esos .3f son los extra que va durar en el menu despues de que desaparecio la animacion
        if (stateTime <= animation.animationDuration) batch.draw(keyFrame, getX(), getY(), 0f, 0f, getWidth(), getHeight(), 1f, 1f, getRotation())

        if (wasSelected) return

        // Acomodar la posicion del texto en el centro
        glyphLayout.setText(font, texto)
        val x = getX() + (getWidth() / 2f) - (glyphLayout.width / 2f) - 10 // Menos 10 para que se carge un poco a la izq font
        val y = getY() + (getHeight() / 2f) + (glyphLayout.height / 2f) - 10 // Menos 10 para que se baje poquito el font

        font.draw(batch, texto, x, y)
    }

    fun reset() {
        wasSelected = false
        stateTime = 0f
        accionInicial.tiempoAnimacion = 0f
    }

    inner class IniciarAnimacion : Action() {
        var tiempoAnimacion: Float = 0f

        override fun act(delta: Float): Boolean {
            tiempoAnimacion += delta
            return tiempoAnimacion > animation.animationDuration
        }
    }
}
