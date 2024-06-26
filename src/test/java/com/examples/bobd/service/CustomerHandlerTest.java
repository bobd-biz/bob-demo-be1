package com.examples.bobd.service;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.examples.bobd.model.Customer;
import com.examples.bobd.routes.CustomerRoutes;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CustomerHandlerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerHandler customerHandler;
    
    private CustomerRoutes customerRoutes;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        customerRoutes = new CustomerRoutes();
        webTestClient = WebTestClient.bindToRouterFunction(customerRoutes.customerRoutesBean(customerHandler)).build();
    }

    @Test
    public void testFindAllCustomers() {
        Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
        Customer customer2 = new Customer("test2", "First2", "Last2", "Test Company 2");
        when(customerService.findAll()).thenReturn(Flux.just(customer1, customer2));

        webTestClient.get().uri("/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Customer.class);
    }

    @Test
    public void testGetCustomerById() {
        Customer customer = new Customer("test1", "First1", "Last1", "Test Company 1");
        when(customerService.findById("test1")).thenReturn(Mono.just(customer));

        webTestClient.get().uri("/customers/test1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class);
    }

    // Add more tests for other methods in the CustomerHandler class
}
