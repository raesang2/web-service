package com.namestats.vo;

import lombok.Data;

@Data
public class PostpartumCenter {
	private Integer id;
    private String city;
    private String district;
    private String operatorType;
    private String name;
    private String address;
    private String phone;
    private Integer priceStandard;
    private Integer priceSpecial;
    private String localGovCode;
    private Double latitude;
    private Double longitude;
    private Integer pregnantCapacity;
    private Integer infantCapacity;
    private Double pregnantRoomArea;
    private Double infantRoomArea;
    private Double perPregnantRoomArea;
    private Double perInfantRoomArea;
    private String mName;
    private String lName;
    private String mAddr;
    private String lAddr;
    private String mPhone;
    private String lPhone;
}
