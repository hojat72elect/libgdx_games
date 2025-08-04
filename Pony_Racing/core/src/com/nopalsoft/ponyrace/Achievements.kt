package com.nopalsoft.ponyrace

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

class Achievements {
    val EASY: String
    val _18plus: String
    val BIG_LEAGES: String
    val _20_COOLER: String
    val FASTER_THAN_THE_MAKER: String
    val I_WORK_OUT: String
    private val prefAchiv: Preferences = Gdx.app.getPreferences("com.nopalsoft.ponyracing.achievements")
    val len = AssetsHandler.mundoMaximo
    var arrMundos = arrayOfNulls<MundosCompletados>(len)

    init {

        for (i in 0..<len) {
            arrMundos[i] = MundosCompletados(i + 1)
            val obj = arrMundos[i]!!
            obj.easy = prefAchiv.getBoolean("world_easy" + obj.nivel, false)
            obj.normal = prefAchiv.getBoolean("world_normal" + obj.nivel, false)
            obj.hard = prefAchiv.getBoolean("world_hard" + obj.nivel, false)
            obj.superHard = prefAchiv.getBoolean("world_superhard" + obj.nivel, false)
            obj.did15Sec = prefAchiv.getBoolean("world_15Secs" + obj.nivel, false)
        }


        EASY = "easy"
        _18plus = "18plus"
        BIG_LEAGES = "bigleagues"
        _20_COOLER = "20Cooler"
        FASTER_THAN_THE_MAKER = "iworkout"
        I_WORK_OUT = "fasterThanTheMaker"
    }

    fun checkWorldComplete(nivelTiled: Int) {
        var mundoCompletado: MundosCompletados? = null
        val len = arrMundos.size
        for (obj in arrMundos) {
            if (obj!!.nivel == nivelTiled) mundoCompletado = obj
        }
        when (Settings.difficultyLevel) {
            Settings.DIFFICULTY_EASY -> mundoCompletado!!.easy = true
            Settings.DIFFICULTY_NORMAL -> mundoCompletado!!.normal = true
            Settings.DIFFICULTY_HARD -> mundoCompletado!!.hard = true
            Settings.DIFFICULTY_VERY_HARD -> mundoCompletado!!.superHard = true
        }

        for (i in 0..<len) {
            val obj = arrMundos[i]!!
            prefAchiv.putBoolean("world_easy" + obj.nivel, obj.easy)
            prefAchiv.putBoolean("world_normal" + obj.nivel, obj.normal)
            prefAchiv.putBoolean("world_hard" + obj.nivel, obj.hard)
            prefAchiv.putBoolean("world_superhard" + obj.nivel, obj.superHard)
        }
        prefAchiv.flush()

        var easyComplete: Boolean
        var normalComplete: Boolean
        var hardComplete: Boolean
        var superHardComplete: Boolean
        superHardComplete = true
        hardComplete = true
        normalComplete = true
        easyComplete = true

        for (i in 0..<len) {
            val obj = arrMundos[i]!!

            if (!obj.easy) easyComplete = false
            if (!obj.normal) normalComplete = false
            if (!obj.hard) hardComplete = false
            if (!obj.superHard) superHardComplete = false
        }

        if (easyComplete) {
            Gdx.app.log("ACHIEVEMENT", "EASY")
        }

        if (normalComplete) {
            Gdx.app.log("ACHIEVEMENT", "18+")
        }

        if (hardComplete) {
            Gdx.app.log("ACHIEVEMENT", "BIG LEAGUES")
        }

        if (superHardComplete) {
            Gdx.app.log("ACHIEVEMENT", "20% cooler")
        }
    }

    fun checkVictoryMoreThan15Secs(nivelTiled: Int, timeLeft: Float) {
        var mundoCompletado: MundosCompletados? = null
        val len = arrMundos.size
        for (obj in arrMundos) {
            if (obj!!.nivel == nivelTiled) mundoCompletado = obj
        }

        if (timeLeft >= 15 && (Settings.difficultyLevel == Settings.DIFFICULTY_HARD || Settings.difficultyLevel == Settings.DIFFICULTY_VERY_HARD)) {
            mundoCompletado!!.did15Sec = true
        }

        prefAchiv.putBoolean("world_15Secs" + mundoCompletado!!.nivel, mundoCompletado.did15Sec)
        prefAchiv.flush()

        var gotIt = true

        for (i in 0..<len) {
            val obj = arrMundos[i]!!

            if (!obj.did15Sec) {
                gotIt = false
                break
            }
        }

        if (gotIt) {
            Gdx.app.log("ACHIEVEMENT", "FASTER THAN THE MAKER")
        }
    }


    class MundosCompletados(val nivel: Int) {
        var easy: Boolean = false
        var normal: Boolean = false
        var hard: Boolean = false
        var superHard: Boolean = false
        var did15Sec: Boolean = false
    }
}
