package org.animefoda.topawardsbackend.entities.category

import jakarta.persistence.*
import org.animefoda.topawardsbackend.entities.BaseEntity
import org.animefoda.topawardsbackend.entities.event.EventEntity
import org.animefoda.topawardsbackend.entities.event.EventRepository
import org.animefoda.topawardsbackend.entities.nominee.NomineeDTO
import org.animefoda.topawardsbackend.entities.nominee.NomineeEntity
import org.animefoda.topawardsbackend.entities.nominee.NomineeRepository
import org.animefoda.topawardsbackend.exception.NotFound

@Entity
@Table(name = "category")
class CategoryEntity: BaseEntity<CategoryEntity, CategoryDTO>() {
    @Column(unique = true)
    var name:String = "";

    @Column
    var description = ""

    @JoinColumn(name = "event_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var event: EventEntity? = null

    @ManyToMany(fetch = FetchType.EAGER) // Eager ou Lazy, depende do seu uso
    @JoinTable(
        name = "nomination", // Nome da tabela de junção no banco
        joinColumns = [JoinColumn(name = "category_id")],
        inverseJoinColumns = [JoinColumn(name = "nominee_id")]
    )
    var nominees: MutableSet<NomineeEntity?> = mutableSetOf()

    override fun toDTO(): CategoryDTO {
        return CategoryDTO(
            this.id,
            name,
            description,
            event!!.toDTO(),
            nominees.stream().map<NomineeDTO> { obj: NomineeEntity? -> obj!!.toDTO() }.toList()
        )
    }

    companion object {
        /**
         * Creates or updates a CategoryEntity from CategoryInputDTO
         * @param dto The input DTO with category data
         * @param eventRepository Repository to fetch the event
         * @param nomineeRepository Repository to fetch nominees
         * @param existingEntity Optional existing entity for updates (null for create)
         */
        fun fromInput(
            dto: CategoryInputDTO,
            eventRepository: EventRepository,
            nomineeRepository: NomineeRepository,
            existingEntity: CategoryEntity? = null
        ): CategoryEntity {
            val eventEntity = eventRepository.findById(dto.eventId)
                .orElseThrow { NotFound("Event not found with id: ${dto.eventId}") }

            val nomineesEntities = if (dto.nomineeIds.isNotEmpty()) {
                nomineeRepository.findAllById(dto.nomineeIds).toMutableSet()
            } else {
                mutableSetOf()
            }

            val entity = existingEntity ?: CategoryEntity()
            entity.name = dto.name
            entity.description = dto.description
            entity.event = eventEntity
            entity.nominees = nomineesEntities
            
            return entity
        }
    }
}