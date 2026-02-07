package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.*
import io.ktor.network.sockets.*
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

data class OpenConnectionReply2(
    val serverGUID: u64,
    val clientAddress: InetSocketAddress,
    val mtu: u16,
    val encryptionEnabled: Boolean
) : RakNetPacket {

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<OpenConnectionReply2> {

        const val ID: u8 = 0x08u

        override fun encode(packet: OpenConnectionReply2): ByteString {
            val buffer = Buffer()
            buffer.writeU8(ID)
            buffer.writeMagic()
            buffer.writeU64(packet.serverGUID)
            buffer.writeAddress(packet.clientAddress)
            buffer.writeU16(packet.mtu)
            buffer.writeBoolean(packet.encryptionEnabled)
            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): OpenConnectionReply2 {
            val buffer = Buffer().also { it.write(byteString) }
            buffer.skipMagic()

            val serverGUID = buffer.readU64()
            val clientAddress = buffer.readAddress()
            val mtu = buffer.readU16()
            val encryptionEnabled = buffer.readBoolean()
            return OpenConnectionReply2(serverGUID, clientAddress, mtu, encryptionEnabled)
        }

    }

}