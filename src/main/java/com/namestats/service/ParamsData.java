package com.namestats.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class ParamsData {

    // 변수로 빼서 관리할 값들
    private String multiCandTypeValue = "YY";   // MultiCandType 값
    private String multiCandStDtValue = "2024"; // MultiCandStDt 값
    private String multiCandEdDtValue = "2025"; // MultiCandEdDt 값
    private String genderCdValue = "1";         // GenderCd 값
    private String sidoCdValue = "11";         // sidoCd 값
    
    // @MultiCandType
    private List<String> multiCandType;
    private String multiCandTypeType = "STRING";
    private String multiCandTypeDefaultValue = "";

    // @MultiCandStDt
    private List<String> multiCandStDt;
    private String multiCandStDtType = "STRING";
    private String multiCandStDtDefaultValue = "";

    // @MultiCandEdDt
    private List<String> multiCandEdDt;
    private String multiCandEdDtType = "STRING";
    private String multiCandEdDtDefaultValue = "";

    // @GenderCd
    private List<String> genderCd;
    private String genderCdType = "STRING";
    private String genderCdDefaultValue = "[All]";
    private String genderCdWhereClause = "F.GENDER_CD";

    // @SidoCd
    private List<String> sidoCd;    
    private String sidoCdType = "STRING";
    private String sidoCdDefaultValue = "[All]";
    private String sidoCdWhereClause = "C.SIDO_CD";

    // @CggCd
    private List<String> cggCd = Arrays.asList("_EMPTY_VALUE_")
            .stream()
            .map(s -> "\"" + s + "\"")  // 각 항목에 큰따옴표 추가
            .collect(Collectors.toList());
    private String cggCdType = "STRING";
    private String cggCdDefaultValue = "[All]";
    private String cggCdWhereClause = "D.CGG_CD";

    // @UmdCd
    private List<String> umdCd = Arrays.asList("_EMPTY_VALUE_")
            .stream()
            .map(s -> "\"" + s + "\"")  // 각 항목에 큰따옴표 추가
            .collect(Collectors.toList());
    private String umdCdType = "STRING";
    private String umdCdDefaultValue = "[All]";
    private String umdCdWhereClause = "E.UMD_CD";


    // setter for multiCandTypeValue
    public void setMultiCandTypeValue(String multiCandTypeValue) {
        this.multiCandTypeValue = multiCandTypeValue;
        this.multiCandType = Arrays.asList(multiCandTypeValue)
                .stream()
                .map(s -> "\"" + s + "\"")  // 각 항목에 큰따옴표 추가
                .collect(Collectors.toList());
    }

    // setter for multiCandStDtValue
    public void setMultiCandStDtValue(String multiCandStDtValue) {
        this.multiCandStDtValue = multiCandStDtValue;
        this.multiCandStDt = Arrays.asList(multiCandStDtValue)
                .stream()
                .map(s -> "\"" + s + "\"")  // 각 항목에 큰따옴표 추가
                .collect(Collectors.toList());
    }

    // setter for multiCandEdDtValue
    public void setMultiCandEdDtValue(String multiCandEdDtValue) {
        this.multiCandEdDtValue = multiCandEdDtValue;
        this.multiCandEdDt = Arrays.asList(multiCandEdDtValue)
                .stream()
                .map(s -> "\"" + s + "\"")  // 각 항목에 큰따옴표 추가
                .collect(Collectors.toList());
    }

    // setter for genderCdValue
    public void setGenderCdValue(String genderCdValue) {
        this.genderCdValue = genderCdValue;
        this.genderCd = Arrays.asList(genderCdValue)
                .stream()
                .map(s -> "\"" + s + "\"")  // 각 항목에 큰따옴표 추가
                .collect(Collectors.toList());
    }
    
    // setter for sidoCd
    public void setSidoCdValue(String sidoCdValue) {
        this.sidoCdValue = sidoCdValue;
        this.sidoCd = Arrays.asList(sidoCdValue)
                .stream()
                .map(s -> "\"" + s + "\"")  // 각 항목에 큰따옴표 추가
                .collect(Collectors.toList());
    }
    
    // JSON 파싱을 위한 메서드
    public String getParamsJson() {
        return String.format(
            "{\n" +
            "  \"@MultiCandType\": {\n" +
            "    \"value\": %s,\n" +
            "    \"type\": \"%s\",\n" +
            "    \"defaultValue\": \"%s\"\n" +
            "  },\n" +
            "  \"@MultiCandStDt\": {\n" +
            "    \"value\": %s,\n" +
            "    \"type\": \"%s\",\n" +
            "    \"defaultValue\": \"%s\"\n" +
            "  },\n" +
            "  \"@MultiCandEdDt\": {\n" +
            "    \"value\": %s,\n" +
            "    \"type\": \"%s\",\n" +
            "    \"defaultValue\": \"%s\"\n" +
            "  },\n" +
            "  \"@SidoCd\": {\n" +
            "    \"value\": %s,\n" +
            "    \"type\": \"%s\",\n" +
            "    \"defaultValue\": \"%s\",\n" +
            "    \"whereClause\": \"%s\"\n" +
            "  },\n" +
            "  \"@CggCd\": {\n" +
            "    \"value\": %s,\n" +
            "    \"type\": \"%s\",\n" +
            "    \"defaultValue\": \"%s\",\n" +
            "    \"whereClause\": \"%s\"\n" +
            "  },\n" +
            "  \"@UmdCd\": {\n" +
            "    \"value\": %s,\n" +
            "    \"type\": \"%s\",\n" +
            "    \"defaultValue\": \"%s\",\n" +
            "    \"whereClause\": \"%s\"\n" +
            "  },\n" +
            "  \"@GenderCd\": {\n" +
            "    \"value\": %s,\n" +
            "    \"type\": \"%s\",\n" +
            "    \"defaultValue\": \"%s\",\n" +
            "    \"whereClause\": \"%s\"\n" +
            "  }\n" +
            "}",
            multiCandType, multiCandTypeType, multiCandTypeDefaultValue,
            multiCandStDt, multiCandStDtType, multiCandStDtDefaultValue,
            multiCandEdDt, multiCandEdDtType, multiCandEdDtDefaultValue,
            sidoCd, sidoCdType, sidoCdDefaultValue, sidoCdWhereClause,
            cggCd, cggCdType, cggCdDefaultValue, cggCdWhereClause,
            umdCd, umdCdType, umdCdDefaultValue, umdCdWhereClause,
            genderCd, genderCdType, genderCdDefaultValue, genderCdWhereClause
        );
    }
}
