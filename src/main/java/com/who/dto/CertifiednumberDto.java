package com.who.dto;

import com.who.domain.entity.CertifiedEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CertifiednumberDto {

	private Long id;
	private String email;
	private String number;
	
	public CertifiedEntity toEntity() {
		
		CertifiedEntity certifiedEntity = CertifiedEntity.builder()
                .id(id)
                .email(email)
                .number(number)
                .build();
        return certifiedEntity;		
	}
	
	@Builder
    public CertifiednumberDto(Long id, String email, String number) {
    	this.id = id;
        this.email = email;
        this.number = number;
    }
	
}