package com.namestats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibSearchRequest {
    private String isbn;
    private String region;        // 예: 11
    private String dtlRegion;     // 예: 11010
    private Integer pageNo = 1;
    private Integer pageSize = 100;
    private String format = "json";
}