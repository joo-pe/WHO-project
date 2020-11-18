package com.who.domain.repository;

import com.who.domain.entity.Sports2Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Sports2Repository extends JpaRepository<Sports2Entity, Long> {
    List<Sports2Entity> findByTitleContaining(String keyword);
}
