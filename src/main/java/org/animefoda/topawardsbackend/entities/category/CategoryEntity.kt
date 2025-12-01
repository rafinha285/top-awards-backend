package org.animefoda.topawardsbackend.entities.category

import jakarta.persistence.*
import org.animefoda.topawardsbackend.entities.BaseEntity
import org.animefoda.topawardsbackend.entities.event.EventEntity
import org.animefoda.topawardsbackend.entities.nominee.NomineeDTO
import org.animefoda.topawardsbackend.entities.nominee.NomineeEntity

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

    @ManyToMany(mappedBy = "categories")
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
}