package com.who.domain.repository;

import com.who.domain.entity.FaqEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<FaqEntity, Long> {
    List<FaqEntity> findByTitleContaining(String keyword);
}
