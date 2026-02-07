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

data class ConnectedPing(
    val time: i64
) : RakNetPacket {

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<ConnectedPing> {

        const val ID: u8 = 0x00u

        override fun encode(packet: ConnectedPing): ByteString {
            val buffer = Buffer()
            buffer.writeU8(ID)
            buffer.writeI64(packet.time)
            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): ConnectedPing {
            val buffer = Buffer().also { it.write(byteString) }
            val time = buffer.readI64()
            return ConnectedPing(time)
        }

    }

}