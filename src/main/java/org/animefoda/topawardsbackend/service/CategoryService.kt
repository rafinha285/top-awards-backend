package org.animefoda.topawardsbackend.service

import org.animefoda.topawardsbackend.entities.category.CategoryDTO
import org.animefoda.topawardsbackend.entities.category.CategoryEntity
import org.animefoda.topawardsbackend.entities.category.CategoryInputDTO
import org.animefoda.topawardsbackend.entities.category.CategoryRepository
import org.animefoda.topawardsbackend.entities.event.EventRepository
import org.animefoda.topawardsbackend.entities.nominee.NomineeRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val eventRepository: EventRepository,
    private val nomineeRepository: NomineeRepository
) {

    fun create(dto: CategoryInputDTO): CategoryDTO{
        val eventEntity = eventRepository.findById(dto.eventId)
            .orElseThrow { RuntimeException("Evento não encontrado com id: ${dto.eventId}") }

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
}