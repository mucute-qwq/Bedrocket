package io.github.mucute.qwq.bedrockt.server

import io.github.mucute.qwq.bedrockt.session.RakNetServerSession
import io.github.mucute.qwq.bedrockt.util.Motd
import io.github.mucute.qwq.bedrockt.util.u16
import io.github.mucute.qwq.bedrockt.util.u64
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlin.random.Random
import kotlin.random.nextULong

class RakNetServer {

    var serverGUID: u64 = Random.nextULong()
        set(value) {
            field = value
            motd = motd.copy(serverGUID = value)
        }

    var motd: Motd = Motd(serverGUID = serverGUID)

    var mtu: u16 = 1400u

    internal var udpSocket: BoundDatagramSocket? = null

    fun isActive() = udpSocket != null && !udpSocket!!.isClosed

    suspend fun listen(
        localAddress: InetSocketAddress,
        selectorManager: SelectorManager = ActorSelectorManager(Dispatchers.IO)
    ): RakNetServerSession {
        udpSocket = aSocket(selectorManager)
            .udp()
            .bind(localAddress)

        return RakNetServerSession(this)
    }

    suspend fun listen(
        hostname: String = "0.0.0.0",
        port: Int = 19132,
        selectorManager: SelectorManager = ActorSelectorManager(Dispatchers.IO)
    ) = listen(InetSocketAddress(hostname, port), selectorManager)

}