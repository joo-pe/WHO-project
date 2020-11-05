package com.who.domain.repository;

import com.who.domain.entity.SportsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SportsRepository extends JpaRepository<SportsEntity, Long> {
    List<SportsEntity> findByTitleContaining(String keyword);
}
