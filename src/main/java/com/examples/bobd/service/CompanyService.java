package com.examples.bobd.service;

import java.util.Collections;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.examples.bobd.model.Company;
import com.examples.bobd.repository.CompanyRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CompanyService {

    private final CompanyRepository repo;

    public Mono<Company> createUser(Company company){
        return repo.save(company);
    }

	public Flux<Company> getAll() {
        return repo.findAll();
    }

    public Mono<Company> findById(Long id){
        return repo.findById(id);
    }

//    public Mono<Company> updateUser(Long id,  Company company){
//        return repo.findById(id)
//                .flatMap(company -> {
//                    Company.builder()
//                    .companyName(company.getCompanyName())
//                    .build();
//                    return repo.save(company);
//                });
//    }
//
//    public Mono<Company> deleteUser(Long id){
//        return repo.findById(id)
//                .flatMap(existingUser -> repo.delete(existingUser)
//                        .then(Mono.just(existingUser)));
//    }
//
//    public Flux<Company> fetchUsers(String name) {
//        Query query = new Query()
//                .with(Sort
//                        .by(Collections.singletonList(Sort.Order.asc("age")))
//                );
//        query.addCriteria(Criteria
//                .where("name")
//                .regex(name)
//        );
//
//        return reactiveMongoTemplate
//                .find(query, Company.class);
//    }
}