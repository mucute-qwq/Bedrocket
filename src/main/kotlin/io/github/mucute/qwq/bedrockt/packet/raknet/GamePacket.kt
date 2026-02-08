package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.u8
import io.github.mucute.qwq.bedrockt.util.writeU8
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

data class GamePacket(
    val body: ByteString
) : RakNetPacket {

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<GamePacket> {

        const val ID: u8 = 0xFEu

        override fun encode(packet: GamePacket): ByteString {
            val buffer = Buffer()
            buffer.writeU8(ID)
            buffer.write(packet.body)
            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): GamePacket {
            val buffer = Buffer().also { it.write(byteString) }
            val body = buffer.readByteString()
            return GamePacket(body)
        }


    }

}