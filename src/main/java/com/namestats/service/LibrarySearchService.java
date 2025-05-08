package com.namestats.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.namestats.dto.LibSearchRequest;
import com.namestats.dto.LibraryResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LibrarySearchService {

	private final RestTemplate restTemplate;
	private final String BASE_URL = "http://data4library.kr/api/libSrchByBook";
	@Value("${api.lib-service-key}")
	private String libServiceKey;
	
	public List<LibraryResponse.LibWrapper> searchAllLibraries(LibSearchRequest request) {
		int pageNo = 1;
		int pageSize = request.getPageSize() != null ? request.getPageSize() : 20;
		List<LibraryResponse.LibWrapper> allLibs = new ArrayList<>();

		try {
			while (true) {
				// URI 빌드
				UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
				        .queryParam("authKey", libServiceKey)
				        .queryParam("isbn", request.getIsbn())
				        .queryParam("region", request.getRegion())
				        .queryParam("pageNo", pageNo)
				        .queryParam("pageSize", pageSize)
				        .queryParam("format", request.getFormat() == null ? "json" : request.getFormat());

				if (request.getDtlRegion() != null && !request.getDtlRegion().isBlank()) {
				    builder.queryParam("dtl_region", request.getDtlRegion());
				}

				String uri = builder.build().toUriString();

				// 헤더 설정 (Accept: application/json)
				HttpHeaders headers = new HttpHeaders();
				headers.set("Accept", "*/*");

				// REST 요청 전송 (exchange 사용)
				ResponseEntity<LibraryResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
						new org.springframework.http.HttpEntity<>(headers), LibraryResponse.class);

				LibraryResponse libraryResponse = responseEntity.getBody();

				// 응답 처리
				if (libraryResponse == null || libraryResponse.getResponse() == null) {
					break;  // 응답이 없으면 종료
				}

				LibraryResponse.Response response = libraryResponse.getResponse();
				if (response.getLibs() == null) {
					break;  // 라이브러리 정보가 없으면 종료
				}

				// 라이브러리 목록을 allLibs에 추가
				allLibs.addAll(response.getLibs());

				// 다음 페이지를 요청할지 여부 결정
				int numFound = response.getNumFound();
				if (pageNo * pageSize >= numFound) {
					break;  // 더 이상 페이지가 없으면 종료
				}

				// 페이지 번호 증가
				pageNo++;
			}
		} catch (HttpClientErrorException e) {
			System.out.println("HTTP 오류 발생: " + e.getStatusCode());
			// 예외 처리 로직 (예: 로그 출력, 사용자에게 알림 등)
		} catch (Exception e) {
			e.printStackTrace();  // 기타 예외 처리
		}

		return allLibs;
	}
}