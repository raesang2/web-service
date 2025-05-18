package com.namestats.mapper;

import com.namestats.vo.KcisaRecommendedBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KcisaRecommendedBookMapper {
    void insertKcisaBook(KcisaRecommendedBook book);
    
    void deleteAllKcisaBook();
    
    KcisaRecommendedBook searchKcisaBook(String title);
}
