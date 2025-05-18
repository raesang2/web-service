package com.namestats.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedBook {
    private String recommendationMonth;
    private String theme;
    private String targetAudience;
    private String title;
    private String subjectCategory;
    private String author;
    private String publisher;
    private Integer publicationYear;
    private String isbn;
    private String callNumber;
    private String keyword;

}