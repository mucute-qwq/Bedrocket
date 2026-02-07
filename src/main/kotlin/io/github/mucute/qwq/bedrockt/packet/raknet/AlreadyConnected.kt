package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.*
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

data class AlreadyConnected(
    val serverGUID: u64
) : RakNetPacket {

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<AlreadyConnected> {

        const val ID: u8 = 0x12u

        override fun encode(packet: AlreadyConnected): ByteString {
            val buffer = Buffer()
            buffer.writeU8(ID)
            buffer.writeMagic()
            buffer.writeU64(packet.serverGUID)
            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): AlreadyConnected {
            val buffer = Buffer().also { it.write(byteString) }
            buffer.skipMagic()

            val serverGUID = buffer.readU64()
            return AlreadyConnected(serverGUID)
        }

    }

}