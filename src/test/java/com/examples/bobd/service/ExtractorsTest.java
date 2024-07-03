package com.examples.bobd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;

public class ExtractorsTest {

    private ServerRequest serverRequest;

    @BeforeEach
    public void setup() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("key", "value");
        queryParams.add("dir", "asc");
        queryParams.add("sort", "field1");
        queryParams.add("sort", "field2");
        queryParams.add("size", "10");
        queryParams.add("page", "1");

        serverRequest = MockServerRequest.builder().queryParams(queryParams).build();
    }

    @Test
    public void testExtractStringParam() {
        assertEquals("value", Extractors.extractStringParam(serverRequest, "key").orElse(null));
    }

    @Test
    public void testExtractLongParam() {
        assertEquals(10L, Extractors.extractLongParam(serverRequest, "size").orElse(-1L));
    }

    @Test
    public void testExtractIntParam() {
        assertEquals(10, Extractors.extractIntParam(serverRequest, "size").orElse(null));
    }

    @Test
    public void testExtractStringsParam() {
        var expected = List.of("field1", "field2");
        var actual =  Extractors.extractStringsParam(serverRequest, "sort").orElse(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testExtractPageable() {
        Pageable expectedPageable = PageRequest.of(1, 10);
        assertEquals(expectedPageable, Extractors.extractPageable(serverRequest).orElse(null));
    }

    @Test
    public void testExtractSort() {
        Sort expectedSort = Sort.by(Sort.Direction.ASC, "field1", "field2");
        assertEquals(expectedSort, Extractors.extractSort(serverRequest, null).orElse(null));
    }
}
