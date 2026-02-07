package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.*
import io.ktor.network.sockets.*
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

data class OpenConnectionRequest2(
    val serverAddress: InetSocketAddress,
    val mtu: u16,
    val clientGUID: u64
) : RakNetPacket {

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<OpenConnectionRequest2> {

        const val ID: u8 = 0x07u

        override fun encode(packet: OpenConnectionRequest2): ByteString {
            val buffer = Buffer()
            buffer.writeU8(ID)
            buffer.writeMagic()
            buffer.writeAddress(packet.serverAddress)
            buffer.writeU16(packet.mtu)
            buffer.writeU64(packet.clientGUID)
            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): OpenConnectionRequest2 {
            val buffer = Buffer().also { it.write(byteString) }
            buffer.skipMagic()

            val serverAddress = buffer.readAddress()
            val mtu = buffer.readU16()
            val clientGUID = buffer.readU64()
            return OpenConnectionRequest2(serverAddress, mtu, clientGUID)
        }


    }

}