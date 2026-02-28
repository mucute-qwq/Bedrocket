package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.util.u32

data class Nack(
    val recordCount: u32,
    val singleSequenceNumber: Boolean,
    val noRangeSequenceNumber: u32,
    val startSequenceNumber: u32,
    val endSequenceNumber: u32,
) {
}