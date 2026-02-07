package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.i64
import io.github.mucute.qwq.bedrockt.util.readI64
import io.github.mucute.qwq.bedrockt.util.u8
import io.github.mucute.qwq.bedrockt.util.writeI64
import io.github.mucute.qwq.bedrockt.util.writeU8
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

class ConnectedPong(
    val pingTime: i64,
    val pongTime: i64,
) : RakNetPacket {

    override fun encode() = encode(this)

    companion object CodecImpl: Codec<ConnectedPong> {

        const val ID: u8 = 0x03u

        override fun encode(packet: ConnectedPong): ByteString {
            val buffer = Buffer()
            buffer.writeU8(ID)
            buffer.writeI64(packet.pingTime)
            buffer.writeI64(packet.pongTime)
            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): ConnectedPong {
            val buffer = Buffer().also { it.write(byteString) }
            val pingTime = buffer.readI64()
            val pongTime = buffer.readI64()
            return ConnectedPong(pingTime, pongTime)
        }


    }

}