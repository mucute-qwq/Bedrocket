package io.github.mucute.qwq.bedrockt.server

import io.github.mucute.qwq.bedrockt.logger
import io.github.mucute.qwq.bedrockt.packet.raknet.FrameSetPacket
import io.github.mucute.qwq.bedrockt.packet.raknet.OpenConnectionReply1
import io.github.mucute.qwq.bedrockt.packet.raknet.OpenConnectionReply2
import io.github.mucute.qwq.bedrockt.packet.raknet.OpenConnectionRequest1
import io.github.mucute.qwq.bedrockt.packet.raknet.OpenConnectionRequest2
import io.github.mucute.qwq.bedrockt.packet.raknet.RakNetPacket
import io.github.mucute.qwq.bedrockt.packet.raknet.UnconnectedPing
import io.github.mucute.qwq.bedrockt.packet.raknet.UnconnectedPong
import io.github.mucute.qwq.bedrockt.util.Motd
import io.github.mucute.qwq.bedrockt.util.RakNetClientOfflineCodecMap
import io.github.mucute.qwq.bedrockt.util.u16
import io.github.mucute.qwq.bedrockt.util.u64
import io.github.mucute.qwq.bedrockt.util.u8
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlinx.io.readByteString
import kotlinx.io.readUByte
import kotlinx.io.write
import java.io.Closeable
import kotlin.random.Random
import kotlin.random.nextULong

class RakNetServer(
    val localAddress: InetSocketAddress = InetSocketAddress("0.0.0.0", 19132),
    val serverGUID: u64 = Random.nextULong(),
    val motd: Motd = Motd(serverGUID = serverGUID),
    val mtu: u16 = 1400u
) : Closeable {

    private val hexFormat = HexFormat {
        bytes {
            byteSeparator = " "
        }
        upperCase = true
    }

    private val udpSocket = runBlocking {
        aSocket(ActorSelectorManager(Dispatchers.IO))
            .udp()
            .bind(localAddress)
    }

    val isClosed: Boolean
        get() = udpSocket.isClosed

    suspend fun accept() {
        val datagram = udpSocket.receive()
        val source = datagram.packet
        val remoteAddress = datagram.address as InetSocketAddress
        val id: u8 = source.readUByte()
        val codec = if (id in 0x80u..0x8Du) FrameSetPacket else RakNetClientOfflineCodecMap[id] ?: error(
            "Unknown packet: 0x${id.toHexString()}, body: ${
                source.readByteArray().toHexString(hexFormat)
            }"
        )

        val packet = codec.decode(source.readByteString())
        val extra = source.readByteArray()
        if (extra.isNotEmpty()) {
            logger.warn {
                "Packet: ${id.toHexString()} still has ${extra.size} bytes not to be read, body: ${
                    extra.toHexString(
                        hexFormat
                    )
                }"
            }
        }

        when (packet) {
            is UnconnectedPing -> {
                val unconnectedPong = UnconnectedPong(
                    time = packet.time,
                    serverGUID = serverGUID,
                    motd = motd.toString()
                )
                udpSocket.sendRakNet(unconnectedPong, remoteAddress)
            }

            is OpenConnectionRequest1 -> {
                val openConnectionReply1 = OpenConnectionReply1(
                    serverGUID = serverGUID,
                    useSecurity = false,
                    mtu = mtu
                )
                udpSocket.sendRakNet(openConnectionReply1, remoteAddress)
            }

            is OpenConnectionRequest2 -> {
                val openConnectionReply2 = OpenConnectionReply2(
                    serverGUID = serverGUID,
                    clientAddress = remoteAddress,
                    mtu = packet.mtu,
                    encryptionEnabled = false
                )
                udpSocket.sendRakNet(openConnectionReply2, remoteAddress)
            }
        }

        logger.info { packet }
    }

    override fun close() = udpSocket.close()

    private suspend fun BoundDatagramSocket.sendRakNet(packet: RakNetPacket, remoteAddress: InetSocketAddress) {
        udpSocket.send(Datagram(Buffer().also { it.write(packet.encode()) }, remoteAddress))
    }

}