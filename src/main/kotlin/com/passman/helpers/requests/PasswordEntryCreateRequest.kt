package com.passman.helpers.requests

data class PasswordEntryCreateRequest(
    val domain: String,
    val username: String,
    val annot: String,
    val passwordEncrypted: String,
    val ownerUserName: String
)