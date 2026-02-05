package io.github.mucute.qwq.bedrockt.session

import io.github.mucute.qwq.bedrockt.packet.raknet.RakNetPacket
import io.github.mucute.qwq.bedrockt.shared.RakNetDatagram
import io.ktor.network.sockets.SocketAddress
import kotlinx.io.bytestring.ByteString

interface RakNetSession {

    suspend fun receive(): RakNetDatagram

    suspend fun send(packet: RakNetPacket, address: SocketAddress)

    suspend fun send(datagram: RakNetDatagram)

}