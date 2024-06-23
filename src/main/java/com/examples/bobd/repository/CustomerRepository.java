package com.examples.bobd.repository;

import java.util.List;
import java.util.Optional;

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
}
