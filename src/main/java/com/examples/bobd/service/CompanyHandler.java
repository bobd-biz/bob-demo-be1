package com.examples.bobd.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.examples.bobd.model.Company;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class CompanyHandler {

	private final CompanyService service;
	
	public Mono<ServerResponse> findAll(ServerRequest request) {
		log.info("findAll request={}", request);
		var response = ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromProducer(service.findAll(), Company.class));
		return response;
	}
	
	public Mono<ServerResponse> findById(ServerRequest request) {
		log.info("findById request={}", request);
		log.info("id={}", Long.valueOf(request.pathVariable("id")));
		
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
                        .body(BodyInserters.fromProducer(service.save(company), Company.class)))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }
	
	public Mono<ServerResponse> update(ServerRequest request) {
        return request.bodyToMono(Company.class)
                .flatMap(company -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromProducer(service.update(company), Company.class))
                .switchIfEmpty(ServerResponse.notFound().build()));
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
