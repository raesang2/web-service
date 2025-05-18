package com.namestats.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.namestats.dto.NaverBookDto;
import com.namestats.service.KcisaRecommendedBookService;
import com.namestats.service.NaverBookSearchService;
import com.namestats.service.RecommendedBookExcelImportService;
import com.namestats.vo.KcisaRecommendedBook;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecommededBookApiController {

	
    private final RecommendedBookExcelImportService recommendedBookExcelImportService;
    private final KcisaRecommendedBookService kcisaRecommendedBookService;
    private final NaverBookSearchService naverBookSearchService;
    
    @GetMapping("/excel-import-recommendedbooks")
    public String excelImportRecommendedBooks() {
    	recommendedBookExcelImportService.importExcelData();
        return "사서 추천 도서 excel import 완료";
    }

    @GetMapping("/api-import-recommendedbooks")
    public String apiImportRecommendedBooks() {
    	kcisaRecommendedBookService.fetchAndSaveBooks();
        return "사서 추천 도서 API import 완료";
    }

    @GetMapping("/searchKcisaBook")
    public ResponseEntity<KcisaRecommendedBook> searchKcisaBook(
            @RequestParam String title,
            @RequestParam String isbn) {

    	// title로는 db에서 추천도서 정보 얻어옴
    	KcisaRecommendedBook book = kcisaRecommendedBookService.searchKcisaBook(normalizeTitle(title));
    	
    	// isbn으로는 naver api로 이미지 주소 얻어옴
    	List <NaverBookDto> bookList = naverBookSearchService.searchBookByQuery(isbn);
    	
    	if(!bookList.isEmpty()) {
    		book.setImage(bookList.get(0).getImage());
    	}

        return ResponseEntity.ok(book);
    }
    	
	private String normalizeTitle(String title) {
	    return title == null ? "" :
	        title.trim()
	             .replaceAll("[ \\n\\t]", "")    // 공백, 줄바꿈, 탭 제거
	             .toLowerCase()
	             .replaceAll("[^가-힣a-z0-9]", "");  // 한글/영문/숫자 외 제거
	}
}