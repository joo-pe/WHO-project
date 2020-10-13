package com.who.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.who.dto.CertifiednumberDto;
import com.who.service.FaqService;
import com.who.service.SendEmailService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class CertifiedController {

	private SendEmailService sendEmailService;
	
//	@GetMapping
//	public String dispsendemail(CertifiednumberDto certifiednumberDto) {
//		
//		sendEmailService.
//		
//	}
}
