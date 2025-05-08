package com.namestats.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryResponse {
    private Response response;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private int pageNo;
        private int pageSize;
        private int numFound;
        private int resultNum;
        private List<LibWrapper> libs;  // 여기를 수정!
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LibWrapper {
        private Lib lib;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Lib {
        private String libCode;
        private String libName;
        private String address;
        private String tel;
        private String fax;
        private String latitude;
        private String longitude;
        private String homepage;
        private String closed;
        private String operatingTime;
    }
}