package com.passman.helpers.models

import org.jetbrains.exposed.sql.*

data class Password(
    val id: Int,
    val domain: String,
    val username: String,
    val annot: String,
    val passwordEncrypted: String,
    val owner: String,
    val roles: List<String>
)

object Passwords : Table() {
    val id = integer("id").autoIncrement()
    val domain = varchar("domain", 255)
    val username = varchar("username", 255)
    val annot = varchar("annot", 255)
    val passwordEncrypted = varchar("passwordEncrypted", 255)
    val owner = varchar("owner", 255)
    val roles = varchar("roles", 255)

    override val primaryKey = PrimaryKey(id)
}