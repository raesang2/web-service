// com.namestats.service.KcisaBookService.java
package com.namestats.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namestats.mapper.KcisaRecommendedBookMapper;
import com.namestats.vo.KcisaRecommendedBook;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KcisaRecommendedBookService {

    private final KcisaRecommendedBookMapper bookMapper;
    private final RestTemplate restTemplate;

	@Value("${api.kcisa-book-service-key}")
    private String serviceKey;

	@Transactional
    public void fetchAndSaveBooks() {
        int page = 1;
        int numOfRows = 1000;

        while (true) {
            String url = String.format(
                "http://api.kcisa.kr/openapi/service/rest/meta2/NLCFsase?serviceKey=%s&numOfRows=%d&pageNo=%d",
                serviceKey, numOfRows, page
            );
            
            String xmlResponse = restTemplate.getForObject(url, String.class);
            if (xmlResponse == null || xmlResponse.isEmpty()) break;

            List<KcisaRecommendedBook> books = parseJson(xmlResponse);
            if (books.isEmpty()) break;

            if(page == 1) {
            	bookMapper.deleteAllKcisaBook();
            }
            
            for (KcisaRecommendedBook book : books) {
                String id = generateId(book.getTitle(), book.getRegDate().toString());
                book.setId(id);
                bookMapper.insertKcisaBook(book);
            }

            if (books.size() < numOfRows) break;
            page++;
        }
    }

	private String generateId(String title, String regDate) {
	    String raw = title + "_" + regDate;
	    return DigestUtils.sha256Hex(raw);  // Apache Commons Codec 사용
	}

    private List<KcisaRecommendedBook> parseJson(String json) {
        List<KcisaRecommendedBook> books = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode itemArr = root.path("response").path("body").path("items").path("item");

            if (itemArr.isArray()) {
                for (JsonNode item : itemArr) {
                    KcisaRecommendedBook book = new KcisaRecommendedBook();
                    book.setTitle(item.path("title").asText(null));
                    book.setCreator(item.path("creator").asText(null));
                    book.setCollectionDb(item.path("collectionDb").asText(null));
                    book.setDescription(item.path("description").asText(null));
                    book.setRights(item.path("rights").asText(null));

                    String regDateStr = item.path("regDate").asText(null);
                    if (regDateStr != null && !regDateStr.isBlank()) {
                        book.setRegDate(LocalDateTime.parse(regDateStr, formatter));
                    }

                    books.add(book);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }
    
    public void deleteAllKcisaBook() {
    	bookMapper.deleteAllKcisaBook();
    }
    
    public KcisaRecommendedBook searchKcisaBook(String title) {
    	return bookMapper.searchKcisaBook(title);
    }
}
