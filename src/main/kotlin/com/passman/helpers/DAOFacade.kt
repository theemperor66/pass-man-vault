package com.passman.helpers

import com.passman.helpers.models.*

interface DAOFacade {
    suspend fun getAllUsers(): List<User>
    suspend fun getUserById(id: Int): User?
    suspend fun getUserByUsername(username: String): User?
    suspend fun addUser(user: User): Boolean
    suspend fun updateUser(id: Int, email: String, passwordHash: String): Boolean
    suspend fun deleteUser(id: Int): Boolean
    suspend fun getAllPasswordEntries(): List<PasswordEntry>
    suspend fun getPasswordEntryById(id: Int): PasswordEntry?
    suspend fun getPasswordEntriesByOwner(owner: Int): List<PasswordEntry>
    suspend fun getPasswordEntriesByDomain(domain: String): List<PasswordEntry>
    suspend fun addPasswordEntry(passwordEntry: PasswordEntry): PasswordEntry
    suspend fun updatePasswordEntry(
        id: Int,
        domain: String,
        username: String,
        annot: String,
        passwordEncrypted: String
    ): Boolean

    suspend fun deletePasswordEntry(id: Int): Boolean
}