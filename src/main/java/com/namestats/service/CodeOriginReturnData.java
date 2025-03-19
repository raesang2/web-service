package com.namestats.service;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CodeOriginReturnData {
    @JsonProperty("TEXT_VALUE")  // JSON에서 오는 TEXT_VALUE를 매핑
    private String TEXT_VALUE;

    @JsonProperty("KEY_VALUE")  // JSON에서 오는 KEY_VALUE를 매핑
    private String KEY_VALUE;
}
