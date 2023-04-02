package com.passman.helpers.models

import org.jetbrains.exposed.sql.*

data class PasswordEntry(
    val id: Int,
    var domain: String,
    var username: String,
    var annot: String,
    var passwordEncrypted: String,
    val owner: Int,
)

object PasswordEntries : Table() {
    val id = integer("id").autoIncrement()
    val domain = varchar("domain", 255)
    val username = varchar("username", 255)
    val annot = varchar("annot", 255)
    val passwordEncrypted = varchar("passwordEncrypted", 255)
    val owner = integer("owner").uniqueIndex().references(Users.id)

    override val primaryKey = PrimaryKey(id)
}