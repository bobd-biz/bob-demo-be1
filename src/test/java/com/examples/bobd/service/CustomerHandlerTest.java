package com.examples.bobd.service;

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
import com.examples.bobd.routes.CustomerRoutes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CustomerHandlerTest {

    @Mock
    private CustomerService customerService;
    
    private final ObjectMapper mapper = new ObjectMapper()
  		  .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @InjectMocks
    private CustomerHandler customerHandler;
    
    private CustomerRoutes customerRoutes;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
//        customerService = new CustomerService(customerRepository);
        customerRoutes = new CustomerRoutes();
        webTestClient = WebTestClient.bindToRouterFunction(customerRoutes.customerRoutesBean(customerHandler)).build();
    }
    
    @Test
    public void testCustomerExtractionFromBody() throws JsonProcessingException {
        Customer customer = new Customer("test1", "First1", "Last1", "Test Company 1");
        String customerString = mapper.writeValueAsString(customer);
        when(customerService.save(null)).thenReturn(Mono.just(customer));
        
        var s = 
        		"""
        		{"lastName": "Last3", "companyName": "Test Company 2", "firstName": "First3"}
        		
        		""";
        var decoded = mapper.readValue(s, Customer.class);
        System.out.println(mapper.writeValueAsString(decoded));
        
		var valid = Mono.just(s)
			.map(str -> {
				try {
					return mapper.readValue(str, Customer.class);
				} catch (JsonProcessingException e) {
					return null;
				}
			});
		
		System.out.println("valid");
		valid.subscribe(System.out::println);
		
		var invalid = Mono.just("invalid")
			.map(str -> {
				try {
					return mapper.readValue(str, Customer.class);
				} catch (JsonProcessingException e) {
					return Mono.error(e);
				}
			});
		
		System.out.println("invalid");
		invalid.subscribe(System.out::println);
		
		var ErrorResponse = Mono.just("invalid").map(str -> {
			try {
				return mapper.readValue(str, Customer.class);
			} catch (JsonProcessingException e) {
				return null;
			}
		}).onErrorReturn(new Customer("error", "error", "error", "error"));
		
		System.out.println("ErrorResponse");
		ErrorResponse.subscribe(System.out::println);
        
//        var response = webTestClient.post().uri("/customers")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(customerString)
//                .retrieve()
//                .bodyToMono(String.class);
//        
//        WebClient webClient = WebClient.create();
//        var responseJson = webClient.get()
//                                       .uri("https://petstore.swagger.io/v2/pet/findByStatus?status=available")
//                                       .retrieve()
//                                       .bodyToMono(String.class);
//        response
//                .expectStatus().isCreated()
//                .expectBody(String.class)
//                .isEqualTo(customerString);
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

    @Test
    @Disabled
	public void testCreateCustomer() {
		Customer customer = new Customer("test1", "First1", "Last1", "Test Company 1");
		when(customerService.save(customer)).thenReturn(Mono.just(customer));

		webTestClient.post().uri("/customers").contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(customer), Customer.class).exchange()
				.expectStatus().isCreated()
				.expectBody(Customer.class);
	}
    
	@Test
	public void testUpdateCustomer() {
		Customer customer = new Customer("test1", "First1", "Last1", "Test Company 1");
		when(customerService.update(customer)).thenReturn(Mono.just(customer));

		webTestClient.put().uri("/customers").contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(customer), Customer.class)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Customer.class);
	}

	@Test
	public void testDeleteCustomer() {
		webTestClient.delete().uri("/customers/test1").exchange()
		.expectStatus().isOk();
	}

	@Test
	@Disabled
	public void testFindByCompanyName() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		Customer customer2 = new Customer("test2", "First2", "Last2", "Test Company 2");
		when(customerService.findByCompanyName("Test Company 1")).thenReturn(Flux.just(customer1, customer2));

		webTestClient.get().uri("/customers?company=Test%20Company 1").accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Customer.class);
	}
	
	@Test
	public void testFindByFirstName() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		when(customerService.findByFirstName("First1")).thenReturn(Flux.just(customer1));

		webTestClient.get().uri("/customers?first=First1").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isOk()
				.expectBodyList(Customer.class);
	}
	
	@Test
	public void testFindByLastName() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		when(customerService.findByLastName("Last1")).thenReturn(Flux.just(customer1));

		webTestClient.get().uri("/customers?last=Last1").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isOk()
				.expectBodyList(Customer.class);
	}
	
	@Test
	public void testFindByFirstNameAndLastName() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		when(customerService.findByName("First1", "Last1")).thenReturn(Flux.just(customer1));

		webTestClient.get().uri("/customers?first=First1&last=Last1").accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Customer.class);
	}
	
	@Test
	public void testFindByFirstNameOrLastName() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		when(customerService.findByName("First1", "Last1")).thenReturn(Flux.just(customer1));

		webTestClient.get().uri("/customers?first=First1&last=Last1").accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Customer.class);
	}
	
	@Test
	@Disabled
	public void testFindByFirstNameOrLastNameNotFound() {
		webTestClient.get().uri("/customers?first=First1").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isNotFound();
	}
	
	@Test
	@Disabled
	public void testFindByFirstNameOrLastNameEmptyLastName() {
		webTestClient.get().uri("/customers?first=First1&last=").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isNotFound();
	}
	
	@Test
	@Disabled
	public void testFindByFirstNameOrLastNameEmptyFirstName() {
		webTestClient.get().uri("/customers?first=&last=Last1").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isNotFound();
	}
	
	@Test
	public void testFindByFirstNameOrLastNameMultiple() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		Customer customer2 = new Customer("test2", "First2", "Last2", "Test Company 2");
		when(customerService.findByName("First1", "Last1")).thenReturn(Flux.just(customer1, customer2));

		webTestClient.get().uri("/customers?first=First1&last=Last1").accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Customer.class);
	}
	
	@Test
	@Disabled
	public void testFindByFirstNameOrLastNameMultipleEmptyLastName() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		Customer customer2 = new Customer("test2", "First2", "Last2", "Test Company 2");
		when(customerService.findByName("First1", "Last1")).thenReturn(Flux.just(customer1, customer2));

		webTestClient.get().uri("/customers?first=First1&last=").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isNotFound();
	}
	
	@Test
	@Disabled
	public void testFindByFirstNameOrLastNameMultipleEmptyFirstName() {
		Customer customer1 = new Customer("test1", "First1", "Last1", "Test Company 1");
		Customer customer2 = new Customer("test2", "First2", "Last2", "Test Company 2");
		when(customerService.findByName("First1", "Last1")).thenReturn(Flux.just(customer1, customer2));

		webTestClient.get().uri("/customers?first=&last=Last1").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isNotFound();
	}
	
}
