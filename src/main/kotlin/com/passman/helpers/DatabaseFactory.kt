package com.passman.helpers

import com.passman.helpers.models.*
import io.ktor.server.application.*
import io.ktor.util.logging.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseFactory {
    fun init(environment: ApplicationEnvironment, log: Logger) {
        val driverClassName = environment.config.propertyOrNull("db.driverClassName")?.getString()
            ?: "org.h2.Driver"
        val jdbcURL = environment.config.propertyOrNull("db.jdbcUrl")?.getString()
            ?: ""
        val user = environment.config.propertyOrNull("db.username")?.getString()
            ?: ""
        val password = environment.config.propertyOrNull("db.password")?.getString()
            ?: ""
        log.info("Connecting to database: $jdbcURL as $user with password $password")
        val database = Database.connect(jdbcURL, driverClassName, user, password)
        transaction(database) {
            SchemaUtils.create(Passwords)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}