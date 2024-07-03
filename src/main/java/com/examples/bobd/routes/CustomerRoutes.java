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

import com.examples.bobd.service.CustomerHandler;

import lombok.extern.slf4j.Slf4j;

@Configuration(proxyBeanMethods = false)
@Slf4j
public class CustomerRoutes {
	
	private static final String PREFIX = "/customers";
	private static final String ID = "/{id}";

  @Bean
  public RouterFunction<ServerResponse> customerRoutesBean(CustomerHandler handler) {
	  return RouterFunctions.route()
			    .path(PREFIX, builder -> builder
		    		.before(request -> {
		    			log.info("Customers {} {}", request.method(), request.path());
		    			return request;
		    		})
			        .GET(ID, handler::getById)
			        .GET(firstNameParam( t -> true)
			                .and(lastNameParam( t -> true)), handler::findByFirstNameOrLastName)
			        .GET(firstNameParam( t -> true), handler::findByFirstName)
			        .GET(lastNameParam( t -> true), handler::findByLastName)
			        .GET(companyNameParam( t -> true), handler::findByCompanyName)
			        .GET( handler::findAll)
			        
			        .POST(accept(MediaType.APPLICATION_JSON), handler::create)
			        .PUT(accept(MediaType.APPLICATION_JSON), handler::update)
			        .DELETE(ID, accept(MediaType.APPLICATION_JSON), handler::delete))
			    .build();
  }
  
	private RequestPredicate firstNameParam(Predicate<String> predicate) {
	    return RequestPredicates.queryParam(CustomerHandler.FIRST_NAME, predicate);
	}
	
	private RequestPredicate lastNameParam(Predicate<String> predicate) {
		return RequestPredicates.queryParam(CustomerHandler.LAST_NAME, predicate);
	}
	
	private RequestPredicate companyNameParam(Predicate<String> predicate) {
		return RequestPredicates.queryParam(CustomerHandler.COMPANY_NAME, predicate);
	}
}
