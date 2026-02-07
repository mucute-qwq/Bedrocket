package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.readU64
import io.github.mucute.qwq.bedrockt.util.readU8
import io.github.mucute.qwq.bedrockt.util.skipMagic
import io.github.mucute.qwq.bedrockt.util.u64
import io.github.mucute.qwq.bedrockt.util.u8
import io.github.mucute.qwq.bedrockt.util.writeMagic
import io.github.mucute.qwq.bedrockt.util.writeU64
import io.github.mucute.qwq.bedrockt.util.writeU8
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

data class IncompatibleProtocol(
    val protocol: u8,
    val serverGUID: u64
) : RakNetPacket {

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<IncompatibleProtocol> {

        const val ID: u8 = 0x19u

        override fun encode(packet: IncompatibleProtocol): ByteString {
            val buffer = Buffer()
            buffer.writeU8(ID)
            buffer.writeU8(packet.protocol)
            buffer.writeMagic()
            buffer.writeU64(packet.serverGUID)
            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): IncompatibleProtocol {
            val buffer = Buffer().also { it.write(byteString) }
            val protocol = buffer.readU8()
            buffer.skipMagic()

            val serverGUID = buffer.readU64()
            return IncompatibleProtocol(protocol, serverGUID)
        }


    }

}