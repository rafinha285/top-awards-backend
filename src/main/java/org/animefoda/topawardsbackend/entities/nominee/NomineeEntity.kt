package org.animefoda.topawardsbackend.entities.nominee

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter
import org.animefoda.topawardsbackend.entities.BaseEntity
import org.animefoda.topawardsbackend.entities.category.CategoryEntity

@Entity
@Table(name = "nominee")
class NomineeEntity : BaseEntity<NomineeEntity,NomineeDTO>() {
    @Column
    var name = ""

    @Column
    var imageUrl: String? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "nomination",
        joinColumns = [JoinColumn(name = "nominee_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    val categories: MutableSet<CategoryEntity?> = HashSet<CategoryEntity?>()

    public override fun toDTO(): NomineeDTO {
        return NomineeDTO(this.id, name, imageUrl)
    }
}