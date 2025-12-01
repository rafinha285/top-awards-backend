package org.animefoda.topawardsbackend.entities.nominee;

import jakarta.persistence.*;
import lombok.Data;
import org.animefoda.topawardsbackend.entities.BaseDTO;
import org.animefoda.topawardsbackend.entities.BaseEntity;
import org.animefoda.topawardsbackend.entities.category.CategoryEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "nominee")
public class NomineeEntity extends BaseEntity<NomineeDTO> {
    @Column
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "nomination",
            joinColumns = @JoinColumn(name = "nominee_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<CategoryEntity> categories = new HashSet<>();

    @Override
    public NomineeDTO toDTO() {
        return new NomineeDTO(id, name);
    }
}
