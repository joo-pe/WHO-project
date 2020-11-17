package com.who.domain.repository;

import com.who.domain.entity.MemberEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import javax.transaction.Transactional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	
    //Optional<MemberEntity> findById(String email);
    
//    MemberEntity findByEmail(String email);

    MemberEntity findMemberEntityById(Long id);
	
    MemberEntity findMemberEntityByEmail(Long id);
    
	MemberEntity findMemberEntityByEmail(String email);
	
	MemberEntity findMemberEntityById(String email);
	
	@Transactional
	@Modifying	// update , delete Query시 @Modifying 어노테이션을 추가
	@Query(value="UPDATE MemberEntity me SET me.password = :password WHERE me.id = :id", nativeQuery=false)
	void update(@Param("id") Long id, @Param("password") String password);
	
}
