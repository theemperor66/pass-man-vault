package com.passman

import com.passman.helpers.*
import com.passman.helpers.models.*
import com.passman.helpers.requests.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    val dao = DAOFacadeImpl()
    val secretKey = environment.config.property("ktor.sessions.secret").getString()
    val https = environment.config.property("ktor.deployment.https").getString().lowercase() != "false"
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(Sessions) {
        cookie<UserSession>("SESSION") {
            cookie.maxAgeInSeconds = 600
            cookie.path = "/"
            cookie.httpOnly = true
            cookie.secure = https // Use secure cookies if configured in environment
            cookie.extensions["SameSite"] = "strict" // Set the SameSite attribute to "strict"
            transform(SessionTransportTransformerMessageAuthentication(secretKey.toByteArray()))
        }
    }
    install(Authentication) {
        session<UserSession>("auth-session") {
            validate { session ->
                if (session != null && session.userId >= 0) {
                    // Validate the user session here
                    if (dao.getUserById(session.userId) != null && session.expireTimestamp > System.currentTimeMillis()) {
                        session
                    } else {
                        null
                    }
                } else {
                    null
                }
            }
            challenge {
                // Redirect to the login page if session is not valid
                call.respondRedirect("/login")
            }
        }
    }
    DatabaseFactory.init(environment = environment, log = environment.log)
    routing {
        get("/") {
            // validate the user session
            val user = call.sessions.get<UserSession>()?.let { dao.getUserById(it.userId) }
            if (user != null) {
                call.respondText("Hello, ${user.username}!")
            } else {
                call.respondText { "Hello, guest!" }
            }
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
            // retrieve user from db
            val userFromDb = dao.getUserByUsername(user.username)
            if (userFromDb != null) {
                // respond with user object
                call.respond(userFromDb)
            } else {
                call.respondText("User could not be registered")
            }
        }
        post("/login") {
            val loginRequest = call.receive<RegisterRequest>()
            val user = dao.getUserByUsername(loginRequest.username)
            if (user != null) {
                if (user.passwordHash == loginRequest.passwordHash) {
                    val session = UserSession(user.id, System.currentTimeMillis() + 600 * 1000)
                    call.sessions.set(session)
                } else {
                    call.respondText("Wrong password")
                }
            } else {
                call.respondText("User not found")
            }
        }
        post("/addPwEntry") {
            val user = call.sessions.get<UserSession>()?.let { dao.getUserById(it.userId) }
            if (user != null) {
                //add validation for pwEntry!
                val pwEntry = call.receive<PasswordEntryCreateRequest>()
                val toAdd = PasswordEntry(
                    0,
                    pwEntry.domain,
                    pwEntry.username,
                    pwEntry.annot,
                    pwEntry.passwordEncrypted,
                    0
                )
                dao.addPasswordEntry(toAdd)
                call.respondText("Added Entry")
            } else {
                call.respondText("login first!")
            }
        }
    }

}