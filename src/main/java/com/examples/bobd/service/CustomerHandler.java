package com.examples.bobd.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.examples.bobd.model.Customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerHandler {

	public static final String CUSTOMER_ID = "id";
	public static final String COMPANY_NAME = "company";
	public static final String FIRST_NAME = "first";
	public static final String LAST_NAME = "last";
	
	private static final Map<String, String> ACCEPTED_FIELDS = 
			Map.of(COMPANY_NAME, "companyName", FIRST_NAME, "firstName", LAST_NAME, "lastName");
	
	private final CustomerService service;
	
	public Mono<ServerResponse> findAll(ServerRequest request) {
		log.info("findAll request={}", request);
		Optional<Pageable> pageable = Extractors.extractPageable(request);
		Optional<Sort> sort = Extractors.extractSort(request, ACCEPTED_FIELDS);
		log.info("pageable={}, sort={}", pageable, sort);
		
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromProducer(service.findAll(pageable, sort), Customer.class));
	}
	
	public Mono<ServerResponse> getById(ServerRequest request) {
		return service.findById(String.valueOf(request.pathVariable(CUSTOMER_ID)))
			.flatMap(customer -> ServerResponse.ok()
	                .contentType(MediaType.APPLICATION_JSON)
	                .bodyValue(customer))
	        .switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> create(ServerRequest request) {
//		request.bodyToMono(Customer.class).subscribe(cust -> log.info("Create: Customer {}", cust));
		log.info("Create: Customer {}", request);
        return request.bodyToMono(Customer.class)
        		.onErrorResume(e -> Mono.error(new RuntimeException("Error: " + e.getMessage())))
                .flatMap(customer -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromProducer(service.save(Customer.builder()
                        		.id(createId())
                        		.firstName(customer.getFirstName())
                        		.lastName(customer.getLastName())
                        		.companyName(customer.getCompanyName())
                        		.build()), Customer.class)));
    }

	private String createId() {
		return UUID.randomUUID().toString();
	}
	
	public Mono<ServerResponse> update(ServerRequest request) {
		
		Mono<Customer> cust =  request.bodyToMono(Customer.class);
		log.info("Update: Customer {}", cust);
		
        return request.bodyToMono(Customer.class)
            .flatMap(customer -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromProducer(service.update(customer), Customer.class))
                    .log("Update: Customer updated id=" + customer.getId()));
	}
	
	public Mono<ServerResponse> delete(ServerRequest request) {
		service.delete(String.valueOf(request.pathVariable(CUSTOMER_ID)));
		return ServerResponse.ok().build();
	}
	
	public Mono<ServerResponse> findByCompanyName(ServerRequest request) {
		String name = request.queryParam(COMPANY_NAME).orElseThrow(() -> new RuntimeException("Company name is required"));
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromProducer(service.findByCompanyName(name), Customer.class));
	}
	
	public Mono<ServerResponse> findByFirstName(ServerRequest request) {
		String name = request.queryParam(FIRST_NAME).orElseThrow(() -> new RuntimeException("First name is required"));
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromProducer(service.findByFirstName(name), Customer.class));
	}
	
	public Mono<ServerResponse> findByLastName(ServerRequest request) {
		String name = request.queryParam(LAST_NAME).orElseThrow(() -> new RuntimeException("Last name is required"));
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromProducer(service.findByLastName(name), Customer.class));
	}
	
	public Mono<ServerResponse> findByFirstNameOrLastName(ServerRequest request) {
		String firstName = request.queryParam(FIRST_NAME).orElseThrow(() -> new RuntimeException("First name is required"));
		String lastName = request.queryParam(LAST_NAME).orElseThrow(() -> new RuntimeException("Last name is required"));
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromProducer(service.findByName(firstName, lastName), Customer.class));
	}
}
