package com.namestats.controller;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namestats.service.CodeRequestService;
import com.namestats.service.NameStatsService;
import com.namestats.service.RequestService;
import com.namestats.vo.NameStatsVO;
import com.namestats.vo.OriginParamVO;
import com.namestats.vo.SearchVO;
import com.namestats.vo.SidoCggCodeVO;
import com.namestats.vo.YearNameStatsVO;

@RestController
@RequestMapping("/api")
public class NameStatsController {

    @Autowired
    private NameStatsService nameStatsService;
    
    @Autowired
    private RequestService requestService;

    @Autowired
    private CodeRequestService codeRequestService;

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
    	if(resultList != null && !resultList.isEmpty()) {
    		this.updateDailyData(resultList, date);
    	}
    	
    	// 3. 신규로 들어온 일일 데이터로 연간 데이터 테이블 갱신
    	this.updateYearData(year);
    	
        return "Data saved successfully!";
    }
    
    private void updateDailyData(ArrayList<NameStatsVO> resultList, LocalDate date) {
    	// 1. 오늘 데이터 delete
    	nameStatsService.deleteDailyNameStats(date);
    	
    	// 2. 오늘 데이터 insert
    	nameStatsService.saveDailyNameStats(resultList);
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
    	List<SidoCggCodeVO> sidoCggCodeVOList = codeRequestService.getSidoCggCode();
    	List<NameStatsVO> originNameData = null;
    	
    	// 1. 오늘날짜로 세팅
    	originParamVO.setMultiCandTypeValue("DT");
    	originParamVO.setMultiCandStDtValue(today);
    	originParamVO.setMultiCandEdDtValue(today);

    	// 2. 성별로 2번 반복
    	for(Integer gender : genderArr) {
    		originParamVO.setGenderCdValue(Integer.toString(gender));
    		// 3. sido/cgg별로 반복
    		for(SidoCggCodeVO sidoCggCodeVO : sidoCggCodeVOList) {
    			originParamVO.setSidoCdValue(sidoCggCodeVO.getSidoCd());
    			originParamVO.setCggCdValue(sidoCggCodeVO.getCggCd());
    			originNameData = requestService.getOriginNameData(originParamVO);
    			if(originNameData != null && !originNameData.isEmpty()) {
    				resultList.addAll(originNameData);
    			}
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
        	System.out.println("start " + todayString);
        	
        	// 1. 원천 데이터 조회
        	ArrayList<NameStatsVO> resultList = this.getDailyNameStatsList(todayString);
        	// 2. table에 insert
        	if(resultList != null && !resultList.isEmpty()) {
        		this.updateDailyData(resultList, start);
        	}
        	System.out.println("end" + todayString);
        	start = start.plusDays(1); // 하루씩 증가

        }
        // 3. 신규로 들어온 일일 데이터로 연간 데이터 테이블 갱신
        this.updateYearData(year);
        
        return "Bulk Data saved successfully!";
    }
    
    @GetMapping(value="/getMoreYearStats", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getStatsByGender(SearchVO searchVO) {
    	// 성별 통합 조회
    	searchVO.setTargetGender("T");
    	List<YearNameStatsVO> tResults = nameStatsService.getAllStatsByYear(searchVO);
    	
    	// 남 조회
    	searchVO.setTargetGender("M");
    	List<YearNameStatsVO> mResults = nameStatsService.getAllStatsByYear(searchVO);
    	
    	// 여 조회
    	searchVO.setTargetGender("F");
    	List<YearNameStatsVO> fResults = nameStatsService.getAllStatsByYear(searchVO);

        // 결과를 맵에 담아서 반환
        Map<String, Object> result = new HashMap<>();
        result.put("tResults", tResults);
        result.put("mResults", mResults);
        result.put("fResults", fResults);

        // 200 OK 응답과 함께 결과 반환
        return ResponseEntity
                .ok()  // HTTP 상태 코드 200 OK
                .header("Content-Type", "application/json")
                .body(result);
    }
    
    @PostMapping("/updateSidoCggCode")
    public String updateSidoCggCode() {
    	codeRequestService.updateSidoCggCode(cidoArr);
        
        return "Sido Cgg Code updated successfully!";
    }    
}
