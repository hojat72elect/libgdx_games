package com.bitfire.uracer.utils

import com.badlogic.gdx.Gdx
import com.bitfire.uracer.game.logic.replaying.Replay
import java.net.NetworkInterface
import java.net.SocketException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object DigestUtils {

    @JvmField
    val sha256: MessageDigest
    val HardwareId: String

    init {
        // try setting up sha256 digest
        try {
            sha256 = MessageDigest.getInstance("SHA-256")
        } catch (_: NoSuchAlgorithmException) {
            throw URacerRuntimeException("No support for SHA-256 crypto has been found.")
        }

        // try retrieve macaddress(es)
        val mac = StringBuilder()

        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                val ma: ByteArray? = ni.getHardwareAddress()
                if (ma != null) {
                    for (b in ma) {
                        mac.append(String.format("%02X", b))
                    }
                }
            }
        } catch (e: SocketException) {
            throw URacerRuntimeException("Cannot determine the MAC address of this machine! => ${e.message}")
        }

        if (mac.length < 6) {
            throw URacerRuntimeException("Cannot retrieve a valid MAC address for this machine!")
        }

        HardwareId = mac.toString()
        Gdx.app.log("DigestUtils", "HardwareID set to 0x$HardwareId")
    }

    @JvmStatic
    fun computeDigest(replay: Replay): String {
        if (replay.isValidData) {
            val trackTimeTicks = replay.ticks.toString()
            val created = replay.creationTimestamp.toString()

            sha256.reset()
            sha256.update(HardwareId.toByteArray())
            sha256.update(created.toByteArray())
            sha256.update(replay.userId.toByteArray())
            sha256.update(replay.trackId.toByteArray())
            sha256.update(trackTimeTicks.toByteArray())

            val digest = sha256.digest()
            val replayId = StringBuilder()

            // output MUST be zero-padded
            for (b in digest) {
                replayId.append(String.format("%02x", b))
            }

            return replayId.toString()
        }

        return ""
    }

    @JvmStatic
    fun isValidDigest(digest: String?) = digest != null && digest.length == 64
}
