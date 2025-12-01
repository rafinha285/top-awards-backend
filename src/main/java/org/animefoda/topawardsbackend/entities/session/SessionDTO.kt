package org.animefoda.topawardsbackend.entities.session

import org.animefoda.topawardsbackend.entities.BaseDTO
import java.time.LocalDateTime

data class SessionDTO(
    override val id: Int?,
    val userId: Int?,
    val loginAt: LocalDateTime,
    val fingerprint: String,
    val ipAddress: String
) : BaseDTO(id)