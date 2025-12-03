package org.animefoda.topawardsbackend.request

import org.animefoda.topawardsbackend.entities.user.UserDTO

data class AuthResponse(
    val token: String,
    val user: UserDTO
)