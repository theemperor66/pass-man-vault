package com.passman.helpers

import io.ktor.server.auth.*

data class UserSession(val userId: Int, val expireTimestamp: Long) : Principal

