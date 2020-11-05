package com.who.domain.repository;

import com.who.domain.entity.PerformanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceRepository extends JpaRepository<PerformanceEntity, Long> {
    List<PerformanceEntity> findByTitleContaining(String keyword);
}
