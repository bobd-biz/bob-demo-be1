package com.examples.bobd.controller;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.examples.bobd.model.Company;
import com.examples.bobd.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

//@Controller
//@RequiredArgsConstructor
public class CompanyController {
	
//	private static final String JSON = MediaType.APPLICATION_JSON_VALUE;
//	private static final String PREFIX = "/company";
//	private final CompanyRepository repo;
//	
//	@GetMapping(path = PREFIX + "/{id}", produces = JSON)
//	public Mono<Company> getById(@PathVariable(required = true) Long id) {
////		return Mono.justOrEmpty(repo.getReferenceById(id));Entity<Company> response = null;
////		ResponseEntity.;
////		repo.getReferenceById(id);
//		return repo.findById(id);
//	}

}
