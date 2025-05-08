package com.namestats.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namestats.service.ExcelImportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ExcelImportController {

	
    private final ExcelImportService service;

    @GetMapping("/import-books")
    public String importBooks() {
        service.importExcelData();
        return "도서 데이터가 성공적으로 저장되었습니다.";
    }
}