package com.examples.bobd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.examples.bobd.model.Company;
import com.examples.bobd.repository.CompanyRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CompanyService {

	private final CompanyRepository repo;
	
	public Optional<Company> findById(Long id) {
		return repo.findById(id);
	}
	
	public List<Company> findAll() {
		return repo.findAll();
	}
	
	public List<Company> findAll(Pageable pageable) {
		return repo.findAll(pageable).toList();
	}

    public Company create(Company company){
        return repo.save(company);
    }
    
	public Company update(Company company) {

		return findById(company.getId()).map(c -> {
			Company newcompany = Company.builder().id(c.getId()).companyName(company.getCompanyName()).build();
			return repo.save(newcompany);
		}).orElseThrow(() -> new RuntimeException("Company not found id=" + company.getId()));
	}
	
	public void delete(Long id) {
		repo.deleteById(id);
	}
	
	public Optional<Company> findByName(String name) {
		return repo.findByCompanyNameIgnoreCase(name);
	}
}