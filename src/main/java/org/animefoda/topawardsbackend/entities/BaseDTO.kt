package org.animefoda.topawardsbackend.entities

import java.io.Serializable

abstract class BaseDTO<D,E>(
    open val id: Int? = null
): Serializable
    where D: BaseDTO<D,E>,E : BaseEntity<E,D>{

    abstract fun toEntity(): E
}