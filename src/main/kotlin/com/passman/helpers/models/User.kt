package com.passman.helpers.models

import org.jetbrains.exposed.sql.Table

data class User(
    val id: Int,
    var email: String,
    var passwordHash: String,
    val username: String
)

object Users : Table() {
    val id = integer("id").autoIncrement()
    var email = varchar("email", 255)
    var passwordHash = varchar("passwordHash", 255)
    val username = varchar("username", 255)
    override val primaryKey = PrimaryKey(PasswordEntries.id)
}