package com.examples.bobd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

import com.examples.bobd.model.Company;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CompanyRepository extends ReactiveCrudRepository<Company, Long>, 
//ReactiveSortingRepository<Company, Long>, 
PagingAndSortingRepository<Company, Long>
{

	Optional<Company> getReferenceById(Long id);
	Flux<Company> findAll();
	Page<Company> findAll(Pageable pageable);
	List<Company> findByCompanyNameIgnoreCase(String companyName);
	Page<Company> findByCompanyNameIgnoreCase(String companyName, Pageable pageable);
	
	
	Mono<Company> save(Company company);
	Mono<Void> deleteById(Long id);

}
