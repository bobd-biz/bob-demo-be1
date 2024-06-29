package com.examples.bobd.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.examples.bobd.model.Company;
import com.examples.bobd.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class CompanyService {

	private final CompanyRepository repo;
	
	public Mono<Company> findById(Long id) {
		return Mono.justOrEmpty(repo.findById(id));
	}

	public Flux<Company> findAll() {
		return Flux.fromIterable(repo.findAll());
	}
	
	public Flux<Company> findAll(Pageable pageable) {
		return Flux.fromIterable(repo.findAll(pageable));
	}

	public Mono<Company> save(Company company){
		return Mono.fromCallable(() -> repo.save(company))
				.subscribeOn(Schedulers.boundedElastic());
	}

	public Mono<Company> update(Company company) {

		Mono<Company> companyMono = findById(company.getId());
		return companyMono.flatMap(c -> {
			Company newcompany = Company.builder().id(c.getId()).companyName(company.getCompanyName()).build();
			return Mono.just(repo.save(newcompany));
		}).switchIfEmpty(Mono.error(new RuntimeException("Company not found id=" + company.getId())));
	}
	
	public void delete(Long id) {
		repo.deleteById(id);
	}
	
	public Mono<Company> findByName(String name) {
		return Mono.justOrEmpty(repo.findByCompanyNameIgnoreCase(name));
	}
}