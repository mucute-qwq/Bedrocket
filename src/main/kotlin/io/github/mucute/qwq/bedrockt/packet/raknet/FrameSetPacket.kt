package io.github.mucute.qwq.bedrockt.packet.raknet

import io.github.mucute.qwq.bedrockt.codec.Codec
import io.github.mucute.qwq.bedrockt.util.*
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString
import kotlinx.io.write

data class FrameSetPacket(
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

    override fun encode() = encode(this)

    companion object CodecImpl : Codec<FrameSetPacket> {

        const val NEEDS_B_AND_AS_FLAG: u8 = 0x4u

        const val CONTINUOUS_SEND_FLAG: u8 = 0x8u

        override fun encode(packet: FrameSetPacket): ByteString {
            val buffer = Buffer()
            var id: u8 = 0x80u.toUByte() or NEEDS_B_AND_AS_FLAG
            if ((packet.flags and 16u) != 0.toUByte() && packet.fragmentIndex != 0.toUInt()) {
                id = id or CONTINUOUS_SEND_FLAG
            }

            buffer.writeU8(id)
            buffer.writeU24Le(packet.sequenceNumber)
            buffer.writeU8(packet.flags)
            buffer.writeU16(packet.lengthInBits)


            return buffer.readByteString()
        }

        override fun decode(byteString: ByteString): FrameSetPacket {
            val buffer = Buffer().also { it.write(byteString) }
            val sequenceNumber = buffer.readU24Le()
            val flags = buffer.readU8()
            val lengthInBits = buffer.readU16Le()
            return FrameSetPacket(
                sequenceNumber,
                flags,
                lengthInBits,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                ByteString()
            )
        }

    }

}