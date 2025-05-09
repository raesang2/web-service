package com.namestats.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namestats.dto.NaverBookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NaverBookSearchService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${api.x-naver-client-id}")
    private String naverClientId;

    @Value("${api.x-naver-client-secret}")
    private String naverClientSecret;

    private static final String BASE_URL = "https://openapi.naver.com/v1/search/book.json";

    public List<NaverBookDto> searchBookByQuery(String query) {
        List<NaverBookDto> result = new ArrayList<>();
        try {
            // URI 구성
            String uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .queryParam("query", query)
                    .build()
                    .toUriString();

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", naverClientId);
            headers.set("X-Naver-Client-Secret", naverClientSecret);
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            // HttpEntity 생성
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // API 호출
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode items = root.path("items");

            if (items.isArray()) {
                for (JsonNode item : items) {
                    NaverBookDto book = objectMapper.treeToValue(item, NaverBookDto.class);
                    result.add(book);
                }
            }

        } catch (HttpClientErrorException e) {
            System.err.println("NAVER API 오류: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
