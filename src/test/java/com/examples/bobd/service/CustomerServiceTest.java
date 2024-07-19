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
	
	@Test
	public void testUpdate() {
		Customer customer = new Customer("test4", "First4", "Last4", "Test Company 4");
		when(customerRepository.findById("test4")).thenReturn(Optional.of(customer));
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);

		StepVerifier.create(customerService.update(customer)).expectNextMatches(c -> c.getId().equals("test4"))
				.verifyComplete();
	}
	
	@Test
	public void testUpdateNotFound() {
        Customer customer =	new Customer("test5", "First5", "Last5", "Test Company 5");
        when(customerRepository.findById("test5")).thenReturn(Optional.empty());
        StepVerifier.create(customerService.update(customer))
                .verifyErrorMessage("Update: Customer not found id=test5");
	}
	
	@Test
	public void testDelete() {
		customerService.delete("test6");
	}
	
	@Test
	public void testFindByCompanyName() {
		Customer customer = new Customer("test7", "First7", "Last7", "Test Company 7");
		when(customerRepository.findByCompanyNameIgnoreCaseContains("Test Company 7")).thenReturn(Arrays.asList(customer));

		StepVerifier.create(customerService.findByCompanyName("Test Company 7"))
				.expectNextMatches(c -> c.getId().equals("test7")).verifyComplete();
	}
	
	@Test
	public void testFindByFirstName() {
        Customer customer =	new Customer("test8", "First8", "Last8", "Test Company 8");
        when(customerRepository.findByFirstNameIgnoreCaseContains("First8")).thenReturn(Arrays.asList(customer));
        StepVerifier.create(customerService.findByFirstName("First8"))
                .expectNextMatches(c -> c.getId().equals("test8")).verifyComplete();
	}
	
	@Test
	public void testFindByLastName() {
        Customer customer =	new Customer("test9", "First9", "Last9", "Test Company 9");
        when(customerRepository.findByLastNameIgnoreCaseContains("Last9")).thenReturn(Arrays.asList(customer));
        StepVerifier.create(customerService.findByLastName("Last9"))
                .expectNextMatches(c -> c.getId().equals("test9")).verifyComplete();
	}
	
	@Test
	public void testFindByFirstNameOrLastName() {
		Customer customer = new Customer("test10", "First10", "Last10", "Test Company 10");
		when(customerRepository.findByAllIgnoreCaseFirstNameContainsOrLastNameContains("First10", "Last10"))
				.thenReturn(Arrays.asList(customer));
		StepVerifier.create(customerService.findByName("First10", "Last10"))
				.expectNextMatches(c -> c.getId().equals("test10")).verifyComplete();
	}
	
	@Test
	public void testFindByFirstNameOrLastNameNotFound() {
		when(customerRepository.findByAllIgnoreCaseFirstNameContainsOrLastNameContains("First11", "Last11"))
				.thenReturn(Arrays.asList());
		StepVerifier.create(customerService.findByName("First11", "Last11")).verifyComplete();
	}
	
	@Test
	public void testFindByFirstNameOrLastNameFirstNameOnly() {
        Customer customer = new	Customer("test12", "First12", "Last12", "Test Company 12");
        when(customerRepository.findByAllIgnoreCaseFirstNameContainsOrLastNameContains("First12", "Last12"))
                .thenReturn(Arrays.asList(customer));
        StepVerifier.create(customerService.findByName("First12", "Last12"))
                .expectNextMatches(c -> c.getId().equals("test12")).verifyComplete();
	}
	
	@Test
	public void testFindByFirstNameOrLastNameLastNameOnly() {
        Customer customer =	new Customer("test13", "First13", "Last13", "Test Company 13");
        when(customerRepository.findByAllIgnoreCaseFirstNameContainsOrLastNameContains("First13", "Last13"))
                .thenReturn(Arrays.asList(customer));
        StepVerifier.create(customerService.findByName("First13", "Last13"))
                .expectNextMatches(c -> c.getId().equals("test13")).verifyComplete();
	}
	
	@Test
	public void testFindByFirstNameOrLastNameMultiple() {
        Customer customer1 = new Customer("test14", "First14", "Last14", "Test Company 14");
        Customer customer2 = new Customer("test15", "First15", "Last15", "Test Company 15");
        when(customerRepository.findByAllIgnoreCaseFirstNameContainsOrLastNameContains("First14", "Last14"))
                .thenReturn(Arrays.asList(customer1, customer2));
        StepVerifier.create(customerService.findByName("First14", "Last14"))
                .expectNextMatches(c -> c.getId().equals("test14"))
                .expectNextMatches(c -> c.getId().equals("test15")).verifyComplete();
	}
	
	@Test
	public void testFindByFirstNameOrLastNameMultipleNotFound() {
		when(customerRepository.findByAllIgnoreCaseFirstNameContainsOrLastNameContains("First16", "Last16")).thenReturn(Arrays.asList());
		StepVerifier.create(customerService.findByName("First16", "Last16")).verifyComplete();
	}
	
	@Test
	public void testFindByFirstNameOrLastNameMultipleFirstNameOnly() {
		Customer customer = new Customer("test17", "First17", "Last17", "Test Company 17");
		when(customerRepository.findByAllIgnoreCaseFirstNameContainsOrLastNameContains("First17", "Last17"))
				.thenReturn(Arrays.asList(customer));
		StepVerifier.create(customerService.findByName("First17", "Last17"))
				.expectNextMatches(c -> c.getId().equals("test17")).verifyComplete();
	}
}
