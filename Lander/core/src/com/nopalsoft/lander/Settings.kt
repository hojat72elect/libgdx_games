package com.nopalsoft.lander

import com.badlogic.gdx.Gdx

object Settings {

    private val pref = Gdx.app.getPreferences("com.tiar.aaa.aaa.aa")

    @JvmField
    var nivelVida = 0

    @JvmField
    var nivelGas = 0

    @JvmField
    var nivelRotacion = 0

    @JvmField
    var nivelVelocidadY = 0

    @JvmField
    var nivelPower = 0

    @JvmField
    var nivelOtro1 = 0
    lateinit var arrEstrellasMundo: IntArray // Cada posicion es un mundo
    lateinit var arrIsWorldLocked: BooleanArray

    @JvmStatic
    fun load(numMapas: Int) {
        arrEstrellasMundo = IntArray(numMapas)
        arrIsWorldLocked = BooleanArray(numMapas)
        pref.clear()
        pref.flush()
        for (i in 0..<numMapas) {
            arrEstrellasMundo[i] = pref.getInteger("arrEstrellasMundo$i", 0)

            if (i == 0) arrIsWorldLocked[0] = false
            else arrIsWorldLocked[i] = pref.getBoolean("arrIsWorldLocked$i", true)
        }

        // Upgrades
        nivelVida = pref.getInteger("nivelVida", 0)
        nivelGas = pref.getInteger("nivelGas", 0)
        nivelRotacion = pref.getInteger("nivelRotacion", 0)
        nivelVelocidadY = pref.getInteger("nivelVelocidadY", 0)
        nivelPower = pref.getInteger("nivelPower", 0)
        nivelOtro1 = pref.getInteger("nivelOtro1", 0)

        // Fin upgrades
    }

    fun save() {
        for (i in arrEstrellasMundo.indices) {
            pref.putInteger("arrEstrellasMundo$i", arrEstrellasMundo[i])
            pref.putBoolean("arrIsWorldLocked$i", arrIsWorldLocked[i])
        }

        // Upgrades
        pref.putInteger("nivelVida", nivelVida)
        pref.putInteger("nivelGas", nivelGas)
        pref.putInteger("nivelRotacion", nivelRotacion)
        pref.putInteger("nivelVelocidadY", nivelVelocidadY)
        pref.putInteger("nivelPower", nivelPower)
        pref.putInteger("nivelOtro1", nivelOtro1)

        // Fin upgrades
        pref.flush()
    }

    /**
     * Guarda el numero de estrellas del mundo que se completo y desbloque el siguiente mundo
     */
    @JvmStatic
    fun setStarsFromLevel(level: Int, numStars: Int) {
        val startActuales = arrEstrellasMundo[level]

        if (startActuales < numStars) {
            arrEstrellasMundo[level] = numStars
        }

        if (arrIsWorldLocked.size >= level + 1) arrIsWorldLocked[level + 1] = false

        save()
    }
}
