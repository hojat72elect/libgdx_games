package com.bitfire.uracer.utils

import com.badlogic.gdx.Gdx
import com.bitfire.uracer.configuration.Config
import com.bitfire.uracer.configuration.Storage
import com.bitfire.uracer.game.logic.replaying.ReplayInfo

object ReplayUtils {

    private fun areValidIds(info: ReplayInfo?): Boolean {
        if (info != null) {
            return DigestUtils.isValidDigest(info.id) && info.userId.isNotBlank() && info.trackId.isNotBlank()
        }

        return false
    }

    @JvmStatic
    fun getDestinationDir(info: ReplayInfo?): String {
        if (areValidIds(info)) {
            return Storage.ReplaysRoot + info!!.getTrackId() + "/" + info.getUserId() + "/"
        }

        return ""
    }

    @JvmStatic
    fun getFullPath(info: ReplayInfo) = getDestinationDir(info) + info.id

    @JvmStatic
    fun pruneReplay(info: ReplayInfo?) {
        if (areValidIds(info)) {
            val rid = info!!.id
            if (!rid.isEmpty()) {
                val path = getFullPath(info)
                if (!path.isEmpty()) {
                    val hf = Gdx.files.external(path)
                    if (hf.exists()) {
                        hf.delete()
                        Gdx.app.log("ReplayUtils", "Pruned #$rid")
                    } else {
                        Gdx.app.error("ReplayUtils", "Couldn't prune #$rid")
                    }
                }
            }
        }
    }

    @JvmStatic
    fun ticksToMilliseconds(ticks: Int) = (ticks * Config.Physics.Dt * AlgebraMath.ONE_ON_CMP_EPSILON).toInt()

    @JvmStatic
    fun ticksToSeconds(ticks: Int) = ticksToMilliseconds(ticks).toFloat() / 1000F
}
