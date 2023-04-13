package com.passman.helpers.requests

data class PasswordEntryUpdateRequest (
    val id: Int,
    val domain: String,
    val username: String,
    val annot: String,
    val passwordEncrypted: String
)