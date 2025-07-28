package com.namestats.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LocalDataPostpartumCenter {

    private String manageNo;
    private String localGovCode;

    private String licenseDate;
    private String status;
    private String phoneNumber;
    private String roadAddress;
    private String businessName;

    private Double coordX5174;
    private Double coordY5174;
    private Double latitude;
    private Double longitude;

    private Integer pregnantCapacity;
    private Integer infantCapacity;

    private Double pregnantRoomArea;
    private Double infantRoomArea;

    private Integer totalFloors;
    private Integer aboveGroundFloors;
    private Integer basementFloors;
    
    private String normalizedName;
    private String normalizedPhone;
    private String normalizedAddr;
}