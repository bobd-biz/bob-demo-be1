package com.examples.bobd.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;

public class ExtractorsTest {

    private ServerRequest serverRequest;
    private static final Map<String, String> validFields = Map.of("field1", "field1", "field2", "field2");

    @BeforeEach
    public void setup() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("key", "value");
        queryParams.add("sort", "Field1");
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
        var expected = List.of("Field1", "field2");
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
        assertEquals(expectedSort, Extractors.extractSort(serverRequest, validFields).orElse(null));
    }

    @Test
    public void testExtractSort_unknown_sort_field() {
        Map<String, String> otherFields = Map.of("foo", "bar", "george", "mary");
    	assertThrows(IllegalArgumentException.class, () -> Extractors.extractSort(serverRequest, otherFields));
    }

    @Test
    public void testExtractSort_no_sort_fields() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("sort", null);

        serverRequest = MockServerRequest.builder().queryParams(queryParams).build();
        
        assertTrue(Extractors.extractSort(serverRequest, Map.of("bogus", "foobar")).isEmpty(), "Unfound fields should be ignored");
    }
    
    @Test
	public void testExtractSort_ignore_parameter_case() {
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("sort", "CAPS");

		serverRequest = MockServerRequest.builder().queryParams(queryParams).build();

        Sort expectedSort = Sort.by(Sort.Direction.ASC, "foobar");
		assertEquals(expectedSort, Extractors.extractSort(serverRequest, Map.of("caps", "foobar")).orElse(null),
				"Parameter case should be ignored");
	}
    
    @Test
    public void testExtractSort_sort_variations() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("sort", "field1");
        queryParams.add("sort", "field2:desc");
        queryParams.add("sort", "field3:asc");
        queryParams.add("sort", "field4:desc:ic");
        queryParams.add("sort", "field5:desc:somethingelse");
        
        serverRequest = MockServerRequest.builder().queryParams(queryParams).build();
        var mockValidFields = Map.of("field1", "field1", "field2", "field2", "field3", "field3", "field4", "field4", "field5", "field5");
        Sort info = Extractors.extractSort(serverRequest, mockValidFields).get();
        
        assertAll(
        		() -> assertEquals(new Sort.Order(Sort.Direction.ASC, "field1"), info.getOrderFor("field1")),
                () -> assertEquals(new Sort.Order(Sort.Direction.DESC, "field2"), info.getOrderFor("field2")),
                () -> assertEquals(new Sort.Order(Sort.Direction.ASC, "field3"), info.getOrderFor("field3")),
                () -> assertEquals(new Sort.Order(Sort.Direction.DESC, "field4", true, NullHandling.NATIVE), info.getOrderFor("field4")),
                () -> assertEquals(new Sort.Order(Sort.Direction.DESC, "field5"), info.getOrderFor("field5"))
        );
    }
}
