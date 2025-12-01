package org.animefoda.topawardsbackend.entities.session

import jakarta.persistence.*
import org.animefoda.topawardsbackend.entities.BaseEntity
import org.animefoda.topawardsbackend.entities.user.UserEntity
import java.time.LocalDateTime

@Entity
@Table(name = "user_sessions") // Dica: Use plural
class SessionEntity : BaseEntity<SessionDTO>() {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    // 'lateinit' promete que vamos preencher essa variavel antes de usar
    // Evita ter que lidar com null
    lateinit var user: UserEntity

    @Column(name = "login_at", nullable = false)
    var loginAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "fingerprint", nullable = false)
    var fingerprint: String = ""

    @Column(name = "ip_address", nullable = false)
    var ipAddress: String = ""

    override fun toDTO(): SessionDTO {
        return SessionDTO(
            id,
            if (::user.isInitialized) user.id else null,
            loginAt,
            fingerprint,
            ipAddress
        )
    }
}