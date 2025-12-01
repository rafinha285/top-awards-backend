package org.animefoda.topawardsbackend.entities.vote

import org.animefoda.topawardsbackend.entities.BaseDTO
import org.animefoda.topawardsbackend.entities.category.CategoryDTO
import org.animefoda.topawardsbackend.entities.nominee.NomineeDTO
import org.animefoda.topawardsbackend.entities.user.UserDTO
import java.time.LocalDateTime

data class VoteDTO(
    override val id: Int?,
    val user: UserDTO,
    val category: CategoryDTO,
    val nominee: NomineeDTO,
    val votedAt: LocalDateTime
): BaseDTO<VoteDTO, VoteEntity>(){
    override fun toEntity(): VoteEntity {
        val entity = VoteEntity()
        entity.id = id
        entity.user = user.toEntity()
        entity.category = category.toEntity()
        entity.nominee = nominee.toEntity()
        entity.votedAt = votedAt
        return entity
    }
}
