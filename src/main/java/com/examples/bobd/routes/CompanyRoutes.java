package com.examples.bobd.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.examples.bobd.service.CompanyHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class CompanyRoutes {
	
	private static final String PREFIX = "/company";

  @Bean
  public RouterFunction<ServerResponse> route(CompanyHandler handler) {
//	  Mono<String> string = request.body(BodyExtractors.toMono(String.class));BodyExtractors.
//	  RouterFunction<ServerResponse> r2 = RouterFunctions.route()
//			  .
//	  
	  return RouterFunctions.route()
			    .path(PREFIX, builder -> builder
			        .GET("/{id}", accept(MediaType.APPLICATION_JSON), handler::getById)
			        .GET(accept(MediaType.APPLICATION_JSON), handler::findAll)
			        .POST(handler::create))
			    .build();
//	  return RouterFunctions
//			  .route(GET(PREFIX + "/hello").and(accept(MediaType.APPLICATION_JSON)), handler::hello);
  }
}