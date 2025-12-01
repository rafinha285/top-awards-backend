package org.animefoda.topawardsbackend.entities.category;

import jakarta.persistence.*;
import lombok.Data;
import org.animefoda.topawardsbackend.entities.BaseEntity;
import org.animefoda.topawardsbackend.entities.event.EventEntity;
import org.animefoda.topawardsbackend.entities.nominee.NomineeEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
public class CategoryEntity extends BaseEntity<CategoryDTO> {

    @Column(unique = true)
    private String name;

    @Column
    private String description;

    @JoinColumn(name = "event_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EventEntity event;

    @ManyToMany(mappedBy = "categories")
    private Set<NomineeEntity> nominees = new HashSet<>();

    @Override
    public CategoryDTO toDTO() {
        return new CategoryDTO(this.getId(), name, description, event.toDTO(),nominees.stream().map(NomineeEntity::toDTO).toList());
    }
}
