package com.namestats.service;


import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namestats.mapper.AiRecommendedBookMapper;
import com.namestats.vo.AiRecommendedBook;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiRecommendedBookService {

    @Value("${api.groq-api-key}")
    private String groqApiKey;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String pythonServerUrl = "http://localhost:8000/similar-books";

    private final AiRecommendedBookMapper aiRecommendedBookMapper;
    
    public List<AiRecommendedBook> getSimilarBooks(String question) {
        try {
            String url = pythonServerUrl + "?question=" + URLEncoder.encode(question, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JSONArray jsonArr = new JSONArray(response.body());
            List<AiRecommendedBook> books = new ArrayList<>();

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject obj = jsonArr.getJSONObject(i);
                AiRecommendedBook book = new AiRecommendedBook();
                book.setId(obj.getLong("id"));
                book.setTitle(obj.getString("title"));
                book.setDescription(obj.getString("description"));
                books.add(book);
            }
            return books;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<AiRecommendedBook> askGroqApi(List<AiRecommendedBook> books, String question) {
    	try {
            StringBuilder context = new StringBuilder();
            context.append("아래 컨텍스트는 도서관 사서가 추천해준 책 제목, 추천 내용입니다. 아래 컨텍스트를 바탕으로 질문에 답변하세요 :");
            for (AiRecommendedBook book : books) {
                context
                	.append("id: ")
                	.append(book.getId())
                	.append("\n책 제목: ")
                	.append(book.getTitle())
                	.append("\n추천 내용: ")
                	.append(book.getDescription()).append("\n\n");
            }
            context.append("답변은 책id는 id 필드, 책제목은 title 필드, 추천 이유는 reason 필드명하고, 필드명에는 공백이 없게 해서 최대 5건, 결과는 result 이름의 json array 만 주세요. ");
            context.append("always respond with valid JSON objects that match this structure:");
            context.append("\"result\":[{\"id\": \"string\",\"title\": \"string\",\"reason\": \"string\" }]");
            context.append("Your response should ONLY contain the JSON object and nothing else.");
            context.append("질문: ");
            String prompt = context.toString() +  question;

            JSONObject bodyJson = new JSONObject();
            bodyJson.put("model", "llama3-70b-8192");
            //bodyJson.put("response_format", "{\"type\": \"json_object\"}");
            JSONArray messages = new JSONArray();
            messages.put(new JSONObject().put("role", "system").put("content", "도움이 되는 도우미"));
            messages.put(new JSONObject().put("role", "user").put("content", prompt));
            bodyJson.put("messages", messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                    .header("Authorization", "Bearer " + groqApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(bodyJson.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject resJson = new JSONObject(response.body());
            String jsonStr = resJson.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            
            return convertToList(jsonStr);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<AiRecommendedBook>();
        }
    }
    
    private List<AiRecommendedBook> convertToList(String rawText) {
        List<AiRecommendedBook> resultList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            // 1. "result: ["로 시작하는 배열 부분만 추출
            int startIndex = rawText.indexOf("result\":");
            if (startIndex == -1) {
                System.out.println("result 키워드를 찾을 수 없습니다.");
                return resultList;
            }

            String resultPart = rawText.substring(startIndex + 7).trim(); // "result:" 다음 부분부터

            // 2. 배열만 남기기 (여는 [, 닫는 ] 위치 찾기)
            int arrayStart = resultPart.indexOf('[');
            int arrayEnd = resultPart.lastIndexOf(']');

            if (arrayStart == -1 || arrayEnd == -1 || arrayEnd <= arrayStart) {
                System.out.println("유효한 JSON 배열을 찾을 수 없습니다.");
                return resultList;
            }

            String jsonArrayStr = resultPart.substring(arrayStart, arrayEnd + 1).trim();

            // 3. JSON 배열 파싱
            List<AiRecommendedBook> aiBookList = mapper.readValue(
                jsonArrayStr,
                new TypeReference<List<AiRecommendedBook>>() {}
            );

            // 4. DB 조회 및 reason 추가
            for (AiRecommendedBook aiBook : aiBookList) {
                AiRecommendedBook dbBook = aiRecommendedBookMapper.aiSearchRecommendedBook(aiBook.getId());
                if (dbBook != null) {
                    dbBook.setReason(aiBook.getReason());
                    resultList.add(dbBook);
                }
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return resultList;
    }

}
