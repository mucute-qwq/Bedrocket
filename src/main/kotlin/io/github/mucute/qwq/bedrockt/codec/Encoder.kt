package io.github.mucute.qwq.bedrockt.codec

import io.github.mucute.qwq.bedrockt.packet.raknet.RakNetPacket
import io.github.mucute.qwq.bedrockt.util.u8
import kotlinx.io.bytestring.ByteString

interface Encoder<T : RakNetPacket> {

    fun encode(packet: T): ByteString

}