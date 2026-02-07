package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.readAddress
import io.github.mucute.qwq.bedrockt.util.readU16
import io.github.mucute.qwq.bedrockt.util.readU32
import io.github.mucute.qwq.bedrockt.util.readU64
import io.github.mucute.qwq.bedrockt.util.u16
import io.github.mucute.qwq.bedrockt.util.u64
import io.github.mucute.qwq.bedrockt.util.u8
import io.github.mucute.qwq.bedrockt.util.writeAddress
import io.github.mucute.qwq.bedrockt.util.writeU16
import io.github.mucute.qwq.bedrockt.util.writeU64
import io.github.mucute.qwq.bedrockt.util.writeU8
import io.ktor.network.sockets.InetSocketAddress
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

data class ConnectionRequestAccepted(
    val clientAddress: InetSocketAddress,
    val systemIndex: u16,
    val requestTime: u64,
    val time: u64
) : RakNetPacket {

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<ConnectionRequestAccepted> {

        const val ID: u8 = 0x10u

        override fun encode(packet: ConnectionRequestAccepted): ByteString {
            val buffer = Buffer()
            buffer.writeU8(ID)
            buffer.writeAddress(packet.clientAddress)
            buffer.writeU16(packet.systemIndex)
            repeat(10) {
                buffer.writeAddress(InetSocketAddress("255.255.255.255", 19132))
            }
            buffer.writeU64(packet.requestTime)
            buffer.writeU64(packet.time)
            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): ConnectionRequestAccepted {
            val buffer = Buffer().also { it.write(byteString) }
            val clientAddress = buffer.readAddress()
            val systemIndex = buffer.readU16()
            repeat(10) {
                buffer.readAddress()
            }
            val requestTime = buffer.readU64()
            val time = buffer.readU64()
            return ConnectionRequestAccepted(clientAddress, systemIndex, requestTime, time)
        }

    }

}