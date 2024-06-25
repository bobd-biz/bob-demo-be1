package com.examples.bobd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.examples.bobd.model.Customer;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerHandler {

	@Autowired
	CustomerService service;
	
	public Mono<ServerResponse> findAll(ServerRequest request) {
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromProducer(service.findAll(), Customer.class));
	}
	
	public Mono<ServerResponse> getById(ServerRequest request) {
		return service.findById(String.valueOf(request.pathVariable("id")))
			.flatMap(customer -> ServerResponse.ok()
	                .contentType(MediaType.APPLICATION_JSON)
	                .bodyValue(customer))
	        .switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Customer.class)
                .flatMap(customer -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(service.create(customer))));
    }
	
	public Mono<ServerResponse> update(ServerRequest request) {
		        return request.bodyToMono(Customer.class)
                .flatMap(customer -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(service.update(customer))));
	}
	
	public Mono<ServerResponse> delete(ServerRequest request) {
		service.delete(String.valueOf(request.pathVariable("id")));
		return ServerResponse.ok().build();
	}
	
	public Mono<ServerResponse> findByCompanyName(ServerRequest request) {
		List<Customer> customers = service.findByCompanyName(request.pathVariable("companyname"));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(customers));
	}
	
	public Mono<ServerResponse> findByFirstName(ServerRequest request) {
		List<Customer> customers = service.findByFirstName(request.pathVariable("firstname"));
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(customers));
	}
	
	public Mono<ServerResponse> findByLastName(ServerRequest request) {
		List<Customer> customers = service.findByLastName(request.pathVariable("lastname"));
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(customers));
	}
	
	public Mono<ServerResponse> findByFirstNameOrLastName(ServerRequest request) {
		String firstName = request.pathVariable("firstname");
		String lastName = request.pathVariable("lastname");
		List<Customer> customers = service.findByName(firstName, lastName);
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(customers));
	}
}
