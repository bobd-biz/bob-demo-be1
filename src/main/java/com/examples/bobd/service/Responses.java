package com.examples.bobd.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public class Responses {

//	public static BodyBuilder ok() {
//        return ServerResponse.ok()
//                .contentType(MediaType.APPLICATION_JSON);
//     }
//	
//	public static BodyBuilder created() {
//        return ServerResponse.status(HttpStatus.CREATED)
//                .contentType(MediaType.APPLICATION_JSON);	
//    }
	
	public static Mono<ServerResponse> notFound(String message) {
		return createResponse(HttpStatus.NOT_FOUND, message);
	}

	public static Mono<ServerResponse> internalServerError(String message) {
		return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}
	
	public static Mono<ServerResponse> badRequest(String message) {
        return createResponse(HttpStatus.BAD_REQUEST, message);
    }
	
	private static Mono<ServerResponse> createResponse(HttpStatus status, String message) {
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(message);	
	}
}
