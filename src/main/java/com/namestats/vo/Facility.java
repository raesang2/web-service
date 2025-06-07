package com.namestats.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Facility {
    private String exfcSn;
    private String dsgnYmd;
    private String dsgnYear;
    private String pfctSn;
    private String rgnCd;
    private String fctyCd;
    private String rgnNm;
    private String pfctNm;
    private String rmk;
    private String instSn;
    private String instNm;
    private String latCrtsVl;
    private String lotCrtsVl;
    private String fctyDesc;     // 시설 설명
    private String fctyImgPath;  // 시설 이미지 경로    
}
