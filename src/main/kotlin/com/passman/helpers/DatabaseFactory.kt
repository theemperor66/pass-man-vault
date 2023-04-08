package com.passman.helpers

import com.passman.helpers.models.*
import io.ktor.server.application.*
import io.ktor.util.logging.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*
import com.passman.helpers.DAOFacadeImpl
import org.postgresql.util.PSQLException
import java.lang.Error
import java.lang.Exception
import java.sql.DriverManager
import java.util.*


object DatabaseFactory {
    fun init(environment: ApplicationEnvironment, log: Logger) {
        val driverClassName = environment.config.propertyOrNull("db.driverClassName")?.getString()
            ?: "org.postgresql.Driver"
        val jdbcURL = environment.config.propertyOrNull("db.jdbcUrl")?.getString()
            ?: ""
        val user = environment.config.propertyOrNull("db.username")?.getString()
            ?: ""
        val password = environment.config.propertyOrNull("db.password")?.getString()
            ?: ""
        log.info("Connecting to database: $jdbcURL as $user with password $password")
        // extract the database name from the jdbcURL
        val db = jdbcURL.substringAfterLast("/")
        log.info("Database name: $db")

        // Connect to the default 'postgres' database
        val defaultJdbcURL = jdbcURL.substringBeforeLast("/") + "/postgres"
        var database: Database
        // Check if the desired database exists
        try {
            // try to connect to the actual desired database
            database = Database.connect(jdbcURL, driverClassName, user, password)
        } catch (e: Exception) {
            // create the database
            createDatabaseIfNotExists(defaultJdbcURL, user, password, db, log)
            // Connect to the actual desired database
            database = Database.connect(jdbcURL, driverClassName, user, password)
            transaction(database)
            {
                // Create the database schema if it doesn't already exist
                SchemaUtils.createMissingTablesAndColumns(PasswordEntries, Users)
            }
        }
        transaction(database)
        {
            // Create the database schema if it doesn't already exist
            SchemaUtils.createMissingTablesAndColumns(PasswordEntries, Users)
        }
    }

    private fun createDatabaseIfNotExists(
        jdbcURL: String,
        user: String,
        password: String,
        dbName: String,
        log: Logger
    ) {
        val connectionProperties = Properties().apply {
            setProperty("user", user)
            setProperty("password", password)
        }

        DriverManager.getConnection(jdbcURL, connectionProperties).use { connection ->
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery("SELECT 1 FROM pg_database WHERE datname = '$dbName'")
            if (resultSet.next()) {
                log.info("Database '$dbName' already exists.")
            } else {
                log.info("Creating database '$dbName'.")
                statement.executeUpdate("CREATE DATABASE $dbName")
            }
        }
    }

    fun initEmbedded(log: Logger) {
        val database = Database.connect("jdbc:h2:file:./test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(PasswordEntries, Users)
            SchemaUtils.createDatabase()
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}