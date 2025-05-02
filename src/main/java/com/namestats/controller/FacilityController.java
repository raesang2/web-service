package com.namestats.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namestats.service.FacilityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FacilityController {

    private final FacilityService facilityService;

    @GetMapping("/fetch-facilities")
    public String fetch() throws Exception {
        facilityService.fetchAndSaveFacilities();
        return "저장 완료";
    }

}

