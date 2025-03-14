package com.namestats.mapper;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.namestats.vo.NameStatsVO;
import com.namestats.vo.SearchVO;
import com.namestats.vo.YearNameStatsVO;

@Mapper
public interface NameStatsMapper {

    void insertDailyNameStats(NameStatsVO NameStatsVO);

    void deleteYearNameStatsByTargetYear(Year year);
    
    void insertYearNameStats(Year year);
    
    List<YearNameStatsVO> findByTargetName(SearchVO searchVO);
    
    List<YearNameStatsVO> findByTargetYear(SearchVO searchVO);

	void deleteDailyNameStatsByTargetDate(LocalDate date);
	
	List<Integer> getTargetYear();
	
	LocalDate getRecentDataDate();
}
