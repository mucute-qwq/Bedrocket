package io.github.mucute.qwq.bedrockt.server

import io.github.mucute.qwq.bedrockt.logger
import io.github.mucute.qwq.bedrockt.packet.raknet.ConnectedPing
import io.github.mucute.qwq.bedrockt.packet.raknet.ConnectedPong
import io.github.mucute.qwq.bedrockt.packet.raknet.ConnectionRequest
import io.github.mucute.qwq.bedrockt.packet.raknet.ConnectionRequestAccepted
import io.github.mucute.qwq.bedrockt.packet.raknet.FrameSet
import io.github.mucute.qwq.bedrockt.packet.raknet.NewIncomingConnection
import io.github.mucute.qwq.bedrockt.packet.raknet.OpenConnectionReply1
import io.github.mucute.qwq.bedrockt.packet.raknet.OpenConnectionReply2
import io.github.mucute.qwq.bedrockt.packet.raknet.OpenConnectionRequest1
import io.github.mucute.qwq.bedrockt.packet.raknet.OpenConnectionRequest2
import io.github.mucute.qwq.bedrockt.packet.raknet.RakNetPacket
import io.github.mucute.qwq.bedrockt.packet.raknet.UnconnectedPing
import io.github.mucute.qwq.bedrockt.packet.raknet.UnconnectedPong
import io.github.mucute.qwq.bedrockt.packet.raknet.datagram.RakNetDatagram
import io.github.mucute.qwq.bedrockt.packet.raknet.reliability.Reliability
import io.github.mucute.qwq.bedrockt.util.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.io.*
import kotlinx.io.write
import java.io.Closeable
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random
import kotlin.random.nextULong
import kotlin.time.Clock

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

    private val sequenceNumberMap = ConcurrentHashMap<String, u32>()

    private val reliableFrameIndexMap = ConcurrentHashMap<String, u32>()

    private val sequencedFrameIndexMap = ConcurrentHashMap<String, u32>()

    private val orderFrameIndexMap = ConcurrentHashMap<String, u32>()

    private val fragmentIndexMap = ConcurrentHashMap<String, u32>()

    val isClosed: Boolean
        get() = udpSocket.isClosed

    suspend fun receive(): RakNetDatagram {
        while (true) {
            val datagram = udpSocket.receive()
            val packet = decode(datagram.packet, false) ?: continue
            val address = datagram.address as InetSocketAddress

            when (packet) {
                is UnconnectedPing -> {
                    val unconnectedPong = UnconnectedPong(
                        time = packet.time,
                        serverGUID = serverGUID,
                        motd = motd.toString()
                    )
                    send(RakNetDatagram(unconnectedPong, address))
                }

                is OpenConnectionRequest1 -> {
                    val openConnectionReply1 = OpenConnectionReply1(
                        serverGUID = serverGUID,
                        useSecurity = false,
                        mtu = mtu
                    )
                    send(RakNetDatagram(openConnectionReply1, address))
                }

                is OpenConnectionRequest2 -> {
                    val openConnectionReply2 = OpenConnectionReply2(
                        serverGUID = serverGUID,
                        clientAddress = address,
                        mtu = packet.mtu,
                        encryptionEnabled = false
                    )
                    send(RakNetDatagram(openConnectionReply2, address))

                }

                is FrameSet -> {
                    val payloadPacket = decode(Buffer().also { it.write(packet.body) }, true)

                    when (payloadPacket) {

                        is ConnectedPing -> {
                            val connectedPong = ConnectedPong(
                                pingTime = payloadPacket.time,
                                pongTime = Clock.System.now().toEpochMilliseconds()
                            )
                            sendFrame(RakNetDatagram(connectedPong, address), Reliability.Unreliable)
                        }

                        is ConnectionRequest -> {
                            val connectionRequestAccepted = ConnectionRequestAccepted(
                                clientAddress = address,
                                systemIndex = 0u,
                                requestTime = payloadPacket.time,
                                time = Clock.System.now().toEpochMilliseconds().toULong()
                            )
                            sendFrame(RakNetDatagram(connectionRequestAccepted, address), Reliability.ReliableOrdered)
                        }

                        is NewIncomingConnection -> {

                        }

                    }

                    logger.warn { packet }
                    logger.warn { payloadPacket }
                }
            }
        }
    }

    private fun decode(source: Source, online: Boolean): RakNetPacket? {
        val id: u8 = source.readU8()
        val codecMap = if (online) RakNetClientOnlineCodecMap else RakNetClientOfflineCodecMap
        val codec = if (id in FrameSet.BEGIN_ID..FrameSet.END_ID) FrameSet.CodecImpl else codecMap[id]
            ?: return null

        val packet = codec.decode(source.readByteString())
        val extra = source.readByteArray()
        if (extra.isNotEmpty()) {
            logger.warn {
                "${if (online) "Online" else "Offline"} packet: ${id.toHexString()} still has ${extra.size} bytes not to be read, body: ${
                    extra.toHexString(
                        hexFormat
                    )
                }"
            }
        }
        return packet
    }

    override fun close() = udpSocket.close()

    suspend fun sendFrame(datagram: RakNetDatagram, reliability: Reliability) {
        val key = datagram.address.toString()
        val sequenceNumber = sequenceNumberMap[key] ?: 0u
        val flags = reliability.toFlags()
        val body = datagram.packet.encode()
        val lengthInBits = (body.size * 8).toUShort()
        val reliableFrameIndex = reliableFrameIndexMap[key] ?: 0u
        val sequencedFrameIndex = sequencedFrameIndexMap[key] ?: 0u
        val orderFrameIndex = orderFrameIndexMap[key] ?: 0u
        val fragmentIndex = fragmentIndexMap[key] ?: 0u

        val frameSet = FrameSet(
            sequenceNumber = sequenceNumber,
            flags = reliability.toFlags(),
            lengthInBits = lengthInBits,
            reliableFrameIndex = reliableFrameIndex,
            sequencedFrameIndex = sequencedFrameIndex,
            orderFrameIndex = orderFrameIndex,
            orderChannel = 0x01u,
            compoundSize = null,
            compoundId = null,
            fragmentIndex = fragmentIndex,
            body = body
        )
        send(datagram.copy(packet = frameSet))

        sequenceNumberMap[key] = sequenceNumberMap[key]?.plus(1u) ?: 0u

        if (Reliability.isReliable(flags)) {
            reliableFrameIndexMap[key] = reliableFrameIndexMap[key]?.plus(1u) ?: 0u
        }

        if (Reliability.isSequenced(flags)) {
            sequencedFrameIndexMap[key] = sequencedFrameIndexMap[key]?.plus(1u) ?: 0u
        }

        if (Reliability.isOrdered(flags)) {
            orderFrameIndexMap[key] = orderFrameIndexMap[key]?.plus(1u) ?: 0u
        }

        if (Reliability.isFragment(flags)) {
            fragmentIndexMap[key] = fragmentIndexMap[key]?.plus(1u) ?: 0u
        }
    }

    suspend fun send(datagram: RakNetDatagram) {
        udpSocket.send(Datagram(Buffer().also { it.write(datagram.packet.encode()) }, datagram.address))
    }

}