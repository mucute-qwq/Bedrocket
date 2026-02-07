package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.readAddress
import io.github.mucute.qwq.bedrockt.util.readU64
import io.github.mucute.qwq.bedrockt.util.u64
import io.github.mucute.qwq.bedrockt.util.u8
import io.github.mucute.qwq.bedrockt.util.writeAddress
import io.github.mucute.qwq.bedrockt.util.writeU64
import io.github.mucute.qwq.bedrockt.util.writeU8
import io.ktor.network.sockets.InetSocketAddress
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

data class NewIncomingConnection(
    val serverAddress: InetSocketAddress,
    val incomingTimestamp: u64,
    val serverTimestamp: u64
) : RakNetPacket {

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<NewIncomingConnection> {

        const val ID: u8 = 0x13u

        override fun encode(packet: NewIncomingConnection): ByteString {
            val buffer = Buffer()
            buffer.writeU8(ID)
            buffer.writeAddress(packet.serverAddress)
            repeat(20) {
                buffer.writeAddress(InetSocketAddress("255.255.255.255", 19132))
            }
            buffer.writeU64(packet.incomingTimestamp)
            buffer.writeU64(packet.serverTimestamp)
            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): NewIncomingConnection {
            val buffer = Buffer().also { it.write(byteString) }
            val serverAddress = buffer.readAddress()
            repeat(20) {
                buffer.readAddress()
            }
            val incomingTimestamp = buffer.readU64()
            val serverTimestamp = buffer.readU64()
            return NewIncomingConnection(serverAddress, incomingTimestamp, serverTimestamp)
        }


    }

}