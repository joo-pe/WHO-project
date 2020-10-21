package com.who.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.who.domain.entity.CertifiedEntity;

public interface CertifiedRepository extends JpaRepository<CertifiedEntity, Long> {

	CertifiedEntity findCertifiedEntityByEmail(String email);
	
	CertifiedEntity findCertifiedEntityByNumber(String number);
	
}
