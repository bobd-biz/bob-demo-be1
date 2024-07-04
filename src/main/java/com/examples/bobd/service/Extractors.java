package com.examples.bobd.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.server.ServerRequest;

public class Extractors {
	
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
	
	public static Optional<Sort> extractSort(ServerRequest request, Set<String> validFields) {
		Optional<List<String>> sort = extractStringsParam(request, SORT_BY);
		System.out.println("sort: " + sort);
		
		List<Sort.Order> orders = sort.stream()
				.flatMap(List::stream)
				.filter(fieldSpec -> fieldSpec != null && !fieldSpec.isEmpty())
				.map(fieldSpec -> createSortOrder(fieldSpec, validFields))
				.toList();
		return !orders.isEmpty() ? Optional.of(Sort.by(orders)) : Optional.empty();
	}
	
	public static Sort.Order createSortOrder(String fieldSpec, Set<String> validFields) {
		String[] parts = fieldSpec.split(":");
		if (!validFields.contains(parts[0])) {
			throw new IllegalArgumentException("Invalid sort field: " + parts[0]);
		}
		return parts.length == 3 ? 
				new Sort.Order(sortDirection(parts[1]), parts[0], isIgnoreCase(parts[2]), Sort.NullHandling.NATIVE) :	
				parts.length == 2 ?
						new Sort.Order(sortDirection(parts[1]), parts[0]) :
						new Sort.Order(Sort.Direction.ASC, parts[0]);
	}
	
	private static Sort.Direction sortDirection(String dir) {
		return dir != null ? Sort.Direction.fromString(dir) : Sort.Direction.ASC;
	}
	
	private static boolean isIgnoreCase(String ic) {
		return "ic".equalsIgnoreCase(ic);
	}
}
