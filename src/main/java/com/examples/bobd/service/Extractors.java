package com.examples.bobd.service;

import java.util.Optional;

import org.springframework.web.reactive.function.server.ServerRequest;

public class Extractors {

	public static Optional<String> extractStringParam(ServerRequest request, String key) {
		return request.queryParam(key);
	}
	
	public static Optional<Long> extractLongParam(ServerRequest request, String key) {
		return request.queryParam(key).map(Long::valueOf);
	}
}
