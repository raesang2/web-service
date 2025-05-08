package com.namestats.service;

import java.util.ArrayList;
import java.util.List;

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
import com.namestats.mapper.NameStatsMapper;
import com.namestats.vo.SidoCggCodeVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodeRequestService {

    private final RestTemplate restTemplate;
    
    private final NameStatsMapper nameStatsMapper;

    private static final String URL = "https://efamily.scourt.go.kr/ds/report/condition/query.do"; // 요청 URL
    
    public List<SidoCggCodeVO> updateSidoCggCode(String[] cidoArr) {
    	// 1. 원천 데이터 조회
    	String response = this.sendPostRequest(cidoArr);
    	
    	// 2. class 로 변환하여 리턴
    	List<CodeOriginReturnData> originReturnDataList= parseJsonResponse(response);
    	
    	// 3. CodeList 로 변환
    	List<SidoCggCodeVO> codeVOList = convertToCodeList(originReturnDataList);
    	
    	// 4. code table 에 저장
    	// 4-1. code table delete
    	nameStatsMapper.deleteSidoCggCode();
    	// 4-2. code table 에 insert
    	nameStatsMapper.insertSidoCggCode(codeVOList);
    	return codeVOList;
    }
    
    private List<SidoCggCodeVO> convertToCodeList(List<CodeOriginReturnData> originReturnDataList) {
    	List<SidoCggCodeVO> codeVOList = new ArrayList<SidoCggCodeVO>();
    	SidoCggCodeVO codeVO = null;
    	String cgg = "";
    	
    	for(CodeOriginReturnData originData : originReturnDataList) {
    		codeVO = new SidoCggCodeVO();
    		
    		// 1. key_value에서 cgg에 넣기
    		cgg = originData.getKEY_VALUE();
    		codeVO.setCggCd(originData.getKEY_VALUE());

    		// 2. key_value에서 앞 두글자 잘라서 sido
    		codeVO.setSidoCd(cgg.substring(0, 2));

    		codeVOList.add(codeVO);
		}
    	
    	return codeVOList;
	}

	private String sendPostRequest(String[] cidoArr) {
        // form-urlencoded 데이터 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        
        // ParamsData 객체 생성
        CodeParamsData codeParamsData = new CodeParamsData();
        
        // 값 설정 (setter 사용)
        codeParamsData.setSidoCdValue(cidoArr);
                
        // JSON 출력
        String updatedParamsJson = codeParamsData.getParamsJson();
     
        params.add("DS_ID", "1261");
        params.add("PARAM_TYPE", "LIST");
        params.add("DATASRC_TYPE","QUERY");
        params.add("KEY_VALUE_ITEM","KEY_VALUE");
        params.add("CAPTION_VALUE_ITEM","TEXT_VALUE");
        params.add("DATASRC","7AwMbl4UusCBNKRaiZd14gJXSfmqRbtGt88XuKeBhFIjkWhHvNqiE5YMeSjTuETNUgaFrDqd6EUdZDfmemC2N8GB6bQaVFPC9SSaLgKTphSzrAuli1KNkpRO2OtrZCq5aHnTmBRZT0HpLTaDIRwQJKXnoKfJozSwIOH/JAodREMUg6yZedNo+wprGN4vrQEdqlZghHcO6UPQB8QrOODTD0cbTx+MlDiZCcK5DY1ftVg2lzm/doCVxYun05qlzcNoQrlm2LKbcOQS9CT6YUro/mfKwJ7r7JiXiWwMtDszRpI=");        
        params.add("parameterValues",updatedParamsJson);
        
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

    private List<CodeOriginReturnData> parseJsonResponse(String jsonResponse) {
        // Jackson의 ObjectMapper를 사용하여 JSON 응답을 Java 객체로 변환
        try {
        	ObjectMapper objectMapper = new ObjectMapper();

        	List<CodeOriginReturnData> originReturnDataList = objectMapper.readValue(jsonResponse, new TypeReference<List<CodeOriginReturnData>>() {});

        	return originReturnDataList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<SidoCggCodeVO> getSidoCggCode()
    {
    	return nameStatsMapper.getSidoCggCode();
    }
}