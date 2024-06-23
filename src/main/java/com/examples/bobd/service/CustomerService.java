package com.examples.bobd.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.examples.bobd.model.Customer;
import com.examples.bobd.repository.CompanyRepository;
import com.examples.bobd.repository.CustomerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CustomerService {

	@Autowired
	private final CustomerRepository repo;
	
	public Optional<Customer> findById(String id) {
		return repo.findById(id);
	}
	
	public List<Customer> findAll() {
		return Collections.emptyList();
//		return repo.findAll().;
	}
	
	public List<Customer> findAll(Pageable pageable) {
		return repo.findAll(pageable).toList();
	}

    public Customer create(Customer customer){
        return repo.save(customer);
    }
    
	public Customer update(Customer customer) {

		return findById(customer.getId()).map(c -> {
			Customer newcustomer = Customer.builder()
					.id(c.getId())
					.firstName(customer.getFirstName())
					.lastName(customer.getLastName())
					.companyName(customer.getCompanyName())
					.build();
			return repo.save(newcustomer);
		}).orElseThrow(() -> new RuntimeException("Customer not found id=" + customer.getId()));
	}
	
	public void delete(String id) {
		repo.deleteById(id);
	}
	
	public List<Customer> findByFirstName(String firstName) {
		return repo.findByFirstNameIgnoreCase(firstName);
	}
	
	public List<Customer> findByLastName(String lastName) {
		return repo.findByLastNameIgnoreCase(lastName);
	}
	
	public List<Customer> findByName(String firstName, String lastName) {
		return repo.findByFirstNameOrLastNameIgnoreCase(firstName, lastName);
	}

	public List<Customer> findByCompanyName(String pathVariable) {
		return repo.findByCompanyNameIgnoreCase(pathVariable);
	}
}