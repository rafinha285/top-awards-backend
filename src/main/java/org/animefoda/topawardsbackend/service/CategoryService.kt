package org.animefoda.topawardsbackend.service

import org.animefoda.topawardsbackend.entities.category.CategoryDTO
import org.animefoda.topawardsbackend.entities.category.CategoryEntity
import org.animefoda.topawardsbackend.entities.category.CategoryInputDTO
import org.animefoda.topawardsbackend.entities.category.CategoryRepository
import org.animefoda.topawardsbackend.entities.event.EventRepository
import org.animefoda.topawardsbackend.entities.nominee.NomineeRepository
import org.animefoda.topawardsbackend.exception.NotFound
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val eventRepository: EventRepository,
    private val nomineeRepository: NomineeRepository
) {

    @Cacheable("categories")
    fun findAll(): List<CategoryDTO> {
        return categoryRepository.findAll().map { it.toDTO() }
    }

    @Cacheable(value = ["category"], key = "#id")
    fun findById(id: Int): CategoryDTO {
        return categoryRepository.findById(id)
            .orElseThrow { NotFound("Category $id not found") }
            .toDTO()
    }

    @Cacheable(value = ["categoriesByEvent"], key = "#eventId")
    fun findAllByEvent(eventId: Int): List<CategoryDTO> {
        val event = eventRepository.findById(eventId)
            .orElseThrow { NotFound("EventId $eventId not found") }
        return categoryRepository.findAllByEvent(event).map { it.toDTO() }
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(value = ["categories"], allEntries = true),
            CacheEvict(value = ["categoriesByEvent"], allEntries = true)
        ]
    )
    fun create(dto: CategoryInputDTO): CategoryDTO{
        val eventEntity = eventRepository.findById(dto.eventId)
            .orElseThrow { NotFound("Evento não encontrado com id: ${dto.eventId}") }

        // 2. Busca os Nominees pelos IDs (se houver)
        val nomineesEntities = if (dto.nomineeIds.isNotEmpty()) {
            nomineeRepository.findAllById(dto.nomineeIds).toMutableSet()
        } else {
            mutableSetOf()
        }

        // 3. Monta a Entidade Categoria manualmente
        val entity = CategoryEntity()
        entity.name = dto.name
        entity.description = dto.description
        entity.event = eventEntity // Associa o evento encontrado
        entity.nominees = nomineesEntities

        // 4. Salva e converte de volta para o DTO completo para responder ao front
        val category = categoryRepository.save(entity)
        return category.toDTO()
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(value = ["categories"], allEntries = true),
            CacheEvict(value = ["category"], key = "#id"),
            CacheEvict(value = ["categoriesByEvent"], allEntries = true)
        ]
    )
    fun update(id: Int, dto: CategoryInputDTO): CategoryDTO {
        val existingCategory = categoryRepository.findById(id)
            .orElseThrow { NotFound("Category $id not found") }
        
        val updatedEntity = CategoryEntity.fromInput(
            dto, 
            eventRepository, 
            nomineeRepository, 
            existingCategory
        )
        
        return categoryRepository.save(updatedEntity).toDTO()
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(value = ["categories"], allEntries = true),
            CacheEvict(value = ["category"], key = "#id"),
            CacheEvict(value = ["categoriesByEvent"], allEntries = true)
        ]
    )
    fun delete(id: Int): CategoryDTO {
        val category = categoryRepository.findById(id)
            .orElseThrow { NotFound("Category $id not found") }
        categoryRepository.delete(category)
        return category.toDTO()
    }
}