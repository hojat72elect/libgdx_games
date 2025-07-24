package dev.lonami.klooni.serializer

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

interface BinSerializable {
    @Throws(IOException::class)
    fun write(output: DataOutputStream)

    @Throws(IOException::class)
    fun read(input: DataInputStream)
}
