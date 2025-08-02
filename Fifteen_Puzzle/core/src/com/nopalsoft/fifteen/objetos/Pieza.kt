package com.nopalsoft.fifteen.objetos

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.nopalsoft.fifteen.Assets

class Pieza(var posicion: Int, valor: Int) : Actor() {
    val SIZE: Float = 110f // Tamano final de la ficha

    var valor: Int // esta pieza la hice privada porque cuando cambio su valor tambien tengo que cambiar la imagen de esta pieza
    var keyframe: TextureRegion? = null

    init {
        setWidth(SIZE)
        setHeight(SIZE)
        setOrigin(SIZE / 2f, SIZE / 2f)

        setPosition(
            mapPosiciones.get(posicion)!!.x,
            mapPosiciones.get(posicion)!!.y
        )
        this.valor = valor

        when (valor) {
            0 -> keyframe = Assets.piezaVacia
            1 -> keyframe = Assets.pieza1
            2 -> keyframe = Assets.pieza2
            3 -> keyframe = Assets.pieza3
            4 -> keyframe = Assets.pieza4
            5 -> keyframe = Assets.pieza5
            6 -> keyframe = Assets.pieza6
            7 -> keyframe = Assets.pieza7
            8 -> keyframe = Assets.pieza8
            9 -> keyframe = Assets.pieza9
            10 -> keyframe = Assets.pieza10
            11 -> keyframe = Assets.pieza11
            12 -> keyframe = Assets.pieza12
            13 -> keyframe = Assets.pieza13
            14 -> keyframe = Assets.pieza14
            15 -> keyframe = Assets.pieza15
            -10 -> keyframe = null
            else -> keyframe = null
        }
    }

    fun moveToPosition(pos: Int) {
        this.posicion = pos
        Gdx.app.log("Move to ", pos.toString() + "")
        addAction(
            Actions.moveTo(
                mapPosiciones.get(posicion)!!.x,
                mapPosiciones.get(posicion)!!.y, .085f
            )
        )
    }

    fun moveInstantly(pos: Int) {
        this.posicion = pos
        setPosition(
            mapPosiciones.get(posicion)!!.x,
            mapPosiciones.get(posicion)!!.y
        )
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        if (keyframe == null) return
        batch.draw(
            keyframe, getX(), getY(), getOriginX(), getOriginY(),
            getWidth(), getHeight(), getScaleX(), getScaleY(),
            getRotation()
        )
    }

    companion object {
        // //Las posiciones empiezan a contar de izq a derecha desde arriba hacia abajo
        val mapPosiciones: LinkedHashMap<Int?, Vector2?> = LinkedHashMap<Int?, Vector2?>()

        init {
            mapPosiciones.put(0, Vector2(20f, 350f))
            mapPosiciones.put(1, Vector2(130f, 350f))
            mapPosiciones.put(2, Vector2(240f, 350f))
            mapPosiciones.put(3, Vector2(350f, 350f))
            mapPosiciones.put(4, Vector2(20f, 240f))
            mapPosiciones.put(5, Vector2(130f, 240f))
            mapPosiciones.put(6, Vector2(240f, 240f))
            mapPosiciones.put(7, Vector2(350f, 240f))
            mapPosiciones.put(8, Vector2(20f, 130f))
            mapPosiciones.put(9, Vector2(130f, 130f))
            mapPosiciones.put(10, Vector2(240f, 130f))
            mapPosiciones.put(11, Vector2(350f, 130f))
            mapPosiciones.put(12, Vector2(20f, 20f))
            mapPosiciones.put(13, Vector2(130f, 20f))
            mapPosiciones.put(14, Vector2(240f, 20f))
            mapPosiciones.put(15, Vector2(350f, 20f))
        }
    }
}
