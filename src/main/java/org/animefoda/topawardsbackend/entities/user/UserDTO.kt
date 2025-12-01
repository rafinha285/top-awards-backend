package org.animefoda.topawardsbackend.entities.user

import org.animefoda.topawardsbackend.entities.BaseDTO

data class UserDTO(
    override val id: Int?,
    val email: String,
    val name: String,
    val type: UserType,
): BaseDTO<UserDTO, UserEntity>(id) {
    override fun toEntity(): UserEntity {
        val entity = UserEntity()
        entity.id = this.id
        entity.email = email
        entity.name = name
        entity.type = type
        return entity
    }
}
