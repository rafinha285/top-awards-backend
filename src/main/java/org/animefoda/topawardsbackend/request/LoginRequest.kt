package org.animefoda.topawardsbackend.request

data class LoginRequest(
    val email: String,
    val password: String,
    val fingerprint: String
)
