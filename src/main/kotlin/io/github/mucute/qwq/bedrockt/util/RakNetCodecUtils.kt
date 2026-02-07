package io.github.mucute.qwq.bedrockt.util

import io.github.mucute.qwq.bedrockt.packet.raknet.AlreadyConnected
import io.github.mucute.qwq.bedrockt.packet.raknet.ConnectedPing
import io.github.mucute.qwq.bedrockt.packet.raknet.ConnectedPong
import io.github.mucute.qwq.bedrockt.packet.raknet.ConnectionRequest
import io.github.mucute.qwq.bedrockt.packet.raknet.ConnectionRequestAccepted
import io.github.mucute.qwq.bedrockt.packet.raknet.Disconnect
import io.github.mucute.qwq.bedrockt.packet.raknet.IncompatibleProtocol
import io.github.mucute.qwq.bedrockt.packet.raknet.NewIncomingConnection
import io.github.mucute.qwq.bedrockt.packet.raknet.OpenConnectionReply1
import io.github.mucute.qwq.bedrockt.packet.raknet.OpenConnectionReply2
import io.github.mucute.qwq.bedrockt.packet.raknet.OpenConnectionRequest1
import io.github.mucute.qwq.bedrockt.packet.raknet.OpenConnectionRequest2
import io.github.mucute.qwq.bedrockt.packet.raknet.UnconnectedPing
import io.github.mucute.qwq.bedrockt.packet.raknet.UnconnectedPong

val RakNetClientOfflineCodecMap = hashMapOf(
    UnconnectedPing.ID1 to UnconnectedPing,
    OpenConnectionRequest1.ID to OpenConnectionRequest1,
    OpenConnectionRequest2.ID to OpenConnectionRequest2,
    Disconnect.ID to Disconnect,
)