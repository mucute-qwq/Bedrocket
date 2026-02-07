package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.u8
import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.append
import kotlinx.io.bytestring.buildByteString

data object Disconnect : RakNetPacket, Codec<Disconnect> {

    const val ID: u8 = 0x15u

    override fun encode() = encode(this)

    override fun encode(packet: Disconnect) = buildByteString {
        append(ID)
    }

    override fun decode(byteString: ByteString) = this

}