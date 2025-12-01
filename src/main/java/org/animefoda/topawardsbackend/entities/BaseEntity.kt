package org.animefoda.topawardsbackend.entities

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity<D: BaseDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public var id: Int? = null

    abstract fun toDTO(): D

}