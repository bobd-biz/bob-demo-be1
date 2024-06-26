package com.examples.bobd.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.examples.bobd.model.Company;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CompanyHandler {

	private final CompanyService service;
	
	public Mono<ServerResponse> findAll(ServerRequest request) {
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromProducer(service.findAll(), Company.class));
	}
	
	public Mono<ServerResponse> hello(ServerRequest request) {
      Company response = Company.builder()
    		  .id(1L)
    		  .companyName("test company")
    		  .build();
	  return ServerResponse.ok()
			  .contentType(MediaType.APPLICATION_JSON)
			  .body(BodyInserters.fromValue(response));
	}
	
	public Mono<ServerResponse> findById(ServerRequest request) {
		return service.findById(Long.valueOf(request.pathVariable("id")))
				.flatMap(customer -> ServerResponse.ok()
		                .contentType(MediaType.APPLICATION_JSON)
		                .bodyValue(customer))
		        .switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Company.class)
                .flatMap(company -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(service.create(company))))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }
	
	public Mono<ServerResponse> update(ServerRequest request) {
        return request.bodyToMono(Company.class)
                .flatMap(company -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(service.update(company))))
                .switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> delete(ServerRequest request) {
		service.delete(Long.valueOf(request.pathVariable("id")));
		return ServerResponse.ok().build();
	}
	
	public Mono<ServerResponse> findByName(ServerRequest request) {
		return service.findByName(request.pathVariable("name"))
				.flatMap(customer -> ServerResponse.ok()
		                .contentType(MediaType.APPLICATION_JSON)
		                .bodyValue(customer))
		        .switchIfEmpty(ServerResponse.notFound().build());
	}
}
