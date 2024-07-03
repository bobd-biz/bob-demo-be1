package com.examples.bobd.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.server.ServerRequest;

public class Extractors {
	
	private static final String SORT_DIR = "dir";
	private static final String SORT_BY = "sort";
	private static final String PAGE_SIZE = "size";
	private static final String PAGE_NUMBER = "page";
	private static final int DEFAULT_PAGE_SIZE = 10000;

	public static Optional<String> extractStringParam(ServerRequest request, String key) {
		return request.queryParam(key);
	}
	
	public static Optional<Long> extractLongParam(ServerRequest request, String key) {
		return request.queryParam(key).map(Long::valueOf);
	}
	
	public static Optional<Integer> extractIntParam(ServerRequest request, String key) {
		return request.queryParam(key).map(Integer::valueOf);
	}
	
	public static Optional<List<String>> extractStringsParam(ServerRequest request, String key) {
        return Optional.ofNullable(request.queryParams().get(key));
	}
	
	public static Optional<Pageable> extractPageable(ServerRequest request) {
		Optional<Integer> pageSize = extractIntParam(request, PAGE_SIZE);
		Optional<Integer> pageNumber = extractIntParam(request, PAGE_NUMBER);
		if (pageSize.isPresent() || pageNumber.isPresent()) {
			return Optional.of(Pageable.ofSize(pageSize.orElse(DEFAULT_PAGE_SIZE))
					.withPage(pageNumber.orElse(0)));
		}
		return Optional.empty();
	}
	
	public static Optional<Sort> extractSort(ServerRequest request) {
		return extractSort(request, Set.of());
	}
	
	public static Optional<Sort> extractSort(ServerRequest request, Set<String> validFields) {
		Optional<List<String>> sort = extractStringsParam(request, SORT_BY);
		Optional<String> dir = extractStringParam(request, SORT_DIR);
		System.out.println("sort: " + sort);
		
		// validate sort fields
		if (sort.isPresent() && !sort.get().stream()
				.filter(field -> field != null)
				.map(field -> field.toLowerCase())
				.allMatch(validFields::contains)) {
			throw new IllegalArgumentException("Invalid sort field");
		}
		
		if (sort.isPresent() || dir.isPresent()) {
			return Optional.of(Sort.by(Sort.Direction.fromString(dir.orElse("asc")), 
					sort.get().toArray(new String[0])));
		}
		return Optional.empty();
	}
}
