
package com.examples.bobd.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.server.ServerRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for extracting parameters from a ServerRequest.
 */
@Slf4j
public class Extractors {

	private static final String SORT_BY = "sort";
	private static final String PAGE_SIZE = "size";
	private static final String PAGE_NUMBER = "page";
	private static final int DEFAULT_PAGE_SIZE = 10000;

	/**
	 * Extracts a string parameter from the request.
	 *
	 * @param request the server request
	 * @param key     the parameter key
	 * @return an Optional containing the parameter value if present
	 */
	public static Optional<String> extractStringParam(ServerRequest request, String key) {
		return request.queryParam(key);
	}

	/**
	 * Extracts a long parameter from the request.
	 *
	 * @param request the server request
	 * @param key     the parameter key
	 * @return an Optional containing the parameter value if present
	 */
	public static Optional<Long> extractLongParam(ServerRequest request, String key) {
		return request.queryParam(key).map(Long::valueOf);
	}

	/**
	 * Extracts an integer parameter from the request.
	 *
	 * @param request the server request
	 * @param key     the parameter key
	 * @return an Optional containing the parameter value if present
	 */
	public static Optional<Integer> extractIntParam(ServerRequest request, String key) {
		return request.queryParam(key).map(Integer::valueOf);
	}

	/**
	 * Extracts a list of string parameters from the request.
	 *
	 * @param request the server request
	 * @param key     the parameter key
	 * @return an Optional containing the parameter values if present
	 */
	public static Optional<List<String>> extractStringsParam(ServerRequest request, String key) {
		return Optional.ofNullable(request.queryParams().get(key));
	}

	/**
	 * Extracts a pageable object from the request.
	 *
	 * @param request the server request
	 * @return an Optional containing the pageable object if present
	 */
	public static Optional<Pageable> extractPageable(ServerRequest request) {
		Optional<Integer> pageSize = extractIntParam(request, PAGE_SIZE);
		Optional<Integer> pageNumber = extractIntParam(request, PAGE_NUMBER);
		if (pageSize.isPresent() || pageNumber.isPresent()) {
			return Optional.of(Pageable.ofSize(pageSize.orElse(DEFAULT_PAGE_SIZE)).withPage(pageNumber.orElse(0)));
		}
		return Optional.empty();
	}

	/**
	 * Extracts a sort object from the request.
	 *
	 * @param request     the server request
	 * @param validFields a map of valid fields
	 * @return an Optional containing the sort object if present
	 */
	public static Optional<Sort> extractSort(ServerRequest request, Map<String, String> validFields) {
		Optional<List<String>> sort = extractStringsParam(request, SORT_BY);
		log.info("sort={} valid={}", sort, validFields);

		List<Sort.Order> orders = sort.stream().flatMap(List::stream)
				.filter(fieldSpec -> fieldSpec != null && !fieldSpec.isEmpty())
				.map(fieldSpec -> createSortOrder(fieldSpec, validFields)).toList();
		return !orders.isEmpty() ? Optional.of(Sort.by(orders)) : Optional.empty();
	}

	/**
	 * Creates a sort order object.
	 *
	 * @param fieldSpec   the field specification
	 * @param validFields a map of valid fields
	 * @return a sort order object
	 */
	public static Sort.Order createSortOrder(String fieldSpec, Map<String, String> validFields) {
		String[] parts = fieldSpec.split(":");
		log.info("parts={}", List.of(parts));
		log.info("lower={}", parts[0].toLowerCase());
		if (!validFields.containsKey(parts[0].toLowerCase())) {
			throw new IllegalArgumentException("Invalid sort field: " + parts[0]);
		}
		return parts.length == 3
				? new Sort.Order(sortDirection(parts[1]), toExternal(parts[0], validFields), isIgnoreCase(parts[2]),
						Sort.NullHandling.NATIVE)
				: parts.length == 2 ? new Sort.Order(sortDirection(parts[1]), toExternal(parts[0], validFields))
						: new Sort.Order(Sort.Direction.ASC, toExternal(parts[0], validFields));
	}

	/**
	 * Converts a key to its external representation.
	 *
	 * @param key         the key
	 * @param validFields a map of valid fields
	 * @return the external representation of the key
	 */
	private static String toExternal(String key, Map<String, String> validFields) {
		return validFields.get(key.toLowerCase());
	}

	/**
	 * Determines the sort direction from a string.
	 *
	 * @param dir the direction string
	 * @return the sort direction
	 */
	private static Sort.Direction sortDirection(String dir) {
		return dir != null ? Sort.Direction.fromString(dir) : Sort.Direction.ASC;
	}

	/**
	 * Determines if a string represents a case-insensitive sort.
	 *
	 * @param ic the string
	 * @return true if the string represents a case-insensitive sort, false
	 *         otherwise
	 */
	private static boolean isIgnoreCase(String ic) {
		return "ic".equalsIgnoreCase(ic);
	}
}
