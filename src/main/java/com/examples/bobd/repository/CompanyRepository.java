package com.examples.bobd.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.examples.bobd.model.Company;

public interface CompanyRepository extends ListCrudRepository<Company, Long>, 
				CrudRepository<Company, Long>, PagingAndSortingRepository<Company, Long> {

	Optional<Company> findByCompanyNameIgnoreCase(String companyName);
	Optional<Company> findByCompanyNameIgnoreCase(String companyName, Sort sort);

//	List<T> findAll(Sort sort);
//	Iterable<T> findAll(Sort sort);
//	Page<T> findAll(Pageable pageable);
}
