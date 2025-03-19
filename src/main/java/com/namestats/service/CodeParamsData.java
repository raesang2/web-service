package com.namestats.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class CodeParamsData {

    // 변수로 빼서 관리할 값들
	private String sidoCdValue = "11";         // sidoCd 값
    
    // @SidoCd
    private String sidoCd;    
    private String sidoCdType = "STRING";
    private String sidoCdDefaultValue = "[All]";

    // @CggCd
    private String cggCd = "[]";
    private String cggCdType = "STRING";
    private String cggCdDefaultValue = "[All]";
    
    // setter for sidoCd
    public void setSidoCdValue(String[] cidoArr) {
    	// 불필요한 공백 제거, 그리고 쌍따옴표를 추가하는 방법
    	sidoCd = Arrays.toString(cidoArr)
    			.replace("[", "[\"")
    			.replace("]", "\"]")
    			.replace(", ", "\", \"");
    }
    
    // JSON 파싱을 위한 메서드
    public String getParamsJson() {
        return String.format(
            "{\n" +
            "  \"@SidoCd\": {\n" +
            "    \"value\": %s,\n" +
            "    \"type\": \"%s\",\n" +
            "    \"defaultValue\": \"%s\",\n" +
            "  },\n" +
            "  \"@CggCd\": {\n" +
            "    \"value\": %s,\n" +
            "    \"type\": \"%s\",\n" +
            "    \"defaultValue\": \"%s\",\n" +
            "  }\n" +
            "}",
            sidoCd,
            sidoCdType,
            sidoCdDefaultValue,
            cggCd,
            cggCdType,
            cggCdDefaultValue
        );
    }
}
