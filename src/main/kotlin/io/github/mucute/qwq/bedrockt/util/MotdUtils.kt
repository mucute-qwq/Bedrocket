package io.github.mucute.qwq.bedrockt.util

import kotlin.random.Random
import kotlin.random.nextULong

fun createMotd(
    edition: String = "MCPE",
    name: String = "Dedicated Server",
    protocol: i32 = 898,
    version: String = "1.21.130",
    playerCount: i32 = 0,
    playerMax: i32 = 20,
    serverGUID: u64 = Random.nextULong(),
    subName: String = "Bedrock level",
    gameMode: String = "Survival",
    nintendoLimited: Boolean = false,
    port: u16 = 19132u,
    portV6: u16 = 19132u,
) = listOf(
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