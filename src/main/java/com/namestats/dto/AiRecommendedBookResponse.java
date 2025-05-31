package com.namestats.dto;

import java.util.List;

import com.namestats.vo.AiRecommendedBook;

import lombok.Data;

@Data
public class AiRecommendedBookResponse {
	 private List<AiRecommendedBook> result;
}
