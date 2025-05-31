package com.namestats.vo;

import lombok.Data;

@Data
public class AiRecommendedBook {
	private Long id;
	private String reason;
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
    private String description;
}
