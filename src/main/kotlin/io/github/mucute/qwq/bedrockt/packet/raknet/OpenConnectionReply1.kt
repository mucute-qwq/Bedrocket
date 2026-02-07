package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.readAddress
import io.github.mucute.qwq.bedrockt.util.readBoolean
import io.github.mucute.qwq.bedrockt.util.readU16
import io.github.mucute.qwq.bedrockt.util.readU64
import io.github.mucute.qwq.bedrockt.util.skipMagic
import io.github.mucute.qwq.bedrockt.util.u16
import io.github.mucute.qwq.bedrockt.util.u64
import io.github.mucute.qwq.bedrockt.util.u8
import io.github.mucute.qwq.bedrockt.util.writeAddress
import io.github.mucute.qwq.bedrockt.util.writeBoolean
import io.github.mucute.qwq.bedrockt.util.writeMagic
import io.github.mucute.qwq.bedrockt.util.writeU16
import io.github.mucute.qwq.bedrockt.util.writeU64
import io.github.mucute.qwq.bedrockt.util.writeU8
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

data class OpenConnectionReply1(
    val serverGUID: u64,
    val useSecurity: Boolean,
    val mtu: u16
) : RakNetPacket {

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<OpenConnectionReply1> {

        const val ID: u8 = 0x06u

        override fun encode(packet: OpenConnectionReply1): ByteString {
            val buffer = Buffer()
            buffer.writeU8(ID)
            buffer.writeMagic()
            buffer.writeU64(packet.serverGUID)
            buffer.writeBoolean(packet.useSecurity)
            buffer.writeU16(packet.mtu)
            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): OpenConnectionReply1 {
            val buffer = Buffer().also { it.write(byteString) }
            buffer.skipMagic()

            val serverGUID = buffer.readU64()
            val useSecurity = buffer.readBoolean()
            val mtu = buffer.readU16()
            return OpenConnectionReply1(serverGUID, useSecurity, mtu)
        }


    }

}