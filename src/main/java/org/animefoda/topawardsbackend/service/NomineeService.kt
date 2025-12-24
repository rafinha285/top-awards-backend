package org.animefoda.topawardsbackend.service

import org.animefoda.topawardsbackend.entities.nominee.NomineeDTO
import org.animefoda.topawardsbackend.entities.nominee.NomineeEntity
import org.animefoda.topawardsbackend.entities.nominee.NomineeRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
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

    @Transactional
    @CacheEvict(value = ["nominees"], allEntries = true)
    fun create(dto: NomineeDTO): NomineeDTO {
        val nominee = NomineeEntity().apply {
            this.name = dto.name
        }
        val savedNominee = nomineeRepository.save(nominee)
        return savedNominee.toDTO()
    }
}
