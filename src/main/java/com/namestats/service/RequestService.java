package com.namestats.service;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namestats.vo.NameStatsVO;
import com.namestats.vo.OriginParamVO;

@Service
public class RequestService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String URL = "https://efamily.scourt.go.kr/ds/report/query.do"; // 요청 URL
    
    public List<NameStatsVO> getOriginNameData(OriginParamVO originParamVO) {
    	// 1. 원천 데이터 조회
    	String response = this.sendPostRequest(originParamVO);
    	
    	// 2. class 로 변환하여 리턴
    	List<OriginReturnData> originReturnDataList= parseJsonResponse(response);
    	
    	// 3. NameStatsVOList 로 변환
    	List<NameStatsVO> nameStatsVOList = null;
    	if(originReturnDataList != null && !originReturnDataList.isEmpty()) {
    		nameStatsVOList = convertToStatsList(originReturnDataList, originParamVO);
    	}
    	
    	return nameStatsVOList;
    }
    
    private List<NameStatsVO> convertToStatsList(List<OriginReturnData> originReturnDataList, OriginParamVO originParamVO) {
    	List<NameStatsVO> nameStatsVOList = new ArrayList<NameStatsVO>();
    	NameStatsVO nameStatsVO = null;
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    	
    	for(OriginReturnData originData : originReturnDataList) {
    		nameStatsVO = new NameStatsVO();
    		
    		// 1. 날짜 넣기
    		// 연도일 경우
    		if("YY".equals(originParamVO.getMultiCandTypeValue())) {
    			nameStatsVO.setTargetYear(Year.parse(originParamVO.getMultiCandStDtValue()));
    		}
    		// 일자일 경우
    		else {
    	        // "yyyyMMdd" 형식으로 파싱하기 위한 DateTimeFormatter 사용
    			nameStatsVO.setTargetDate(LocalDate.parse(originParamVO.getMultiCandStDtValue(), formatter));
    		}
    		
    		// 2. 이름 넣기
    		nameStatsVO.setTargetName(originData.get이름());
    		
    		// 3. 성별에 맞춰서 count 넣기(1: 남자 / 2: 여자)
    		if("1".equals(originParamVO.getGenderCdValue())){
    			nameStatsVO.setTargetGender("M");
    		}else {
    			nameStatsVO.setTargetGender("F");
    		}
    		
    		// 4. COUNT 넣기
    		nameStatsVO.setTargetCount(originData.get건수());
    		
    		// 5. SIDO 넣기
    		nameStatsVO.setTargetSido(originParamVO.getSidoCdValue());
    		
    		// 6. CGG 넣기
    		nameStatsVO.setTargetCgg(originParamVO.getCggCdValue());
    		
    		nameStatsVOList.add(nameStatsVO);
		}
    	
    	
    	return nameStatsVOList;
	}

	private String sendPostRequest(OriginParamVO originParamVO) {
        // form-urlencoded 데이터 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        
        // ParamsData 객체 생성
        ParamsData paramsData = new ParamsData();
        
        // 값 설정 (setter 사용)
        paramsData.setMultiCandTypeValue(originParamVO.getMultiCandTypeValue());
        paramsData.setMultiCandStDtValue(originParamVO.getMultiCandStDtValue());
        paramsData.setMultiCandEdDtValue(originParamVO.getMultiCandEdDtValue());
        paramsData.setGenderCdValue(originParamVO.getGenderCdValue());
        paramsData.setSidoCdValue(originParamVO.getSidoCdValue());
        paramsData.setCggCdValue(originParamVO.getCggCdValue());
        
        
        // JSON 출력
        String updatedParamsJson = paramsData.getParamsJson();
        
        params.add("dsid", "1261");
        params.add("dstype", "DS");
        params.add("sqlid", "1811-1");        
        params.add("params",updatedParamsJson);        

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // x-www-form-urlencoded 타입 설정

        // POST 요청 보내기
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);

		//"data" 부분만 추출
		// Jackson ObjectMapper 객체 생성
		ObjectMapper objectMapper = new ObjectMapper();
		
		// JSON을 JsonNode로 변환
		JsonNode rootNode;
		JsonNode dataNode = null;
		try {
			rootNode = objectMapper.readTree(response.getBody());
			dataNode = rootNode.path("data");
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// JSON 응답을 파싱하여 반환
		if(dataNode != null) {
			return dataNode.toString();
		}else {
			return "";
		}
    }

    private List<OriginReturnData> parseJsonResponse(String jsonResponse) {
        // Jackson의 ObjectMapper를 사용하여 JSON 응답을 Java 객체로 변환
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
        	// JSON을 List<RankingData>로 변환
        	List<OriginReturnData> originReturnDataList = objectMapper.readValue(jsonResponse, new TypeReference<List<OriginReturnData>>() {});

        	return originReturnDataList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}