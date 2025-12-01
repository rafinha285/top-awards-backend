package org.animefoda.topawardsbackend.entities.session

import org.animefoda.topawardsbackend.entities.BaseDTO
import org.animefoda.topawardsbackend.entities.user.UserEntity
import java.time.LocalDateTime

data class SessionDTO(
    override val id: Int?,
    val userId: Int?,
    val loginAt: LocalDateTime,
    val fingerprint: String,
    val ipAddress: String
) : BaseDTO<SessionDTO, SessionEntity>(id) {
    override fun toEntity(): SessionEntity {

        val entity = SessionEntity()
        entity.id = id
        if (this.userId != null) {
            val userRef = UserEntity()
            userRef.id = this.userId
            // O Hibernate vai usar esse ID para criar a Foreign Key na tabela user_sessions
            entity.user = userRef
        }
        entity.loginAt = loginAt
        entity.fingerprint = fingerprint
        entity.ipAddress = ipAddress
        return entity
    }
}