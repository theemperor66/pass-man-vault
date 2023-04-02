package com.passman

import com.passman.helpers.DAOFacadeImpl
import com.passman.helpers.DatabaseFactory
import com.passman.helpers.models.User
import com.passman.helpers.requests.RegisterRequest
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    DatabaseFactory.init(environment = environment, log = environment.log)
    val dao = DAOFacadeImpl()
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        post("/register") {
            // body to RegisterRequest object
            val registerRequest = call.receive<RegisterRequest>()
            // create user object
            val user = User(
                id = 0,
                email = registerRequest.email,
                passwordHash = registerRequest.passwordHash,
                username = registerRequest.username
            )
            println(user)
            // insert user into db
            dao.addUser(user)

            // respond with user object
            call.respond(user)
        }
        post("/login") {
            val loginRequest = call.receive<RegisterRequest>()
            val user = dao.getUserByUsername(loginRequest.username)
            if (user != null) {
                if (user.passwordHash == loginRequest.passwordHash) {
                    call.respond(user)
                } else {
                    call.respondText("Wrong password")
                }
            } else {
                call.respondText("User not found")
            }
        }
    }
}