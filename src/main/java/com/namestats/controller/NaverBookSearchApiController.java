package com.namestats.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.namestats.dto.NaverBookDto;
import com.namestats.service.NaverBookSearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NaverBookSearchApiController {

	private final NaverBookSearchService naverBookSearchService;

	@GetMapping("/naverSearchBooks")
	public ResponseEntity<List<NaverBookDto>> searchBooks(
			@RequestParam(required = true) String query
			) {

		List<NaverBookDto> naverBookDtoList = naverBookSearchService.searchBookByQuery(query);

		return ResponseEntity.ok(naverBookDtoList);
	}
}
