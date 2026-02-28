package io.github.mucute.qwq.bedrockt.packet.raknet.reliability

import io.github.mucute.qwq.bedrockt.util.u8

enum class Reliability {

    Unreliable,
    UnreliableSequenced,
    Reliable,
    ReliableOrdered,
    ReliableSequenced;

    fun toU8(): u8 = when (this) {
        Unreliable -> 0x00u
        UnreliableSequenced -> 0x01u
        Reliable -> 0x02u
        ReliableOrdered -> 0x03u
        ReliableSequenced -> 0x04u
    }

    fun toFlags(): u8 = (toU8().toInt() shl 5).toUByte()

    companion object {

        const val NEEDS_B_AND_AS_FLAG: u8 = 0x4u

        const val CONTINUOUS_SEND_FLAG: u8 = 0x8u

        fun from(flags: u8) = when (flags) {
            0x00u.toUByte() -> Unreliable
            0x01u.toUByte() -> UnreliableSequenced
            0x02u.toUByte() -> Reliable
            0x03u.toUByte() -> ReliableOrdered
            0x04u.toUByte() -> ReliableSequenced
            else -> error("Unknown reliability flags: $flags")
        }

        fun isFragment(flags: u8) = flags and 16u != 0u.toUByte()

        fun reliability(flags: u8) = Reliability.from(((flags and 224u).toInt() shr 5).toUByte())

        fun isReliable(flags: u8) = when (reliability(flags)) {
            Reliable,
            ReliableOrdered,
            ReliableSequenced -> true

            else -> false
        }

        fun isOrdered(flags: u8) = when (reliability(flags)) {
            UnreliableSequenced,
            ReliableOrdered,
            ReliableSequenced -> true

            else -> false
        }

        fun isSequenced(flags: u8) = when (reliability(flags)) {
            UnreliableSequenced,
            ReliableSequenced -> true

            else -> false
        }

    }

}