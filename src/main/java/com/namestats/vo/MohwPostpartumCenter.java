package com.namestats.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MohwPostpartumCenter {
    private String city;          // 시도
    private String district;      // 시군구
    private String operatorType;  // 운영주체
    private String name;          // 산후조리원명
    private String address;
    private String phone;
    private Integer priceStandard; // 일반실
    private Integer priceSpecial;  // 특실
    private String normalizedName;
    private String normalizedPhone;
    private String normalizedAddr;
}