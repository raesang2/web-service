package com.namestats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostpartumSearchDto {
    private String city;
    private String district;
    private String name;
    private String operatorType;
}