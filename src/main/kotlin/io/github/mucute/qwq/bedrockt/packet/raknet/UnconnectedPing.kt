package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.*
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

// Contains 2 packet ids: 0x01, 0x02
data class UnconnectedPing(
    val time: i64,
    val clientGUID: u64
) : RakNetPacket {

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<UnconnectedPing> {

        const val ID1: u8 = 0x01u

        const val ID2: u8 = 0x02u

        override fun encode(packet: UnconnectedPing): ByteString {
            val buffer = Buffer()
            buffer.writeU8(ID1)
            buffer.writeI64(packet.time)
            buffer.writeMagic()
            buffer.writeU64(packet.clientGUID)
            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): UnconnectedPing {
            val buffer = Buffer()
            buffer.write(byteString)

            val time = buffer.readI64()
            buffer.skipMagic()

            val clientGUID = buffer.readU64()

            return UnconnectedPing(time, clientGUID)
        }

    }

}