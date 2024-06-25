package com.examples.bobd.routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.examples.bobd.service.CompanyHandler;

@Configuration(proxyBeanMethods = false)
public class CompanyRoutes {
	
	private static final String PREFIX = "/companies";

  @Bean
  public RouterFunction<ServerResponse> companyRoutesBean(CompanyHandler handler) {
	  return RouterFunctions.route()
			    .path(PREFIX, builder -> builder
			        .GET(handler::findAll)
			        .GET("/{id}", accept(MediaType.APPLICATION_JSON), handler::getById)
//			        .GET(accept(MediaType.APPLICATION_JSON), handler::q)
			        .POST(accept(MediaType.APPLICATION_JSON), handler::create)
			        .PUT(accept(MediaType.APPLICATION_JSON), handler::update)
			        .DELETE("/{id}", accept(MediaType.APPLICATION_JSON), handler::delete))
			    .build();
  }
}