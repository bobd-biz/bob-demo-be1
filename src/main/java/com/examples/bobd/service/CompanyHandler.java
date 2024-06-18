package com.examples.bobd.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.examples.bobd.model.Company;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CompanyHandler {

  public Mono<ServerResponse> hello(ServerRequest request) {
      Company response = Company.builder()
    		  .companyName("test company")
    		  .build();
	  return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(response));
  }
  
//  public Flux<ServerResponse> findAll(ServerRequest request) {
//	  
//  }
}
