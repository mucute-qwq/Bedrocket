package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.*
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

data class UnconnectedPong(
    val time: i64,
    val serverGUID: u64,
    val motd: String,
) : RakNetPacket {

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<UnconnectedPong> {

        val ID: u8 = 0x1Cu

        override fun encode(
            packet: UnconnectedPong
        ): ByteString {
            val buffer = Buffer()
            buffer.writeU8(ID)
            buffer.writeI64(packet.time)
            buffer.writeU64(packet.serverGUID)
            buffer.writeMagic()
            buffer.writeRakNetString(packet.motd)
            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): UnconnectedPong {
            val buffer = Buffer().also { it.write(byteString) }
            val time = buffer.readI64()
            val serverGUID = buffer.readU64()
            buffer.skipMagic()

            val motd = buffer.readRakNetString()
            return UnconnectedPong(time, serverGUID, motd)
        }

    }

}