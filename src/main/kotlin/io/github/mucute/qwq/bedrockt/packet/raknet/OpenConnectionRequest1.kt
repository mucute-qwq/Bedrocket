package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.readU16
import io.github.mucute.qwq.bedrockt.util.readU8
import io.github.mucute.qwq.bedrockt.util.skipMagic
import io.github.mucute.qwq.bedrockt.util.u16
import io.github.mucute.qwq.bedrockt.util.u8
import io.github.mucute.qwq.bedrockt.util.writeMagic
import io.github.mucute.qwq.bedrockt.util.writeU16
import io.github.mucute.qwq.bedrockt.util.writeU8
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

data class OpenConnectionRequest1(
    val protocolVersion: u8,
    val mtu: u16
) : RakNetPacket {

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<OpenConnectionRequest1> {

        const val ID: u8 = 0x05u

        override fun encode(packet: OpenConnectionRequest1): ByteString {
            val buffer = Buffer()
            buffer.writeU8(ID)
            buffer.writeMagic()
            buffer.writeU8(packet.protocolVersion)
            buffer.writeU16((packet.mtu - 46u).toUShort())
            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): OpenConnectionRequest1 {
            val buffer = Buffer().also { it.write(byteString) }
            buffer.skipMagic()

            val protocolVersion = buffer.readU8()
            val mtu = (buffer.readByteString().size + 28).toUShort()
            return OpenConnectionRequest1(protocolVersion, mtu)
        }

    }

}