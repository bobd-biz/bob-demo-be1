package com.examples.bobd.repository;

import java.util.List;

import org.springframework.data.repository.ListPagingAndSortingRepository;

import com.examples.bobd.model.Company;

public interface CompanyRepository extends ListPagingAndSortingRepository<Company, Long> {

	List<Company> findAll();
	Company getReferenceById(Long id);

//	@Override
//	Page<Company> findAll(Pageable pageable);
//
//	@Override
//	List<Company> findAll(Sort sort);

}
