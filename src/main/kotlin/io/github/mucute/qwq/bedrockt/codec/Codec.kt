package io.github.mucute.qwq.bedrockt.codec

import io.github.mucute.qwq.bedrockt.packet.raknet.RakNetPacket

interface Codec<T : RakNetPacket> : Encoder<T>, Decoder<T>