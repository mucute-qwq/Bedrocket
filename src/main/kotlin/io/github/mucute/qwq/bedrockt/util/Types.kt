@file:Suppress("NOTHING_TO_INLINE")

package io.github.mucute.qwq.bedrockt.util

import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.io.readIntLe
import kotlinx.io.readLongLe
import kotlinx.io.readShortLe
import kotlinx.io.readUByte
import kotlinx.io.readUInt
import kotlinx.io.readUIntLe
import kotlinx.io.readULong
import kotlinx.io.readULongLe
import kotlinx.io.readUShort
import kotlinx.io.readUShortLe
import kotlinx.io.writeIntLe
import kotlinx.io.writeLongLe
import kotlinx.io.writeShortLe
import kotlinx.io.writeUByte
import kotlinx.io.writeUInt
import kotlinx.io.writeUIntLe
import kotlinx.io.writeULong
import kotlinx.io.writeULongLe
import kotlinx.io.writeUShort
import kotlinx.io.writeUShortLe

typealias u8 = UByte

typealias u16 = UShort

typealias u32 = UInt

typealias u64 = ULong

typealias i8 = Byte

typealias i16 = Short

typealias i32 = Int

typealias i64 = Long

inline fun Sink.writeU8(value: u8) {
    writeUByte(value)
}

inline fun Sink.writeU16(value: u16) {
    writeUShort(value)
}

inline fun Sink.writeU32(value: u32) {
    writeUInt(value)
}

inline fun Sink.writeU64(value: u64) {
    writeULong(value)
}

inline fun Sink.writeU16Le(value: u16) {
    writeUShortLe(value)
}

inline fun Sink.writeU32Le(value: u32) {
    writeUIntLe(value)
}

inline fun Sink.writeU64Le(value: u64) {
    writeULongLe(value)
}

inline fun Sink.writeI8(value: i8) {
    writeByte(value)
}

inline fun Sink.writeI16(value: i16) {
    writeShort(value)
}

inline fun Sink.writeI32(value: i32) {
    writeInt(value)
}

inline fun Sink.writeI64(value: i64) {
    writeLong(value)
}

inline fun Sink.writeI16Le(value: i16) {
    writeShortLe(value)
}

inline fun Sink.writeI32Le(value: i32) {
    writeIntLe(value)
}

inline fun Sink.writeI64Le(value: i64) {
    writeLongLe(value)
}

inline fun Source.readU8(): u8 {
    return readUByte()
}

inline fun Source.readU16(): u16 {
    return readUShort()
}

inline fun Source.readU32(): u32 {
    return readUInt()
}

inline fun Source.readU64(): u64 {
    return readULong()
}

inline fun Source.readU16Le(): u16 {
    return readUShortLe()
}

inline fun Source.readU32Le(): u32 {
    return readUIntLe()
}

inline fun Source.readU64Le(): u64 {
    return readULongLe()
}

inline fun Source.readI8(): i8 {
    return readByte()
}

inline fun Source.readI16(): i16 {
    return readShort()
}

inline fun Source.readI32(): i32 {
    return readInt()
}

inline fun Source.readI64(): i64 {
    return readLong()
}

inline fun Source.readI16Le(): i16 {
    return readShortLe()
}

inline fun Source.readI32Le(): i32 {
    return readIntLe()
}

inline fun Source.readI64Le(): i64 {
    return readLongLe()
}