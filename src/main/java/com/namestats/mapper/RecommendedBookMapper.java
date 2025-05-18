package com.namestats.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.namestats.dto.RecommendedBookDto;
import com.namestats.vo.RecommendedBook;

@Mapper
public interface RecommendedBookMapper {
    void insertRecommendedBook(RecommendedBook book);

    void deleteAllRecommendedBooks();
    
    List<RecommendedBook> searchRecommendedBooks(RecommendedBookDto dto);

    List<String> getDistinctYears();

    List<String> getDistinctTargets();
}