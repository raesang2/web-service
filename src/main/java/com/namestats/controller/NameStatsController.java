package com.namestats.controller;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namestats.service.NameStatsService;
import com.namestats.service.RequestService;
import com.namestats.vo.NameStatsVO;
import com.namestats.vo.OriginParamVO;
import com.namestats.vo.SearchVO;
import com.namestats.vo.YearNameStatsVO;

@RestController
@RequestMapping("/api")
public class NameStatsController {

    @Autowired
    private NameStatsService nameStatsService;
    
    @Autowired
    private RequestService requestService;

    private static Integer[] genderArr = { 1, 2 };
    
    private static String[] cidoArr = {
            "11", "26", "27", "28", "29", "30", "31", "36", "41", "43","44", "46", "47", "48", "50", "51", "52", "21", "22", "23", "24", "25", "42", "45"
        };
   
    
    @PostMapping("/insertDailyNameStats")
    public String insertDailyNameStats() {
    	// 날짜 세팅
        String today = "20250105";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(today, formatter);    	
    	Year year = Year.parse(today.substring(0, 4));
    	
    	// 1. 원천 데이터 조회
    	ArrayList<NameStatsVO> resultList = this.getDailyNameStatsList(today);
    	
    	// 2. table에 insert
    	this.updateDailyData(resultList, date);
    	
    	// 3. 신규로 들어온 일일 데이터로 연간 데이터 테이블 갱신
    	this.updateYearData(year);
    	
        return "Data saved successfully!";
    }
    
    private void updateDailyData(ArrayList<NameStatsVO> resultList, LocalDate date) {
    	// 1. 오늘 데이터 delete
    	nameStatsService.deleteDailyNameStats(date);
    	
    	// 2. 오늘 데이터 insert
    	for(NameStatsVO nameStatsVO : resultList) {
    		nameStatsService.saveDailyNameStats(nameStatsVO);
    	}
    }
    
    private void updateYearData(Year year) {
    	// 1. 오늘 기준 연도 데이터 삭제
    	nameStatsService.deleteYearNameStats(year);
    	
    	// 2. 신규 데이터로 연간 데이터 갱신
    	nameStatsService.insertYearNameStats(year);
    }
    
    private ArrayList<NameStatsVO> getDailyNameStatsList(String today){
    	ArrayList<NameStatsVO> resultList = new ArrayList<NameStatsVO>();

    	OriginParamVO originParamVO = new OriginParamVO();

    	// 1. 오늘날짜로 세팅
    	originParamVO.setMultiCandTypeValue("DT");
    	originParamVO.setMultiCandStDtValue(today);
    	originParamVO.setMultiCandEdDtValue(today);

    	// 2. 성별로 2번 반복
    	for(Integer gender : genderArr) {
    		originParamVO.setGenderCdValue(Integer.toString(gender));
    		// 3. 시도별로 반복
    		for(String cido : cidoArr) {
    			originParamVO.setSidoCdValue(cido);
    			resultList.addAll(requestService.getOriginNameData(originParamVO));
    		}
    	}
    	
    	return resultList;
    }
    
    @PostMapping("/bulkInsertDailyNameStats")
    public String bulkInsertDailyNameStats(String startDate, String endDate) {
        // 시작 날짜와 끝 날짜
//        String startDate = "20230101";
//        String endDate 	 = "20231231";
        
        // DateTimeFormatter를 사용해 "yyyyMMdd" 형식으로 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        
        // 시작 날짜와 끝 날짜 LocalDate로 변환
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        
        Year year = Year.parse(startDate.substring(0, 4));
        // 날짜가 endDate까지 반복
        while (!start.isAfter(end)) {
        	// 날짜 세팅
        	String todayString = start.format(formatter);
        	System.out.println(todayString);
        	
        	// 1. 원천 데이터 조회
        	ArrayList<NameStatsVO> resultList = this.getDailyNameStatsList(todayString);
        	// 2. table에 insert
        	this.updateDailyData(resultList, start);
        	
        	
        	start = start.plusDays(1); // 하루씩 증가

        }
        // 3. 신규로 들어온 일일 데이터로 연간 데이터 테이블 갱신
        this.updateYearData(year);
        
        return "Bulk Data saved successfully!";
    }
    
    
}
