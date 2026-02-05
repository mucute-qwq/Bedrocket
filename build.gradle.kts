@file:Suppress("PropertyName")

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
}

group = "io.github.mucute.qwq.bedrockt"
version = "0.0.1"

kotlin {
    jvmToolchain(21)
}

dependencies {
    api(libs.ktor.network)
    api(libs.logback.classic)
    api(libs.kotlinx.datetime)
    api(libs.kotlin.logging)
    testImplementation(libs.kotlin.test.junit)
}
