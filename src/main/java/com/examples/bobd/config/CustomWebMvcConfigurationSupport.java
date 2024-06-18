package com.examples.bobd.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.reactive.config.WebFluxConfigurationSupport;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;

@Configuration
public class CustomWebMvcConfigurationSupport extends WebFluxConfigurationSupport {

	private static final int DEFAULT_PAGE_SIZE = 100;
	
    @Bean
    public PageRequest defaultPageRequest() {
        return PageRequest.of(0, DEFAULT_PAGE_SIZE);
    }

    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        SortHandlerMethodArgumentResolver argumentResolver = new SortHandlerMethodArgumentResolver();
        argumentResolver.setSortParameter("sort");
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver(argumentResolver);
        resolver.setFallbackPageable(defaultPageRequest());
        resolver.setPageParameterName("page");
        resolver.setSizeParameterName("size");
//        argumentResolvers.add(resolver);
    }
}