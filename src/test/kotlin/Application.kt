import io.github.mucute.qwq.bedrockt.server.RakNetServer
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class Application {

    companion object {

        @JvmStatic
        fun main(args: Array<String>): Unit = runBlocking {
            val rakNetServer = RakNetServer()
            val rakNetSession = rakNetServer.listen()
            print("RakNetServer listened on 0.0.0.0:19132")

            while (true) {
                val packet = rakNetSession.receive()
                println(packet)
            }
        }

    }

}