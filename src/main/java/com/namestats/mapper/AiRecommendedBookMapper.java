package com.namestats.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.namestats.vo.AiRecommendedBook;

@Mapper
public interface AiRecommendedBookMapper {
    AiRecommendedBook aiSearchRecommendedBook(Long bookId);

}