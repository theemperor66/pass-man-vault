package com.passman

import io.ktor.server.application.*
import com.passman.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSockets()
    configureAdministration()
    configureSerialization()
    configureDatabases()
    configureTemplating()
    configureRouting()
}
