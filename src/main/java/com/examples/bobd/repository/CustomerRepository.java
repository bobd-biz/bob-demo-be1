package com.examples.bobd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.examples.bobd.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, String>, PagingAndSortingRepository<Customer, String> {

	  List<Customer> findByLastNameIgnoreCase(String lastName);
	  Page<Customer> findByLastNameIgnoreCase(String lastName, Pageable pageable);
	  List<Customer> findByFirstNameIgnoreCase(String firstName);
	  Page<Customer> findByFirstNameIgnoreCase(String firstName, Pageable pageable);
	  List<Customer> findByFirstNameAndLastNameIgnoreCase(String firstName, String lastName);
	  Page<Customer> findByFirstNameAndLastNameIgnoreCase(String firstName, String lastName, Pageable pageable);

	  Optional<Customer> findById(String id);
}
