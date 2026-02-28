import io.github.mucute.qwq.bedrockt.server.RakNetServer
import io.ktor.network.sockets.InetSocketAddress
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Application {

    companion object {

        @JvmStatic
        fun main(args: Array<String>): Unit = runBlocking {
            val localAddress = InetSocketAddress("0.0.0.0", 19132)
            val rakNetServer = RakNetServer(localAddress)
            print("RakNetServer listened on $localAddress")

            while (true) {
                val session = rakNetServer.receive()

            }
        }

    }

}