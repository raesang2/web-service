package com.namestats.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class KcisaRecommendedBook {
    private Long id;
    private String title;
    private String creator;
    private LocalDateTime regDate;
    private String collectionDb;
    private String description;
    private String rights;
    private String image;
}
