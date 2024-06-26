package com.examples.bobd.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.examples.bobd.model.Customer;
import com.examples.bobd.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository repo;
	
	public Mono<Customer> findById(String id) {
		return Mono.justOrEmpty(repo.findById(id));
	}
	
	public Flux<Customer> findAll() {
		return Flux.fromIterable(repo.findAll());
	}
	
	public Flux<Customer> findAll(Pageable pageable) {
		return Flux.fromIterable(repo.findAll(pageable));
	}

    public Mono<Customer> save(Customer customer){
		return Mono.fromCallable(() -> repo.save(customer))
				.subscribeOn(Schedulers.boundedElastic());
    }
    
	public Mono<Customer> update(Customer customer) {
		return findById(customer.getId()).map(c -> {
			Customer newcustomer = Customer.builder()
					.id(c.getId())
					.firstName(customer.getFirstName())
					.lastName(customer.getLastName())
					.companyName(customer.getCompanyName())
					.build();
			return repo.save(newcustomer);
		})
		.switchIfEmpty(Mono.error(new RuntimeException("Update: Customer not found id=" + customer.getId())));
	}
	
	public void delete(String id) {
		repo.deleteById(id);
	}
	
	public Flux<Customer> findByFirstName(String firstName) {
		return Flux.fromIterable(repo.findByFirstNameIgnoreCase(firstName));
	}
	
	public Flux<Customer> findByLastName(String lastName) {
		return Flux.fromIterable(repo.findByLastNameIgnoreCase(lastName));
	}
	
	public Flux<Customer> findByName(String firstName, String lastName) {
		return Flux.fromIterable(repo.findByFirstNameOrLastNameIgnoreCase(firstName, lastName));
	}

	public Flux<Customer> findByCompanyName(String pathVariable) {
		return Flux.fromIterable(repo.findByCompanyNameIgnoreCase(pathVariable));
	}
}