package com.passman.helpers

import com.passman.helpers.models.*
import com.typesafe.config.ConfigException.Null
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DAOFacadeImpl : DAOFacade {

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        email = row[Users.email],
        passwordHash = row[Users.passwordHash],
        username = row[Users.username]
    )

    private fun resultRowToPasswordEntry(row: ResultRow) = PasswordEntry(
        id = row[PasswordEntries.id],
        domain = row[PasswordEntries.domain],
        username = row[PasswordEntries.username],
        annot = row[PasswordEntries.annot],
        passwordEncrypted = row[PasswordEntries.passwordEncrypted],
        owner = row[PasswordEntries.owner]
    )

    override suspend fun getAllUsers(): List<User> {
        return Users.selectAll().map(::resultRowToUser)
    }

    override suspend fun getUserById(id: Int): User? {
        return Users.select { Users.id eq id }.map(::resultRowToUser).firstOrNull()
    }

    override suspend fun getUserByUsername(username: String): User? {
        var user: User? = null
        transaction {
            user = Users.select { Users.username eq username }.map(::resultRowToUser).firstOrNull()
        }
        return user
    }

    override suspend fun addUser(user: User): Boolean {
        transaction {
            Users.insert {
                it[email] = user.email
                it[passwordHash] = user.passwordHash
                it[this.username] = user.username
            }
        }
        return true
    }

    override suspend fun updateUser(id: Int, email: String, passwordHash: String): Boolean {
        Users.update({ Users.id eq id }) {
            it[Users.email] = email
            it[Users.passwordHash] = passwordHash
        }
        return true
    }

    override suspend fun deleteUser(id: Int): Boolean {
        Users.deleteWhere { Users.id eq id }
        return true
    }

    override suspend fun getAllPasswordEntries(): List<PasswordEntry> {
        return PasswordEntries.selectAll().map(::resultRowToPasswordEntry)
    }

    override suspend fun getPasswordEntryById(id: Int): PasswordEntry? {
        return PasswordEntries.select { PasswordEntries.id eq id }.map(::resultRowToPasswordEntry).firstOrNull()
    }

    override suspend fun getPasswordEntriesByOwner(owner: Int): List<PasswordEntry> {
        return PasswordEntries.select { PasswordEntries.owner eq owner }.map(::resultRowToPasswordEntry)
    }

    override suspend fun getPasswordEntriesByDomain(domain: String): List<PasswordEntry> {
        return PasswordEntries.select { PasswordEntries.domain eq domain }.map(::resultRowToPasswordEntry)
    }

    override suspend fun addPasswordEntry(passwordEntry: PasswordEntry): PasswordEntry {
        PasswordEntries.insert {
            it[domain] = passwordEntry.domain
            it[username] = passwordEntry.username
            it[annot] = passwordEntry.annot
            it[passwordEncrypted] = passwordEntry.passwordEncrypted
            it[owner] = passwordEntry.owner
        }
        return passwordEntry
    }

    override suspend fun updatePasswordEntry(
        id: Int,
        domain: String,
        username: String,
        annot: String,
        passwordEncrypted: String
    ): Boolean {
        PasswordEntries.update({ PasswordEntries.id eq id }) {
            it[PasswordEntries.domain] = domain
            it[PasswordEntries.username] = username
            it[PasswordEntries.annot] = annot
            it[PasswordEntries.passwordEncrypted] = passwordEncrypted
        }
        return true
    }

    override suspend fun deletePasswordEntry(id: Int): Boolean {
        PasswordEntries.deleteWhere { PasswordEntries.id eq id }
        return true
    }
}