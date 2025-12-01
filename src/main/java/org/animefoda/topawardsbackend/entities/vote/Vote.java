package org.animefoda.topawardsbackend.entities.vote;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.animefoda.topawardsbackend.entities.BaseEntity;
import org.animefoda.topawardsbackend.entities.category.CategoryEntity;
import org.animefoda.topawardsbackend.entities.nominee.NomineeEntity;
import org.animefoda.topawardsbackend.entities.user.UserEntity;

import java.time.LocalDateTime;

public class Vote extends BaseEntity<VoteDTO> {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "nominee_id", nullable = false)
    private NomineeEntity nominee;

    @Column(name = "voted_at")
    private LocalDateTime votedAt = LocalDateTime.now();

    @Override
    public VoteDTO toDTO() {
        return new VoteDTO(id, user.toDTO(), category.toDTO(), nominee.toDTO(), votedAt);
    }
}
