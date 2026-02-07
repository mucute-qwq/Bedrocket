package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.readBoolean
import io.github.mucute.qwq.bedrockt.util.readU64
import io.github.mucute.qwq.bedrockt.util.u64
import io.github.mucute.qwq.bedrockt.util.u8
import io.github.mucute.qwq.bedrockt.util.writeBoolean
import io.github.mucute.qwq.bedrockt.util.writeU64
import io.github.mucute.qwq.bedrockt.util.writeU8
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

data class ConnectionRequest(
    val currentGUID: u64,
    val time: u64,
    val useSecurity: Boolean
) : RakNetPacket {

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<ConnectionRequest> {

        const val ID: u8 = 0x09u

        override fun encode(packet: ConnectionRequest): ByteString {
            val buffer = Buffer()
            buffer.writeU8(ID)
            buffer.writeU64(packet.currentGUID)
            buffer.writeU64(packet.time)
            buffer.writeBoolean(packet.useSecurity)
            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): ConnectionRequest {
            val buffer = Buffer().also { it.write(byteString) }
            val currentGUID = buffer.readU64()
            val time = buffer.readU64()
            val useSecurity = buffer.readBoolean()
            return ConnectionRequest(currentGUID, time, useSecurity)
        }

    }

}