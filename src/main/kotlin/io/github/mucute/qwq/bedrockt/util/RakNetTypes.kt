@file:Suppress("NOTHING_TO_INLINE")

package io.github.mucute.qwq.bedrockt.util

import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.SocketAddress
import io.ktor.network.sockets.toJavaAddress
import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteArray
import kotlinx.io.readByteString
import kotlinx.io.readString
import kotlinx.io.write
import kotlinx.io.writeString
import java.net.Inet4Address
import java.net.Inet6Address

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

inline fun Sink.writeAddress(address: InetSocketAddress) {
    val inetSocketAddress = address.toJavaAddress() as java.net.InetSocketAddress
    val inetAddress = inetSocketAddress.address

    when {
        inetAddress is Inet4Address -> {
            writeU8(0x04u)
            write(inetAddress.address)
            writeU16(inetSocketAddress.port.toUShort())
        }

        else -> {
            writeU8(0x06u)
            writeU16(23u)
            writeU16(inetSocketAddress.port.toUShort())
            writeU32(0u)
            write(inetAddress.address)
            writeU32(0u)
        }
    }
}

inline fun Source.readAddress(): InetSocketAddress {
    return when (val version = readU8()) {

        0x04u.toUByte() -> {
            val address = readByteArray(4)
            val port = readU16().toInt()
            InetSocketAddress(Inet4Address.getByAddress(address).hostAddress, port)
        }

        0x06u.toUByte() -> {
            skip(2)
            val port = readU16().toInt()
            skip(4)
            val address = readByteArray(16)
            skip(4)
            InetSocketAddress(Inet6Address.getByAddress(address).hostAddress, port)
        }

        else -> throw IllegalArgumentException("Unknown IP protocol version $version")

    }
}