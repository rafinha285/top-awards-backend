package org.animefoda.topawardsbackend.entities.session

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface UserSessionRepository: JpaRepository<SessionEntity, Int> {
    @Query("SELECT COUNT(DISTINCT s.user.id) FROM SessionEntity s WHERE s.fingerprint = :fingerprint")
    fun countUniqueUsersByFingerprint(fingerprint: String): Int

    fun countByIpAddressAndLoginAtAfter(ipAddress: String, date: LocalDateTime): Int
}