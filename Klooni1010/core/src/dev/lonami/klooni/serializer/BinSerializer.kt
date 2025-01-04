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
    fun serialize(serializable: BinSerializable, output: OutputStream) = DataOutputStream(output).use { outputStream ->
        outputStream.write(HEADER)
        outputStream.writeInt(VERSION)
        serializable.write(outputStream)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun deserialize(serializable: BinSerializable, input: InputStream) = DataInputStream(input).use { inputStream ->
        // Read the HEADER and the VERSION (checks)
        val savedBuffer = ByteArray(HEADER.size)
        inputStream.readFully(savedBuffer)
        if (savedBuffer.contentEquals(HEADER).not()) throw IOException("Invalid saved header found.")

        val savedVersion = inputStream.readInt()
        if (savedVersion != VERSION)
            throw IOException("Invalid saved version found. Should be $VERSION, not $savedVersion")

        // Read the saved data if the checks passed
        serializable.read(inputStream)
    }
}