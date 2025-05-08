package com.namestats.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.namestats.dto.BookExistResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookExistService {

	private final RestTemplate restTemplate;
	private static final String BASE_URL = "http://data4library.kr/api/bookExist";
	@Value("${api.lib-service-key}")
	private String libServiceKey;
	
    public BookExistResponse checkBookExist(String libCode, String isbn13, String format) {
        String uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("authKey", libServiceKey)
                .queryParam("libCode", libCode)
                .queryParam("isbn13", isbn13)
                .queryParam("format", format == null ? "json" : format)
                .build()
                .toUriString();

		// 헤더 설정 (Accept: application/json)
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "*/*");

		// REST 요청 전송 (exchange 사용)
		ResponseEntity<BookExistResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
				new org.springframework.http.HttpEntity<>(headers), BookExistResponse.class);
        return responseEntity.getBody();
    }
}
