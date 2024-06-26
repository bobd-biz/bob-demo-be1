package com.examples.bobd.service;

import com.examples.bobd.model.Customer;
import com.examples.bobd.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    public void testFindAll() {
        Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
        Customer customer2 = new Customer("test2", "First2", "Last2", "Test Company 2");
        when(customerRepository.findAll()).thenReturn( Arrays.asList(customer1, customer2));

        StepVerifier.create(customerService.findAll())
                .expectNextMatches(c -> c.getId().equals("test1"))
                .expectNextMatches(c -> c.getId().equals("test2"))
                .verifyComplete();
    }

    // Add more tests for other methods in the CustomerService class
}
