package com.namestats.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namestats.service.PostpartumCenterExcelImportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostpartumCenterApiController {

    private final PostpartumCenterExcelImportService service;

    @GetMapping("/excel-import-localdata-postpartumcenter")
    public String excelImportLocalDataPostpartumCenters() {
    	service.importLocalDataExcelData();
        return "산후조리원 localdata excel import 완료";
    }
    
    @GetMapping("/excel-import-mohw-postpartumcenter")
    public String excelImportMohwPostpartumCenters() {
    	service.importMohwExcelData();
        return "산후조리원 mohw excel import 완료";
    }
}
