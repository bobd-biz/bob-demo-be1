package com.examples.bobd.routes;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.examples.bobd.model.Customer;
import com.examples.bobd.service.CustomerHandler;
import com.examples.bobd.service.CustomerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CustomerRoutesTest {

    private static final String PREFIX = "/customers";
    
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
        when(customerService.findAll(any(), any())).thenReturn(Flux.just(customer1, customer2));

        webTestClient.get().uri(PREFIX)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Customer.class)
                .hasSize(2);
    }

    @Test
    public void testGetCustomerById() {
        Customer customer = new Customer("test1", "First1", "Last1", "Test Company 1");
        when(customerService.findById("test1")).thenReturn(Mono.just(customer));

        webTestClient.get().uri(PREFIX + "/test1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class);
    }

    @Test
    @Disabled
	public void testCreateCustomer() {
		Customer customer = new Customer("test1", "First1", "Last1", "Test Company 1");
		Customer updatedCustomer = new Customer("test1", "First1A", "Last1A", "Test Company 1");
		when(customerService.findById("test1")).thenReturn(Mono.just(customer));
		when(customerService.save(customer)).thenReturn(Mono.just(customer));

		try {
			webTestClient.post().uri(PREFIX).contentType(MediaType.APPLICATION_JSON)
					.body(Mono.just(customer), Customer.class).exchange()
					.expectStatus().isCreated()
					.expectBody(Customer.class)
					.isEqualTo(updatedCustomer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	@Test
	public void testUpdateCustomer() {
		Customer customer = new Customer("test1", "First1", "Last1", "Test Company 1");
		when(customerService.update(customer)).thenReturn(Mono.just(customer));

		webTestClient.put().uri(PREFIX).contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(customer), Customer.class)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Customer.class);
	}

	@Test
	public void testDeleteCustomer() {
		webTestClient.delete().uri(PREFIX + "/test1")
			.exchange()
			.expectStatus().isOk();
	}

	@Test
	@Disabled
	public void testFindByCompanyName() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		Customer customer2 = new Customer("test2", "First2", "Last2", "Test Company 2");
		when(customerService.findByCompanyName("Test Company 1")).thenReturn(Flux.just(customer1, customer2));

		webTestClient.get().uri(PREFIX + "?company=Test%20Company%201")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody(Customer.class);
	}
	
	@Test
	public void testFindByFirstName() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		when(customerService.findByFirstName("First1")).thenReturn(Flux.just(customer1));

		webTestClient.get().uri(PREFIX + "?first=First1")
				.accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isOk()
				.expectBodyList(Customer.class);
	}
	
	@Test
	public void testFindByLastName() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		when(customerService.findByLastName("Last1")).thenReturn(Flux.just(customer1));

		webTestClient.get().uri(PREFIX + "?last=Last1")
			.accept(MediaType.APPLICATION_JSON).exchange()
			.expectStatus().isOk()
			.expectBodyList(Customer.class);
	}
	
	@Test
	public void testFindByFirstNameAndLastName() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		when(customerService.findByName("First1", "Last1")).thenReturn(Flux.just(customer1));

		webTestClient.get().uri(PREFIX + "?first=First1&last=Last1").accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Customer.class);
	}
	
	@Test
	public void testFindByFirstNameOrLastName() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		when(customerService.findByName("First1", "Last1")).thenReturn(Flux.just(customer1));

		webTestClient.get().uri(PREFIX + "?first=First1&last=Last1").accept(MediaType.APPLICATION_JSON)
				.exchange().expectStatus().isOk()
				.expectBodyList(Customer.class);
	}
	
	@Test
	@Disabled
	public void testFindByFirstNameOrLastNameNotFound() {
		webTestClient.get().uri(PREFIX + "?first=First1").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isNotFound();
	}
	
	@Test
	@Disabled
	public void testFindByFirstNameOrLastNameEmpty() {
		webTestClient.get().uri(PREFIX + "?name").accept(MediaType.APPLICATION_JSON).exchange()
			.expectStatus()
			.isNotFound();
	}	
	
	@Test
	@Disabled
	public void testFindByFirstNameOrLastNameEmptyLastName() {
		webTestClient.get().uri(PREFIX + "?first=First1&last=").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isNotFound();
	}
	
	@Test
	public void testFindByFirstNameOrLastNameEmptyFirstName() {
		webTestClient.get().uri(PREFIX + "t?first=&last=Last1").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isNotFound();
	}
	
	@Test
	public void testFindByFirstNameOrLastNameMultiple() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		Customer customer2 = new Customer("test2", "First2", "Last2", "Test Company 2");
		when(customerService.findByName("First1", "Last1")).thenReturn(Flux.just(customer1, customer2));

		webTestClient.get().uri(PREFIX + "?first=First1&last=Last1").accept(MediaType.APPLICATION_JSON)
				.exchange().expectStatus().isOk()
				.expectBodyList(Customer.class).hasSize(2);
	}
	
	@Test
	@Disabled
	public void testFindByFirstNameOrLastNameMultipleEmpty() {
        Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
        Customer customer2 = new Customer("test2", "First2", "Last2", "Test Company 2");
        when(customerService.findByName("First1", "Last1")).thenReturn(Flux.just(customer1, customer2));
        
        webTestClient.get().uri(PREFIX).accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isNotFound();
	}
	
	@Test
	@Disabled
	public void testFindByFirstNameOrLastNameMultipleEmptyLastName() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		Customer customer2 = new Customer("test2", "First2", "Last2", "Test Company 2");
		when(customerService.findByName("First1", "Last1")).thenReturn(Flux.just(customer1, customer2));

		webTestClient.get().uri(PREFIX + "?first=First1&last=").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isNotFound();
	}
	
	@Test
	@Disabled
	public void testFindByFirstNameOrLastNameMultipleEmptyFirstName() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		Customer customer2 = new Customer("test2", "First2", "Last2", "Test Company 2");
		when(customerService.findByName("First1", "Last1")).thenReturn(Flux.just(customer1, customer2));

		webTestClient.get().uri(PREFIX + "?first=&last=Last1").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isNotFound();
	}
	
}
