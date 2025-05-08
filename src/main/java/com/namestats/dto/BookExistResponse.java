package com.namestats.dto;

import lombok.Data;

@Data
public class BookExistResponse {
    private BookExistResponseWrapper response;

    @Data
    public static class BookExistResponseWrapper {
        private Request request;
        private Result result;
    }

    @Data
    public static class Request {
        private String isbn13;
        private String libCode;
    }

    @Data
    public static class Result {
        private String hasBook;       // "Y" or "N"
        private String loanAvailable; // "Y" or "N"
    }
}
