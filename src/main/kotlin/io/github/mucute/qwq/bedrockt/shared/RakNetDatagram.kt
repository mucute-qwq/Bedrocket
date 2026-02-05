package io.github.mucute.qwq.bedrockt.shared

import io.github.mucute.qwq.bedrockt.packet.raknet.RakNetPacket
import io.ktor.network.sockets.*

data class RakNetDatagram(
    val packet: RakNetPacket,
    val address: SocketAddress,
)