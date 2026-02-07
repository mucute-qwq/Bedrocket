package io.github.mucute.qwq.bedrockt.session

import io.github.mucute.qwq.bedrockt.exception.UnknownPacketException
import io.github.mucute.qwq.bedrockt.logger
import io.github.mucute.qwq.bedrockt.packet.raknet.*
import io.github.mucute.qwq.bedrockt.server.RakNetServer
import io.github.mucute.qwq.bedrockt.shared.RakNetDatagram
import io.github.mucute.qwq.bedrockt.util.RakNetClientOfflineCodecMap
import io.github.mucute.qwq.bedrockt.util.readU8
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlinx.io.readByteString
import kotlinx.io.write

class RakNetServerSession(
    val rakNetServer: RakNetServer
) : RakNetSession {

    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun receive(): RakNetDatagram = withContext(Dispatchers.IO) {
        val udpSocket = rakNetServer.udpSocket!!
        val datagram = udpSocket.receive()
        val source = datagram.packet
        val id = source.readU8()

        var packet: RakNetPacket? = null

        for ((targetId, codecImpl) in RakNetClientOfflineCodecMap) {
            if (id == targetId) {
                packet = codecImpl.decode(source.readByteString())
                break
            }
        }

        val extra = source.readByteArray()
        if (extra.isNotEmpty()) {
            logger.warn {
                "RakNet packet id: 0x${id.toHexString()}, still had ${extra.size} sizes not to be read. Body: ${
                    source.readByteArray().toHexString(format = HexFormat {
                        upperCase = true
                        bytes {
                            byteSeparator = " "
                        }
                    })
                }"
            }
        }

        if (packet == null) {
            throw UnknownPacketException(
                "Unknown RakNet packet id: 0x${id.toHexString()}, body: ${
                    source.readByteArray().toHexString(format = HexFormat {
                        upperCase = true
                        bytes {
                            byteSeparator = " "
                        }
                    })
                }"
            )
        }

        when (packet) {

            is UnconnectedPing -> {
                val unconnectedPong = UnconnectedPong(
                    time = packet.time,
                    serverGUID = rakNetServer.serverGUID,
                    motd = rakNetServer.motd.toString()
                )
                send(unconnectedPong, datagram.address)
            }

            is OpenConnectionRequest1 -> {
                val openConnectionReply1 = OpenConnectionReply1(
                    serverGUID = rakNetServer.serverGUID,
                    useSecurity = false,
                    mtu = rakNetServer.mtu
                )
                send(openConnectionReply1, datagram.address)
            }

            is OpenConnectionRequest2 -> {
                val openConnectionReply2 = OpenConnectionReply2(
                    serverGUID = rakNetServer.serverGUID,
                    clientAddress = datagram.address as InetSocketAddress,
                    mtu = packet.mtu,
                    encryptionEnabled = false
                )
                send(openConnectionReply2, datagram.address)

                val alreadyConnected = AlreadyConnected(rakNetServer.serverGUID)
                send(alreadyConnected, datagram.address)
            }

        }

        RakNetDatagram(packet, datagram.address)

    }

    override suspend fun send(packet: RakNetPacket, address: SocketAddress) = send(RakNetDatagram(packet, address))

    override suspend fun send(datagram: RakNetDatagram) = withContext(Dispatchers.IO) {
        val udpSocket = rakNetServer.udpSocket!!
        Buffer().use {
            it.write(datagram.packet.encode())
            udpSocket.send(Datagram(it, datagram.address))
        }
    }

}