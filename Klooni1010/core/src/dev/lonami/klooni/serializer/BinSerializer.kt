package dev.lonami.klooni.serializer

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object BinSerializer {
    // ascii (klooni) and binary (1010b)
    private val HEADER = byteArrayOf(0x6B, 0x6C, 0x6F, 0x6F, 0x6E, 0x69, 0xa)

    // MODIFY THIS VALUE EVERY TIME A BinSerializable IMPLEMENTATION CHANGES
    // Or unwanted results will happen and corrupt the game in an unknown way.
    private const val VERSION = 2

    @JvmStatic
    @Throws(IOException::class)
    fun serialize(serializable: BinSerializable, output: OutputStream) {
        try {
            DataOutputStream(output).use { outputData ->
                outputData.write(HEADER)
                outputData.writeInt(VERSION)
                serializable.write(outputData)
            }
        } catch (_: IOException) {
        }
    }

    @JvmStatic
    @Throws(IOException::class)
    fun deserialize(serializable: BinSerializable, input: InputStream) {
        try {
            DataInputStream(input).use { inputData ->
                // Read the HEADER and the VERSION (checks)
                val savedBuffer = ByteArray(HEADER.size)
                inputData.readFully(savedBuffer)
                if (!savedBuffer.contentEquals(HEADER)) throw IOException("Invalid saved header found.")

                val savedVersion = inputData.readInt()
                if (savedVersion != VERSION) {
                    throw IOException(
                        "Invalid saved version found. Should be $VERSION, not $savedVersion"
                    )
                }

                // Read the saved data if the checks passed
                serializable.read(inputData)
            }
        } catch (_: IOException) {
        }
    }
}
