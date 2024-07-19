package com.examples.bobd.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.examples.bobd.model.Company;

public interface CompanyRepository extends ListCrudRepository<Company, Long>, 
				CrudRepository<Company, Long>, PagingAndSortingRepository<Company, Long> {

	List<Company> findByCompanyNameContainsIgnoreCase(String companyName);
	List<Company> findByCompanyNameContainsIgnoreCase(String companyName, Sort sort);

// Inherited:
//	List<T> findAll(Sort sort);
//	Iterable<T> findAll(Sort sort);
//	Page<T> findAll(Pageable pageable);
}
