package com.namestats.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.namestats.vo.RecommendedBook;

@Mapper
public interface RecommendedBookMapper {
    void insertRecommendedBook(RecommendedBook book);
}