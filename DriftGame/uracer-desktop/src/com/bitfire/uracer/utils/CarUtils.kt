package com.bitfire.uracer.utils

import com.badlogic.gdx.Gdx
import com.bitfire.uracer.game.actors.Car

fun mtSecToKmHour(mtsec: Float) = mtsec * 3.6F

fun dumpSpeedInfo(msg: String, subject: String, car: Car, ticks: Int) {

    val elapsed = ReplayUtils.ticksToSeconds(ticks)
    val dist = car.traveledDistance
    val mts = dist / elapsed
    val logMessage = "$subject traveled ${dist}m (acc: ${car.accuDistCount}) in ${String.format("%.03f", elapsed)}s (speed: $mts mt/s) final_position: ${car.getBody().position}"
    Gdx.app.log(msg, logMessage)
}
