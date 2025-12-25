package org.animefoda.topawardsbackend.service

import org.animefoda.topawardsbackend.entities.nominee.NomineeDTO
import org.animefoda.topawardsbackend.entities.nominee.NomineeEntity
import org.animefoda.topawardsbackend.entities.nominee.NomineeRepository
import org.animefoda.topawardsbackend.exception.NotFound
import org.animefoda.topawardsbackend.response.ApiResponse
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NomineeService(
    private val nomineeRepository: NomineeRepository
) {

    @Cacheable("nominees")
    fun findAll(): List<NomineeDTO> {
        return nomineeRepository.findAll().map { it.toDTO() }
    }

    @Cacheable("nominee")
    fun findById(id: Int): NomineeDTO {
        val nominee =  nomineeRepository.findById(id)
            .orElseThrow { NotFound("Nominee $id not found") }
        return nominee.toDTO()
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict("nominee", allEntries = true),
            CacheEvict("nominees", allEntries = true)
        ]
    )
    fun create(dto: NomineeDTO): NomineeDTO {
        val nominee = NomineeEntity().apply {
            this.name = dto.name
            this.imageUrl = dto.imageUrl
        }
        val savedNominee = nomineeRepository.save(nominee)
        return savedNominee.toDTO()
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict("nominees", allEntries = true),
            CacheEvict("nominee", key = "#id", allEntries = true),
        ]
    )
    fun update(id:Int, dto: NomineeDTO): NomineeDTO {
        val nominee = nomineeRepository.findById(id)
            .orElseThrow { NotFound("Nominee $id not found") }
        
        // Atualiza apenas os campos do DTO, preservando relações
        nominee.name = dto.name
        nominee.imageUrl = dto.imageUrl
        
        val savedNominee = nomineeRepository.save(nominee)
        return savedNominee.toDTO()
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict("nominees", allEntries = true),
            CacheEvict("nominee", key = "#id")
        ]
    )
    fun delete(id: Int): NomineeDTO {
        val nominee = nomineeRepository.findById(id)
            .orElseThrow { NotFound("Nominee $id not found") }
        nomineeRepository.delete(nominee)
        return nominee.toDTO()
    }

    /**
     * Atualiza apenas a imageUrl de um nominee
     */
    @Transactional
    @Caching(
        evict = [
            CacheEvict("nominees", allEntries = true),
            CacheEvict("nominee", key = "#id")
        ]
    )
    fun updateImage(id: Int, imageUrl: String): NomineeDTO {
        val nominee = nomineeRepository.findById(id)
            .orElseThrow { NotFound("Nominee $id not found") }
        nominee.imageUrl = imageUrl
        val savedNominee = nomineeRepository.save(nominee)
        return savedNominee.toDTO()
    }
}
