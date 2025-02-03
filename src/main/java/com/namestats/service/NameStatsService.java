package com.namestats.service;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.namestats.mapper.NameStatsMapper;
import com.namestats.vo.NameStatsVO;
import com.namestats.vo.SearchVO;
import com.namestats.vo.YearNameStatsVO;

@Service
public class NameStatsService {

    @Autowired
    private NameStatsMapper nameStatsMapper;

    // 일일 데이터 저장
    public void saveDailyNameStats(NameStatsVO NameStatsVO) {
        nameStatsMapper.insertDailyNameStats(NameStatsVO);
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

    // 연도/이름으로 조회
    public List<YearNameStatsVO> getAllNameStatsByYear(SearchVO searchVO) {
        return nameStatsMapper.findByTargetYearName(searchVO);
    }
    
    public List<Integer> getTargetYear(){
    	return nameStatsMapper.getTargetYear();
    }
}
