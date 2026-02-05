package io.github.mucute.qwq.bedrockt.server

import io.github.mucute.qwq.bedrockt.session.RakNetServerSession
import io.github.mucute.qwq.bedrockt.util.createMotd
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlin.random.Random
import kotlin.random.nextULong

class RakNetServer {

    var serverGUID = Random.nextULong()

    var motd = createMotd(serverGUID = serverGUID)

    internal var udpSocket: BoundDatagramSocket? = null

    fun isActive() = udpSocket != null && !udpSocket!!.isClosed

    suspend fun listen(
        localAddress: SocketAddress,
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