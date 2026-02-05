@file:Suppress("NOTHING_TO_INLINE")

package io.github.mucute.qwq.bedrockt.util

import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.readString
import kotlinx.io.write
import kotlinx.io.writeString

val Magic = ByteString(0, -1, -1, 0, -2, -2, -2, -2, -3, -3, -3, -3, 18, 52, 86, 120)

inline fun Sink.writeMagic() {
    write(Magic)
}

inline fun Source.readMagic(): ByteString {
    return readByteString(Magic.size)
}

inline fun Source.skipMagic() {
    skip(Magic.size.toLong())
}

inline fun Sink.writeBoolean(value: Boolean) {
    writeByte(if (value) 0x01 else 0x00)
}

inline fun Source.readBoolean(): Boolean {
    return readByte() != 0x00.toByte()
}

inline fun Sink.writeRakNetString(string: String) {
    val size = string.length.toUShort()
    writeU16(size)
    writeString(string)
}

inline fun Source.readRakNetString(): String {
    val size = readU16()
    return readString(size.toLong())
}