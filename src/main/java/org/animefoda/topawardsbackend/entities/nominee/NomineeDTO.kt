package org.animefoda.topawardsbackend.entities.nominee

import org.animefoda.topawardsbackend.entities.BaseDTO

data class NomineeDTO(
    override val id: Int,
    val name: String,
): BaseDTO(id)
