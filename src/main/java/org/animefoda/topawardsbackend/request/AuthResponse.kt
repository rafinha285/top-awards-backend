package org.animefoda.topawardsbackend.request

data class AuthResponse(
    val token: String,
    val name: String
)