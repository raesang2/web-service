package com.namestats.service;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namestats.mapper.FacilityMapper;
import com.namestats.vo.Facility;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Service
@RequiredArgsConstructor
@Component
public class FacilityService {

    private final FacilityMapper facilityMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.facility-service-key}")
    private String SERVICE_KEY;
    private final int RECORDS_PER_PAGE = 100;

    public void fetchAndSaveFacilities() throws Exception {
        int totalPageCnt = getTotalPageCount();

        if(totalPageCnt > 0) {
        	facilityMapper.deleteAllFacility();	
        }
        
        for (int pageIndex = 1; pageIndex <= totalPageCnt; pageIndex++) {
            String apiUrl = buildUrl(pageIndex);
            String json = restTemplate.getForObject(new URI(apiUrl), String.class);
            JsonNode items = objectMapper.readTree(json)
                    .path("response").path("body").path("items");

            for (JsonNode item : items) {
                Facility facility = objectMapper.treeToValue(item, Facility.class);
                // rgnNm 넣기
                insertRgnNm(facility);
                // "서울특별시 제1호 시립 서울형 키즈카페" 위치 하드코딩
                if ("20014".equals(facility.getExfcSn())){
                	facility.setLatCrtsVl("37.5123677");
                	facility.setLotCrtsVl("126.9280281");
                }
                facilityMapper.insertFacility(facility);
            }
        }
    }

    private void insertRgnNm(Facility facility) {
        String url = "https://www.code.go.kr/stdcode/regCodeL.do?regionCd=" + facility.getRgnCd();

        try {
            // URL로부터 HTML 가져오기
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")  // 일부 서버는 User-Agent 없으면 차단
                    .timeout(5000)             // 타임아웃 설정
                    .get();

            // <td class="table_center01"> 요소 찾기
            Elements tds = doc.select("td.table_center01");
            String text = "";
            for (Element td : tds) {
                text = td.text();
                facility.setRgnNm(text);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private int getTotalPageCount() throws Exception {
        String apiUrl = buildUrl(1);
        String json = restTemplate.getForObject(new URI(apiUrl), String.class);
        return objectMapper.readTree(json)
                .path("response").path("body").path("totalPageCnt").asInt();
    }

    private String buildUrl(int pageIndex) {
        return "https://apis.data.go.kr/1741000/exfc5/getExfc5?" +
                "serviceKey=" + URLEncoder.encode(SERVICE_KEY, StandardCharsets.UTF_8) +
                "&pageIndex=" + pageIndex +
                "&recordCountPerPage=" + RECORDS_PER_PAGE;
    }

    public List<Facility> getFacilitiesByRegion(String regionName, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return facilityMapper.searchByRegionWithPaging(regionName, pageSize, offset);
    }

    public int getFacilityCountByRegion(String regionName) {
        return facilityMapper.countByRegion(regionName);
    }
}