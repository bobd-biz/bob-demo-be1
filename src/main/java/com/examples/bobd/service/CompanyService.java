package com.examples.bobd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.examples.bobd.model.Company;
import com.examples.bobd.repository.CompanyRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {

	@Autowired
	private final CompanyRepository repo;
	
	public Optional<Company> findById(Long id) {
		return repo.findById(id);
	}

//	public List<Company> findAll() {
//		return repo.findAll();
//	}

	public Flux<Company> findAll() {
		return Flux.fromIterable(repo.findAll());
//				.subscribeOn(Schedulers.boundedElastic());
	}
	
	public List<Company> findAll(Pageable pageable) {
		return repo.findAll(pageable).toList();
	}

//	public Company create(Company company){
//		return repo.save(company);
//	}
	public Mono<Company> create(Company company){
		return Mono.fromCallable(() -> repo.save(company))
				.subscribeOn(Schedulers.boundedElastic());
	}

	public Company update(Company company) {

		return findById(company.getId()).map(c -> {
			Company newcompany = Company.builder().id(c.getId()).companyName(company.getCompanyName()).build();
			return repo.save(newcompany);
		}).orElseThrow(() -> new RuntimeException("Company not found id=" + company.getId()));
	}
	
	public void delete(Long id) {
		repo.deleteById(id);
	}
	
	public Optional<Company> findByName(String name) {
		return repo.findByCompanyNameIgnoreCase(name);
	}
}