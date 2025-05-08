package com.namestats.controller;

import com.namestats.dto.BookSearchResponse;
import com.namestats.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookSearchApiController {

    private final BookSearchService bookSearchService;

    @GetMapping("/searchBooks")
    public ResponseEntity<BookSearchResponse> searchBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String isbn13,
            @RequestParam(required = false) String exactMatch,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {

        BookSearchResponse response = bookSearchService.searchBooks(
                keyword, title, author, publisher, isbn13, exactMatch, pageNo, pageSize
        );
        return ResponseEntity.ok(response);
    }
}
