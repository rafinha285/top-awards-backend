package org.animefoda.topawardsbackend.entities.nominee

import org.springframework.data.jpa.repository.JpaRepository

interface NomineeRepository: JpaRepository<NomineeEntity, Int> {
}