package com.namestats.service;

import com.namestats.dto.BookSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class BookSearchService {

    private final RestTemplate restTemplate;

    private static final String BASE_URL = "http://data4library.kr/api/srchBooks";

    @Value("${api.lib-service-key}")
    private String libServiceKey;

    public BookSearchResponse searchBooks(String keyword, String title, String author,
                                          String publisher, String isbn13, String exactMatch,
                                          int pageNo, int pageSize) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("authKey", libServiceKey)
                .queryParam("format", "json")
                .queryParam("pageNo", pageNo)
                .queryParam("pageSize", pageSize);

        if (keyword != null && !keyword.isBlank()) builder.queryParam("keyword", keyword);
        if (title != null && !title.isBlank()) builder.queryParam("title", title);
        if (author != null && !author.isBlank()) builder.queryParam("author", author);
        if (publisher != null && !publisher.isBlank()) builder.queryParam("publisher", publisher);
        if (isbn13 != null && !isbn13.isBlank()) builder.queryParam("isbn13", isbn13);
        if (exactMatch != null && !exactMatch.isBlank()) builder.queryParam("exactMatch", exactMatch);

        String uri = builder.build().toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");

        ResponseEntity<BookSearchResponse> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                BookSearchResponse.class
        );

        return responseEntity.getBody();
    }
}
