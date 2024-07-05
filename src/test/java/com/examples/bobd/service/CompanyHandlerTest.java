
package com.examples.bobd.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.examples.bobd.model.Company;
import com.examples.bobd.routes.CompanyRoutes;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CompanyHandlerTest {

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyHandler companyHandler;
    
    private CompanyRoutes companyRoutes;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        companyRoutes = new CompanyRoutes();
        webTestClient = WebTestClient.bindToRouterFunction(companyRoutes.companyRoutesBean(companyHandler)).build();
    }

    @Test
    public void testFindAllCompanies() {
        Company company1 = new Company(1L, "Test Company 1");
        Company company2 = new Company(2L, "Test Company 2");
        when(companyService.findAll()).thenReturn(Flux.just(company1, company2));
        when(companyService.findAll(any(), any())).thenReturn(Flux.just(company1, company2));

        webTestClient.get().uri("/companies")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Company.class);
    }

    @Test
    public void testGetCompanyById() {
        Company company = new Company(1L, "Test Company 1");
        when(companyService.findById(1L)).thenReturn(Mono.just(company));

        webTestClient.get().uri("/companies/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Company.class);
    }

    // Add more tests for other methods in the CompanyHandler class
}
