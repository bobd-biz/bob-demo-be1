package com.examples.bobd.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.examples.bobd.model.Customer;
import com.examples.bobd.repository.CustomerRepository;

import reactor.test.StepVerifier;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        Customer customer = new Customer("test1", "First1", "Last1", "Test Company 1");
        when(customerRepository.findById("test1")).thenReturn(Optional.of(customer));

        StepVerifier.create(customerService.findById("test1"))
                .expectNextMatches(c -> c.getId().equals("test1"))
                .verifyComplete();
    }

    @Test
    public void testFindByIdNotFound() {
        when(customerRepository.findById("nonexistent")).thenReturn(Optional.empty());

        StepVerifier.create(customerService.findById("nonexistent"))
                .verifyComplete(); // Verify that no Customer is emitted
    }

@Test
    public void testFindAll() {
        Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
        Customer customer2 = new Customer("test2", "First2", "Last2", "Test Company 2");
        when(customerRepository.findAll()).thenReturn( Arrays.asList(customer1, customer2));

        StepVerifier.create(customerService.findAll())
                .expectNextMatches(c -> c.getId().equals("test1"))
                .expectNextMatches(c -> c.getId().equals("test2"))
                .verifyComplete();
    }


	@Test
	public void testSave() {
	    Customer customer = new Customer("test3", "First3", "Last3", "Test Company 3");
	    when(customerRepository.save(any(Customer.class))).thenReturn(customer);
	
	    StepVerifier.create(customerService.save(customer))
	            .expectNextMatches(c ->  c.getId().equals("test3"))
	            .verifyComplete();
	}
	
	// Add more tests for other methods in the CustomerService class
}
