package com.namestats.service;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.namestats.mapper.NameStatsMapper;
import com.namestats.vo.NameStatsVO;
import com.namestats.vo.SearchVO;
import com.namestats.vo.YearNameStatsVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NameStatsService {

	private final NameStatsMapper nameStatsMapper;
	
    // 일일 데이터 저장
    public void saveDailyNameStats(ArrayList<NameStatsVO> list) {
        nameStatsMapper.insertDailyNameStats(list);
    }
    
    // 연간 데이터 삭제
    public void deleteYearNameStats(Year year) {
        nameStatsMapper.deleteYearNameStatsByTargetYear(year);
    }
    
    // 일일 데이터를 연간 테이블에 적재
    public void insertYearNameStats(Year year) {
    	nameStatsMapper.insertYearNameStats(year);
    }
    
    // 일일 데이터 삭제
    public void deleteDailyNameStats(LocalDate date) {
        nameStatsMapper.deleteDailyNameStatsByTargetDate(date);
    }    

    // 이름으로 조회
    public List<YearNameStatsVO> getAllStatsByName(SearchVO searchVO) {
        return nameStatsMapper.findByTargetName(searchVO);
    }

    // 연도별 조회
    public List<YearNameStatsVO> getAllStatsByYear(SearchVO searchVO) {
        return nameStatsMapper.findByTargetYear(searchVO);
    }
    
    public List<Integer> getTargetYear(){
    	return nameStatsMapper.getTargetYear();
    }

    public LocalDate getRecentDataDate(){
    	return nameStatsMapper.getRecentDataDate();
    }
}
