package io.github.mucute.qwq.bedrockt.util

import io.github.mucute.qwq.bedrockt.packet.raknet.ConnectionRequest
import io.github.mucute.qwq.bedrockt.packet.raknet.Disconnect
import io.github.mucute.qwq.bedrockt.packet.raknet.GamePacket
import io.github.mucute.qwq.bedrockt.packet.raknet.IncompatibleProtocol
import io.github.mucute.qwq.bedrockt.packet.raknet.NewIncomingConnection
import io.github.mucute.qwq.bedrockt.packet.raknet.OpenConnectionRequest1
import io.github.mucute.qwq.bedrockt.packet.raknet.OpenConnectionRequest2
import io.github.mucute.qwq.bedrockt.packet.raknet.UnconnectedPing

val RakNetClientOfflineCodecMap = hashMapOf(
    UnconnectedPing.ID1 to UnconnectedPing,
    OpenConnectionRequest1.ID to OpenConnectionRequest1,
    OpenConnectionRequest2.ID to OpenConnectionRequest2,
    IncompatibleProtocol.ID to IncompatibleProtocol,
)

val RakNetClientOnlineCodecMap = hashMapOf(
    ConnectionRequest.ID to ConnectionRequest,
    NewIncomingConnection.ID to NewIncomingConnection,
    GamePacket.ID to GamePacket,
)