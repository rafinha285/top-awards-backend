package org.animefoda.topawardsbackend.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val fingerprint: String
)
