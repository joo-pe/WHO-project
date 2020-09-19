package com.who.domain.repository;

import com.who.domain.entity.MemberEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String email);
<<<<<<< HEAD
    
=======
>>>>>>> 971909878b3efee5999b8584ff37d1442e07acec
}

