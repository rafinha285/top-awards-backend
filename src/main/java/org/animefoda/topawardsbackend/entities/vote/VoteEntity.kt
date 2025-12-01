package org.animefoda.topawardsbackend.entities.vote

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.animefoda.topawardsbackend.entities.BaseEntity
import org.animefoda.topawardsbackend.entities.category.CategoryEntity
import org.animefoda.topawardsbackend.entities.nominee.NomineeEntity
import org.animefoda.topawardsbackend.entities.user.UserEntity
import java.time.LocalDateTime

@Entity
@Table(name = "votes")
class VoteEntity: BaseEntity<VoteEntity, VoteDTO>() {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity? = null

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    var category: CategoryEntity? = null

    @ManyToOne
    @JoinColumn(name = "nominee_id", nullable = false)
    var nominee: NomineeEntity? = null

    @Column(name = "voted_at")
    var votedAt: LocalDateTime = LocalDateTime.now()

    public override fun toDTO(): VoteDTO {
        return VoteDTO(this.id, user!!.toDTO(), category!!.toDTO(), nominee!!.toDTO(), votedAt)
    }
}