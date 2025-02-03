package com.namestats.service;

import lombok.Data;

@Data
public class OriginReturnData {
    private int 순위;             // 순위
    private String 전체비율;       // 전체비율
    private int 건수;             // 건수
    private String 이름;          // 이름
    private int 정렬;             // 정렬
}
