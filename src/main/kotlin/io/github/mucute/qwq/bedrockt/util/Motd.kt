package io.github.mucute.qwq.bedrockt.util

import kotlin.random.Random
import kotlin.random.nextULong

data class Motd(
    val edition: String = "MCPE",
    val name: String = "Dedicated Server",
    val protocol: i32 = 898,
    val version: String = "1.21.130",
    val playerCount: i32 = 0,
    val playerMax: i32 = 20,
    val serverGUID: u64 = Random.nextULong(),
    val subName: String = "Bedrock level",
    val gameMode: String = "Survival",
    val nintendoLimited: Boolean = false,
    val port: u16 = 19132u,
    val portV6: u16 = 19132u,
) {

    override fun toString() = arrayOf(
        edition,
        name,
        protocol,
        version,
        playerCount,
        playerMax,
        serverGUID,
        subName,
        gameMode,
        if (nintendoLimited) 0x00 else 0x01,
        port.toString(),
        portV6.toString(),
    ).joinToString(separator = ";", postfix = ";")

}