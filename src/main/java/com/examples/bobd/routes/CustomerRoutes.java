package com.examples.bobd.routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

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
			        .GET(ID, accept(MediaType.APPLICATION_JSON), handler::getById)
			        .GET(firstNameParam()
			                .and(lastNameParam()), handler::findByFirstNameOrLastName)
			        .GET(firstNameParam(), handler::findByFirstName)
			        .GET(lastNameParam(), handler::findByLastName)
			        .GET(companyNameParam(), handler::findByCompanyName)
			        .GET(accept(MediaType.APPLICATION_JSON), handler::findAll)
			        
			        .POST(accept(MediaType.APPLICATION_JSON), handler::create)
			        .PUT(accept(MediaType.APPLICATION_JSON), handler::update)
			        .DELETE(ID, accept(MediaType.APPLICATION_JSON), handler::delete))
			    .build();
  }
  
	private RequestPredicate firstNameParam() {
	    return RequestPredicates.queryParam(CustomerHandler.FIRST_NAME, t -> true).and(accept(MediaType.APPLICATION_JSON));
	}
	
	private RequestPredicate lastNameParam() {
		return RequestPredicates.queryParam(CustomerHandler.LAST_NAME, t -> true).and(accept(MediaType.APPLICATION_JSON));
	}
	
	private RequestPredicate companyNameParam() {
		return RequestPredicates.queryParam(CustomerHandler.COMPANY_NAME, t -> true).and(accept(MediaType.APPLICATION_JSON));
	}
}
