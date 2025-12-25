package org.animefoda.topawardsbackend.entities.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Int> {
    fun findByEmail(email: String): UserEntity?
    fun findAllByActiveTrue(): List<UserEntity>
}