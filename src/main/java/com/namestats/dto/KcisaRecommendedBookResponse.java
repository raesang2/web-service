package com.namestats.dto;

import java.util.List;

import lombok.Data;

@Data
public class KcisaRecommendedBookResponse {
    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private List<Item> items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }

    @Data
    public static class Item {
        private String title;
        private String creator;
        private String collectionDb;
        private String description;
        private String regDate;
        private String rights;
    }
}