package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.packet.raknet.reliability.Reliability
import io.github.mucute.qwq.bedrockt.util.*
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

data class FrameSet(
    val sequenceNumber: u32,
    val flags: u8,
    val lengthInBits: u16,
    val reliableFrameIndex: u32?,
    val sequencedFrameIndex: u32?,
    val orderFrameIndex: u32?,
    val orderChannel: u8?,
    val compoundSize: u32?,
    val compoundId: u16?,
    val fragmentIndex: u32?,
    val body: ByteString
) : RakNetPacket {

    inline val isFragment: Boolean
        get() = Reliability.isFragment(flags)

    inline val isReliable: Boolean
        get() = Reliability.isReliable(flags)

    inline val isOrdered: Boolean
        get() = Reliability.isOrdered(flags)

    inline val isSequenced: Boolean
        get() = Reliability.isSequenced(flags)

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<FrameSet> {

        const val BEGIN_ID: u8 = 0x80u

        const val END_ID: u8 = 0x8Du

        override fun encode(packet: FrameSet): ByteString {
            val buffer = Buffer()
            var id: u8 = 0x80u.toUByte() or Reliability.NEEDS_B_AND_AS_FLAG
            if ((packet.flags and 16u) != 0u.toUByte() && packet.fragmentIndex != 0.toUInt()) {
                id = id or Reliability.CONTINUOUS_SEND_FLAG
            }

            buffer.writeU8(id)
            buffer.writeU24Le(packet.sequenceNumber)
            buffer.writeU8(packet.flags)
            buffer.writeU16(packet.lengthInBits)

            if (packet.isReliable) {
                buffer.writeU24Le(packet.reliableFrameIndex!!)
            }

            if (packet.isSequenced) {
                buffer.writeU24Le(packet.sequencedFrameIndex!!)
            }

            if (packet.isOrdered) {
                buffer.writeU24Le(packet.orderFrameIndex!!)
                buffer.writeU8(packet.orderChannel!!)
            }

            if ((packet.flags and 16u) != 0u.toUByte()) {
                buffer.writeU32(packet.compoundSize!!)
                buffer.writeU16(packet.compoundId!!)
                buffer.writeU32(packet.fragmentIndex!!)
            }

            buffer.write(packet.body)

            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): FrameSet {
            val buffer = Buffer().also { it.write(byteString) }
            val sequenceNumber = buffer.readU24Le()
            val flags = buffer.readU8()
            val lengthInBits = buffer.readU16()

            var reliableFrameIndex: u32? = null
            var sequencedFrameIndex: u32? = null
            var orderFrameIndex: u32? = null
            var orderChannel: u8? = null
            var compoundSize: u32? = null
            var compoundId: u16? = null
            var fragmentIndex: u32? = null

            if (Reliability.isReliable(flags)) {
                reliableFrameIndex = buffer.readU24Le()
            }

            if (Reliability.isSequenced(flags)) {
                sequencedFrameIndex = buffer.readU24Le()
            }

            if (Reliability.isOrdered(flags)) {
                orderFrameIndex = buffer.readU24Le()
                orderChannel = buffer.readU8()
            }

            if ((flags and 16u) != 0u.toUByte()) {
                compoundSize = buffer.readU32()
                compoundId = buffer.readU16()
                fragmentIndex = buffer.readU32()
            }

            val body = buffer.readByteString((lengthInBits / 8u).toInt())

            return FrameSet(
                sequenceNumber,
                flags,
                lengthInBits,
                reliableFrameIndex,
                sequencedFrameIndex,
                orderFrameIndex,
                orderChannel,
                compoundSize,
                compoundId,
                fragmentIndex,
                body
            )
        }

    }

}