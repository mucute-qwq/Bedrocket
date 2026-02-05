package io.github.mucute.qwq.bedrockt.packet.raknet

import kotlinx.io.bytestring.ByteString

interface RakNetPacket {

    fun encode(): ByteString

}