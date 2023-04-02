package com.passman.helpers.requests

data class RegisterRequest(
    val email: String,
    val passwordHash: String,
    val username: String
)