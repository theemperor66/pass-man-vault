package com.passman.helpers.requests

data class LoginRequest(
    var username: String,
    var passwordHash: String
)