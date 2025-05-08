package com.namestats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchResponse {
    private Response response;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private int pageNo;
        private int pageSize;
        private int numFound;
        private int resultNum;
        private List<DocWrapper> docs;  // ✅ doc 감싸는 wrapper
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DocWrapper {
        private Doc doc;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Doc {
        private String bookname;
        private String authors;
        private String publisher;
        private String publication_year;
        private String isbn13;
        private String addition_symbol;
        private String vol;
        private String class_no;
        private String class_nm;
        private String bookImageURL;
        private String bookDtlUrl;
        private int loan_count;
    }
}
