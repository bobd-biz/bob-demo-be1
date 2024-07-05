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

	  List<Customer> findByLastNameIgnoreCase(String lastName);
	  List<Customer> findByFirstNameIgnoreCase(String firstName);
	  List<Customer> findByFirstNameOrLastNameIgnoreCase(String firstName, String lastName);
	  List<Customer> findByCompanyNameIgnoreCase(String companyName);
	  
	  List<Customer> findByLastNameIgnoreCase(String lastName, Pageable pageable);
	  List<Customer> findByFirstNameIgnoreCase(String firstName, Pageable pageable);
	  List<Customer> findByFirstNameOrLastNameIgnoreCase(String firstName, String lastName, Pageable pageable);
	  List<Customer> findByCompanyNameIgnoreCase(String companyName, Pageable pageable);
	  
	  List<Customer> findByLastNameIgnoreCase(String lastName, Sort sort);
	  List<Customer> findByFirstNameIgnoreCase(String firstName, Sort sort);
	  List<Customer> findByFirstNameOrLastNameIgnoreCase(String firstName, String lastName, Sort sort);
	  List<Customer> findByCompanyNameIgnoreCase(String companyName, Sort sort);

//		List<T> findAll(Sort sort);
//		Iterable<T> findAll(Sort sort);
//		Page<T> findAll(Pageable pageable);
}
