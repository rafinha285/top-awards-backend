package org.animefoda.topawardsbackend.entities.user

import org.animefoda.topawardsbackend.entities.BaseDTO

data class UserDTO(
    override val id: Int,
    val email: String,
    val name: String,
    val type: UserType,
): BaseDTO(id)
