package org.animefoda.topawardsbackend.service

import jakarta.transaction.Transactional
import org.animefoda.topawardsbackend.entities.user.UserDTO
import org.animefoda.topawardsbackend.entities.user.UserRepository
import org.animefoda.topawardsbackend.exception.NotFound
import org.animefoda.topawardsbackend.response.ApiResponse
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Cacheable("users")
    fun findAll(): List<UserDTO> {
        return userRepository.findAllByActiveTrue().map { it.toDTO() }
    }

    @Cacheable("user")
    fun findById(id: Int): UserDTO {
        val user = userRepository.findById(id)
            .orElseThrow { NotFound("User with id $id not found") }
        return user.toDTO()
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict("users", allEntries = true),
            CacheEvict("user", key = "#id", allEntries = true)
        ]
    )
    fun update(id: Int, userDTO: UserDTO): UserDTO{
        val databaseUser = userRepository.findById(id)
            .orElseThrow { NotFound("User with id $id not found") }

        // 2. Atualiza APENAS os campos que podem mudar (mantendo ID e Senha antigos)
        databaseUser.name = userDTO.name
        databaseUser.email = userDTO.email
        databaseUser.type = userDTO.type
        val savedUser = userRepository.save(databaseUser)
        return savedUser.toDTO()
    }
}