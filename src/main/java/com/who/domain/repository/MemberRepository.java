package com.who.domain.repository;

import com.who.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	
    Optional<MemberEntity> findById(String userId);
}
