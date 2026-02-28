package io.github.mucute.qwq.bedrockt.packet.raknet.datagram

import io.github.mucute.qwq.bedrockt.packet.raknet.RakNetPacket
import io.ktor.network.sockets.InetSocketAddress

data class RakNetDatagram(
    val packet: RakNetPacket,
    val address: InetSocketAddress
)