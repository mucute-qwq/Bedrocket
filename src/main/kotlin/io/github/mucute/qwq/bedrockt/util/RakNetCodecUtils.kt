package io.github.mucute.qwq.bedrockt.util

import io.github.mucute.qwq.bedrockt.packet.raknet.UnconnectedPing
import io.github.mucute.qwq.bedrockt.packet.raknet.UnconnectedPong

@OptIn(ExperimentalUnsignedTypes::class)
val RakNetCodecImplMap = hashMapOf(
    UnconnectedPing.ID1 to UnconnectedPing,
    UnconnectedPing.ID2 to UnconnectedPing,
    UnconnectedPong.ID to UnconnectedPong,
)