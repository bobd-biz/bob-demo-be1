package com.examples.bobd.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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

    @Test
		public void testSave() {
			Company company = new Company(1L, "Test Company 1");
			when(companyRepository.save(company)).thenReturn(company);
	
			StepVerifier.create(companyService.save(company)).expectNextMatches(c -> c.getId().equals(1L)).verifyComplete();
		}
    
    @Test
	public void testUpdate() {
		Company company = new Company(1L, "Test Company 1");
		when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
		when(companyRepository.save(company)).thenReturn(company);

		StepVerifier.create(companyService.update(company)).expectNextMatches(c -> c.getId().equals(1L))
				.verifyComplete();
	}
    
    @Test
    public void testUpdateNotFound() {
	            Company company = new Company(1L, "Test Company 1");
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        StepVerifier.create(companyService.update(company))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
	public void testDelete() {
		Company company = new Company(1L, "Test Company 1");
		when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

		companyService.delete(1L);

		verify(companyRepository, times(1)).deleteById(1L);
	}
    
	@Test
	public void testFindByName() {
		Company company = new Company(1L, "Test Company 1");
		when(companyRepository.findByCompanyNameIgnoreCase("Test Company 1")).thenReturn(Optional.of(company));

		StepVerifier.create(companyService.findByName("Test Company 1")).expectNextMatches(c -> c.getId().equals(1L))
				.verifyComplete();
	}
	
	@Test
	public void testFindByNameNotFound() {
		when(companyRepository.findByCompanyNameIgnoreCase("Nonexistent")).thenReturn(Optional.empty());

		StepVerifier.create(companyService.findByName("Nonexistent")).verifyComplete();
	}
	
	@Test
	public void testFindAllPageable() {
        Company company1 = new Company(1L, "Test Company 1");
        Company company2 = new Company(2L, "Test Company 2");
		Optional<Pageable> pageRequest0_10 = Optional.of(PageRequest.of(0, 10));
		Optional<Sort> sort = Optional.empty();
		when(companyRepository.findAll(pageRequest0_10.get())).thenReturn(new PageImpl<>(Arrays.asList(company1, company2)));
		
        StepVerifier.create(companyService.findAll(pageRequest0_10, sort))
                .expectNextMatches(c -> c.getId().equals(1L))
                .expectNextMatches(c -> c.getId().equals(2L))
                .verifyComplete();
    }
	
	@Test
	public void testFindAllPageableEmpty() {
		Optional<Pageable> pageRequest0_10 = Optional.of(PageRequest.of(0, 10));
		Optional<Sort> sort = Optional.empty();
		when(companyRepository.findAll(pageRequest0_10.get())).thenReturn(new PageImpl<>(Collections.emptyList()));

		StepVerifier.create(companyService.findAll(pageRequest0_10, sort))
				.verifyComplete();
	}
	
	@Test
	public void testFindAllPageableOne() {
		Company company = new Company(1L, "Test Company 1");
		Optional<Pageable> pageRequest0_10 = Optional.of(PageRequest.of(0, 10));
		Optional<Sort> sort = Optional.empty();
		when(companyRepository.findAll(pageRequest0_10.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company)));

		StepVerifier.create(companyService.findAll(pageRequest0_10, sort)).expectNextMatches(c -> c.getId().equals(1L))
				.verifyComplete();
	}
	
	@Test
	public void testFindAllPageableTwoPages() {
		Company company1 = new Company(1L, "Test Company 1");
		Company company2 = new Company(2L, "Test Company 2");
		Optional<Pageable> pageRequest0_1 = Optional.of(PageRequest.of(0, 1));
		Optional<Pageable> pageRequest1_1 = Optional.of(PageRequest.of(1, 1));
		Optional<Sort> sort = Optional.empty();
		
		when(companyRepository.findAll(pageRequest0_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company1)));
		when(companyRepository.findAll(pageRequest1_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company2)));
		when(companyRepository.findAll(pageRequest0_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company1)));
		when(companyRepository.findAll(pageRequest1_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company2)));

		StepVerifier.create(companyService.findAll(pageRequest0_1, sort)).expectNextMatches(c -> c.getId().equals(1L))
				.verifyComplete();
		StepVerifier.create(companyService.findAll(pageRequest1_1, sort)).expectNextMatches(c -> c.getId().equals(2L))
				.verifyComplete();
	}
	
	@Test
	public void testFindAllPageableTwoPagesEmpty() {
		Optional<Pageable> pageRequest0_1 = Optional.of(PageRequest.of(0, 1));
		Optional<Pageable> pageRequest1_1 = Optional.of(PageRequest.of(1, 1));
		Optional<Sort> sort = Optional.empty();
		when(companyRepository.findAll(pageRequest0_1.get())).thenReturn(new PageImpl<>(Collections.emptyList()));
		when(companyRepository.findAll(pageRequest1_1.get())).thenReturn(new PageImpl<>(Collections.emptyList()));

		StepVerifier.create(companyService.findAll(pageRequest0_1, sort)).verifyComplete();
		StepVerifier.create(companyService.findAll(pageRequest1_1, sort)).verifyComplete();
	}
	
	@Test
	public void testFindAllPageableTwoPagesOne() {
		Company company = new Company(1L, "Test Company 1");
		Optional<Pageable> pageRequest0_1 = Optional.of(PageRequest.of(0, 1));
		Optional<Pageable> pageRequest1_1 = Optional.of(PageRequest.of(1, 1));
		Optional<Sort> sort = Optional.empty();
		when(companyRepository.findAll(pageRequest0_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company)));
		when(companyRepository.findAll(pageRequest1_1.get())).thenReturn(new PageImpl<>(Collections.emptyList()));

		StepVerifier.create(companyService.findAll(pageRequest0_1, sort)).expectNextMatches(c -> c.getId().equals(1L))
				.verifyComplete();
		StepVerifier.create(companyService.findAll(pageRequest1_1, sort)).verifyComplete();
	}
	
	@Test
	public void testFindAllPageableTwoPagesTwo() {
		Company company1 = new Company(1L, "Test Company 1");
		Company company2 = new Company(2L, "Test Company 2");
		Optional<Pageable> pageRequest0_1 = Optional.of(PageRequest.of(0, 1));
		Optional<Pageable> pageRequest1_1 = Optional.of(PageRequest.of(1, 1));
		Optional<Sort> sort = Optional.empty();
		when(companyRepository.findAll(pageRequest0_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company1)));
		when(companyRepository.findAll(pageRequest1_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company2)));

		StepVerifier.create(companyService.findAll(pageRequest0_1, sort)).expectNextMatches(c -> c.getId().equals(1L))
				.verifyComplete();
		StepVerifier.create(companyService.findAll(pageRequest1_1, sort)).expectNextMatches(c -> c.getId().equals(2L))
				.verifyComplete();
	}
	
	@Test
	public void testFindAllPageableTwoPagesTwoEmpty() {
		Optional<Pageable> pageRequest0_1 = Optional.of(PageRequest.of(0, 1));
		Optional<Pageable> pageRequest1_1 = Optional.of(PageRequest.of(1, 1));
		Optional<Sort> sort = Optional.empty();
		when(companyRepository.findAll(pageRequest0_1.get())).thenReturn(new PageImpl<>(Collections.emptyList()));
		when(companyRepository.findAll(pageRequest1_1.get())).thenReturn(new PageImpl<>(Collections.emptyList()));

		StepVerifier.create(companyService.findAll(pageRequest0_1, sort)).verifyComplete();
		StepVerifier.create(companyService.findAll(pageRequest1_1, sort)).verifyComplete();
	}
	
	@Test
	public void testFindAllPageableTwoPagesOneEmpty() {
		Company company = new Company(1L, "Test Company 1");
		Optional<Pageable> pageRequest0_1 = Optional.of(PageRequest.of(0, 1));
		Optional<Pageable> pageRequest1_1 = Optional.of(PageRequest.of(1, 1));
		Optional<Sort> sort = Optional.empty();
		when(companyRepository.findAll(pageRequest0_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company)));
		when(companyRepository.findAll(pageRequest1_1.get())).thenReturn(new PageImpl<>(Collections.emptyList()));

		StepVerifier.create(companyService.findAll(pageRequest0_1, sort)).expectNextMatches(c -> c.getId().equals(1L))
				.verifyComplete();
		StepVerifier.create(companyService.findAll(pageRequest1_1, sort)).verifyComplete();
	}
	
	@Test
	public void testFindAllPageableTwoPagesTwoOne() {
		Company company1 = new Company(1L, "Test Company 1");
		Company company2 = new Company(2L, "Test Company 2");
		Optional<Pageable> pageRequest0_1 = Optional.of(PageRequest.of(0, 1));
		Optional<Pageable> pageRequest1_1 = Optional.of(PageRequest.of(1, 1));
		Optional<Sort> sort = Optional.empty();
		when(companyRepository.findAll(pageRequest0_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company1)));
		when(companyRepository.findAll(pageRequest1_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company2)));

		StepVerifier.create(companyService.findAll(pageRequest0_1, sort)).expectNextMatches(c -> c.getId().equals(1L))
				.verifyComplete();
		StepVerifier.create(companyService.findAll(pageRequest1_1, sort)).expectNextMatches(c -> c.getId().equals(2L))
				.verifyComplete();
	}
	
	@Test
	public void testFindAllPageableTwoPagesTwoOneEmpty() {
		Company company1 = new Company(1L, "Test Company 1");
		Optional<Pageable> pageRequest0_1 = Optional.of(PageRequest.of(0, 1));
		Optional<Pageable> pageRequest1_1 = Optional.of(PageRequest.of(1, 1));
		Optional<Sort> sort = Optional.empty();
		when(companyRepository.findAll(pageRequest0_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company1)));
		when(companyRepository.findAll(pageRequest1_1.get())).thenReturn(new PageImpl<>(Collections.emptyList()));

		StepVerifier.create(companyService.findAll(pageRequest0_1, sort)).expectNextMatches(c -> c.getId().equals(1L))
				.verifyComplete();
		StepVerifier.create(companyService.findAll(pageRequest1_1, sort)).verifyComplete();
	}
	
	@Test
	public void testFindAllPageableTwoPagesTwoOneTwo() {
		Company company1 = new Company(1L, "Test Company 1");
		Company company2 = new Company(2L, "Test Company 2");
		Optional<Pageable> pageRequest0_1 = Optional.of(PageRequest.of(0, 1));
		Optional<Pageable> pageRequest1_1 = Optional.of(PageRequest.of(1, 1));
		Optional<Sort> sort = Optional.empty();
		when(companyRepository.findAll(pageRequest0_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company1)));
		when(companyRepository.findAll(pageRequest1_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company2)));

		StepVerifier.create(companyService.findAll(pageRequest0_1, sort)).expectNextMatches(c -> c.getId().equals(1L))
				.verifyComplete();
		StepVerifier.create(companyService.findAll(pageRequest1_1, sort)).expectNextMatches(c -> c.getId().equals(2L))
				.verifyComplete();
	}
	
	@Test
	public void testFindAllPageableTwoPagesTwoOneTwoEmpty() {
		Company company1 = new Company(1L, "Test Company 1");
		Optional<Pageable> pageRequest0_1 = Optional.of(PageRequest.of(0, 1));
		Optional<Pageable> pageRequest1_1 = Optional.of(PageRequest.of(1, 1));
		Optional<Sort> sort = Optional.empty();
		when(companyRepository.findAll(PageRequest.of(0, 1)))
				.thenReturn(new PageImpl<>(Collections.singletonList(company1)));
		when(companyRepository.findAll(pageRequest1_1.get())).thenReturn(new PageImpl<>(Collections.emptyList()));

		StepVerifier.create(companyService.findAll(pageRequest0_1, sort)).expectNextMatches(c -> c.getId().equals(1L))
				.verifyComplete();
		StepVerifier.create(companyService.findAll(pageRequest1_1, sort)).verifyComplete();
	}
	
	@Test
	public void testFindAllPageableTwoPagesTwoOneTwoOne() {
		Company company1 = new Company(1L, "Test Company 1");
		Company company2 = new Company(2L, "Test Company 2");
		Optional<Pageable> pageRequest0_1 = Optional.of(PageRequest.of(0, 1));
		Optional<Pageable> pageRequest1_1 = Optional.of(PageRequest.of(1, 1));
		Optional<Sort> sort = Optional.empty();
		when(companyRepository.findAll(pageRequest0_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company1)));
		when(companyRepository.findAll(pageRequest1_1.get()))
				.thenReturn(new PageImpl<>(Collections.singletonList(company2)));

		StepVerifier.create(companyService.findAll(pageRequest0_1, sort)).expectNextMatches(c -> c.getId().equals(1L))
				.verifyComplete();
		StepVerifier.create(companyService.findAll(pageRequest1_1, sort)).expectNextMatches(c -> c.getId().equals(2L))
				.verifyComplete();
	}
}
