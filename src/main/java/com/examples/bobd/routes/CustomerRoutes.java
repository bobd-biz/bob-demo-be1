package com.examples.bobd.routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.examples.bobd.service.CustomerHandler;

@Configuration(proxyBeanMethods = false)
public class CustomerRoutes {
	
	private static final String PREFIX = "/customers";

  @Bean//("customerRoutes")
  public RouterFunction<ServerResponse> routes(CustomerHandler handler) {
//	  Mono<String> string = request.body(BodyExtractors.toMono(String.class));BodyExtractors.
//	  RouterFunction<ServerResponse> r2 = RouterFunctions.route()
//			  .
//	  
	  return RouterFunctions.route()
			    .path(PREFIX, builder -> builder
			        .GET("/{id}", accept(MediaType.APPLICATION_JSON), handler::getById)
			        .GET(accept(MediaType.APPLICATION_JSON), handler::findAll)
//			        .GET(accept(MediaType.APPLICATION_JSON), handler::q)
			        .POST(accept(MediaType.APPLICATION_JSON), handler::create)
			        .PUT(accept(MediaType.APPLICATION_JSON), handler::update)
			        .DELETE("/{id}", accept(MediaType.APPLICATION_JSON), handler::delete))
			    .build();
//	  return RouterFunctions
//			  .route(GET(PREFIX + "/hello").and(accept(MediaType.APPLICATION_JSON)), handler::hello);
  }
}
