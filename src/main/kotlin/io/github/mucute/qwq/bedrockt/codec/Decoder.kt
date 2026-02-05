package io.github.mucute.qwq.bedrockt.codec

import io.github.mucute.qwq.bedrockt.packet.raknet.RakNetPacket
import kotlinx.io.bytestring.ByteString

interface Decoder<T : RakNetPacket> {

    fun decode(byteString: ByteString): T

}