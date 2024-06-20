package com.examples.bobd.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.examples.bobd.model.Company;

public interface CompanyRepository extends ListCrudRepository<Company, Long>, 
				CrudRepository<Company, Long>, PagingAndSortingRepository<Company, Long> {

	Optional<Company> findByCompanyNameIgnoreCase(String companyName);
}
