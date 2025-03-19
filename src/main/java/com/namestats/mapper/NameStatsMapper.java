package com.namestats.mapper;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.namestats.vo.NameStatsVO;
import com.namestats.vo.SearchVO;
import com.namestats.vo.SidoCggCodeVO;
import com.namestats.vo.YearNameStatsVO;

@Mapper
public interface NameStatsMapper {

    void insertDailyNameStats(ArrayList<NameStatsVO> list);

    void deleteYearNameStatsByTargetYear(Year year);
    
    void insertYearNameStats(Year year);
    
    List<YearNameStatsVO> findByTargetName(SearchVO searchVO);
    
    List<YearNameStatsVO> findByTargetYear(SearchVO searchVO);

	void deleteDailyNameStatsByTargetDate(LocalDate date);
	
	List<Integer> getTargetYear();
	
	LocalDate getRecentDataDate();
	
	void insertSidoCggCode(List<SidoCggCodeVO> list);
	
	void deleteSidoCggCode();
	
	List<SidoCggCodeVO> getSidoCggCode();
}
