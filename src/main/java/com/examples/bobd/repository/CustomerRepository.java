package com.examples.bobd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.ListPagingAndSortingRepository;

import com.examples.bobd.model.Customer;

public interface CustomerRepository extends ListPagingAndSortingRepository<Customer, String> {

	  List<Customer> findByLastName(String lastName);

	  Optional<Customer> findById(String id);
}
