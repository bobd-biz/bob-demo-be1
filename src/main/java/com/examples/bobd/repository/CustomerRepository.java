package com.examples.bobd.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.examples.bobd.model.Customer;

public interface CustomerRepository extends ListPagingAndSortingRepository<Customer, String>, 
                CrudRepository<Customer, String>, PagingAndSortingRepository<Customer, String> {

	  List<Customer> findByLastNameIgnoreCaseContains(String lastName);
	  List<Customer> findByFirstNameIgnoreCaseContains(String firstName);
	  List<Customer> findByAllIgnoreCaseFirstNameContainsOrLastNameContains(String firstName, String lastName);
	  List<Customer> findByCompanyNameIgnoreCaseContains(String companyName);
	  
	  List<Customer> findByLastNameIgnoreCaseContains(String lastName, Pageable pageable);
	  List<Customer> findByFirstNameIgnoreCaseContains(String firstName, Pageable pageable);
	  List<Customer> findByAllIgnoreCaseFirstNameContainsOrLastNameContains(String firstName, String lastName, Pageable pageable);
	  List<Customer> findByCompanyNameIgnoreCaseContains(String companyName, Pageable pageable);
	  
	  List<Customer> findByLastNameIgnoreCaseContains(String lastName, Sort sort);
	  List<Customer> findByFirstNameIgnoreCaseContains(String firstName, Sort sort);
	  List<Customer> findByAllIgnoreCaseFirstNameContainsOrLastNameContains(String firstName, String lastName, Sort sort);
	  List<Customer> findByCompanyNameIgnoreCaseContains(String companyName, Sort sort);

//		List<T> findAll(Sort sort);
//		Iterable<T> findAll(Sort sort);
//		Page<T> findAll(Pageable pageable);
}
