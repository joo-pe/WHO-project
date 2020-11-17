package com.who.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.who.domain.entity.GuestEntity;

public interface UserRepository extends JpaRepository<GuestEntity, Long>{
	
	// 소셜 로그인으로 반환되는 값 중 
    // email을 통해 이미 생성된 사용자인지, 첫가입하는 사용자인지
    // 판단하기 위한 메서드
    Optional<GuestEntity> findByEmail(String email);

}
