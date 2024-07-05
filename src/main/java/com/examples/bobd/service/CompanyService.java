package com.examples.bobd.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.examples.bobd.model.Company;
import com.examples.bobd.repository.CompanyRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {

	private final CompanyRepository repo;
	
	public Mono<Company> findById(Long id) {
		return Mono.justOrEmpty(repo.findById(id));
	}

	public Flux<Company> findAll() {
		return Flux.fromIterable(repo.findAll());
	}
	
	public Flux<Company> findAll(Optional<Pageable> pageable, Optional<Sort> sort) {
			
		return pageable.isPresent() ? 
				Flux.fromIterable(repo.findAll(pageable.get())) :
				sort.isPresent() ? 
						Flux.fromIterable(repo.findAll(sort.get())) : 
						Flux.fromIterable(repo.findAll());
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
	
	public Mono<Company> findByName(String name, Optional<Sort> sort) {
		
		return sort.isPresent() ? 
						Mono.justOrEmpty(repo.findByCompanyNameIgnoreCase(name, sort.get())) : 
						Mono.justOrEmpty(repo.findByCompanyNameIgnoreCase(name));
	}
}