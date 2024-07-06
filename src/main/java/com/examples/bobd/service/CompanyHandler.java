package com.examples.bobd.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

	private static final String COMPANY_NAME = "name";
	private static final Map<String, String> COMPANY_FIELDS = Map.of(COMPANY_NAME, "companyName");
	
	private final CompanyService service;
	
	public Mono<ServerResponse> findAll(ServerRequest request) {
		log.info("findAll request={}", request);
		Optional<Pageable> pageable = Extractors.extractPageable(request);
		Optional<Sort> sort = Extractors.extractSort(request, COMPANY_FIELDS);
		log.info("pageable={}, sort={}", pageable, sort);
		
		var response = ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromProducer(service.findAll(pageable, sort), Company.class));
		return response;
	}
	
	public Mono<ServerResponse> findById(ServerRequest request) {
		log.info("findById request={}", request);
		log.info("id={}", Long.valueOf(request.pathVariable("id")));
		
		return service.findById(Long.valueOf(request.pathVariable("id")))
				.flatMap(customer -> ServerResponse.ok()
		                .contentType(MediaType.APPLICATION_JSON)
		                .bodyValue(customer))
		        .switchIfEmpty(Responses.notFound("Company not found"));
	}
	
	public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Company.class)
                .flatMap(company -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromProducer(service.save(company), Company.class)))
                .switchIfEmpty(Responses.badRequest("Company not found"));
    }
	
	public Mono<ServerResponse> update(ServerRequest request) {
        return request.bodyToMono(Company.class)
                .flatMap(company -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromProducer(service.update(company), Company.class))
                .switchIfEmpty(Responses.notFound("Company not updated")));
	}
	
	public Mono<ServerResponse> delete(ServerRequest request) {
		service.delete(Long.valueOf(request.pathVariable("id")));
		return ServerResponse.ok().build();
	}
	
	public Mono<ServerResponse> findByName(ServerRequest request) {
		log.info("findByName request={}", request);
		String name = request.queryParam(COMPANY_NAME).orElseThrow(() -> new RuntimeException("Company name is required"));
		return service.findByName(name)
				.flatMap(customer -> ServerResponse.ok()
		                .contentType(MediaType.APPLICATION_JSON)
		                .bodyValue(customer))
		        .switchIfEmpty(Responses.badRequest("Company not found"));
	}
}
