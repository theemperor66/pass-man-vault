package com.passman

import com.passman.helpers.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    DatabaseFactory.init(environment = environment, log = environment.log)
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
    }
}