package com.examples.bobd.service;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.examples.bobd.model.Company;
import com.examples.bobd.repository.CompanyRepository;

import reactor.test.StepVerifier;

public class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        Company company = new Company(1L, "Test Company 1");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        StepVerifier.create(companyService.findById(1L))
                .expectNextMatches(c -> c.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    public void testFindAll() {
        Company company1 = new Company(1L, "Test Company 1");
        Company company2 = new Company(2L, "Test Company 2");
        when(companyRepository.findAll()).thenReturn(Arrays.asList(company1, company2));

        StepVerifier.create(companyService.findAll())
                .expectNextMatches(c -> c.getId().equals(1L))
                .expectNextMatches(c -> c.getId().equals(2L))
                .verifyComplete();
    }

    // Add more tests for other methods in the CompanyService class
}
