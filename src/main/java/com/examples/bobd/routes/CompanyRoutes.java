package com.examples.bobd.routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import java.util.function.Predicate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.examples.bobd.service.CompanyHandler;

import lombok.extern.slf4j.Slf4j;

@Configuration(proxyBeanMethods = false)
@Slf4j
public class CompanyRoutes {
	
	private static final String PREFIX = "/companies";
	
	private static final Predicate<String> notEmpty = name -> name != null && !name.isEmpty();

  @Bean
  public RouterFunction<ServerResponse> companyRoutesBean(CompanyHandler handler) {
	  return RouterFunctions.route()
			    .path(PREFIX, builder -> builder
			    		.before(request -> {
			    			log.info("Companies {} {}", request.method(), request.path());
			    			return request;
			    		})
			        .GET("/{id}", accept(MediaType.APPLICATION_JSON), handler::findById)
			        .GET(hasNameParam().and(accept(MediaType.APPLICATION_JSON)), handler::findByName)
			        .GET(accept(MediaType.APPLICATION_JSON), handler::findAll)
			        
			        .POST(accept(MediaType.APPLICATION_JSON), handler::create)
			        .PUT(accept(MediaType.APPLICATION_JSON), handler::update)
			        .DELETE("/{id}", accept(MediaType.APPLICATION_JSON), handler::delete))
			    .build();
  }
  
	public RequestPredicate hasNameParam() {
		
		return RequestPredicates.queryParam("name", notEmpty);
	}
}