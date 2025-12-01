package org.animefoda.topawardsbackend.entities

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import lombok.Setter

@Setter
@MappedSuperclass
abstract class BaseEntity<E, D>
    where E: BaseEntity<E, D>, D: BaseDTO<D,E>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    abstract fun toDTO(): D
}