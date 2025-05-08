package com.namestats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionDto {
    private String regionCd;       // 예: 11
    private String regionNm;       // 예: 서울특별시
    private String dtlRegionCd;    // 예: 11010
    private String dtlRegionNm;    // 예: 종로구
}