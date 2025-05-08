package com.namestats.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.namestats.dto.BookExistResponse;
import com.namestats.service.BookExistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookExistApiController {

    private final BookExistService bookExistService;

    @GetMapping("/bookExist")
    public ResponseEntity<BookExistResponse> checkBookExist(
            @RequestParam String libCode,
            @RequestParam String isbn13) {

        BookExistResponse response = bookExistService.checkBookExist(libCode, isbn13, "json");
        return ResponseEntity.ok(response);
    }
}