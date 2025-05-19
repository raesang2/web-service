package com.namestats.controller;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.namestats.dto.NaverBookDto;
import com.namestats.dto.RecommendedBookDto;
import com.namestats.service.RecommendedBookService;
import com.namestats.vo.RecommendedBook;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class RecommendedBookViewController {

	private final RecommendedBookService recommendedBookService;

    @GetMapping("/searchRecommendedBookPage")
    public String searchRecommendedBookPage(Model model) {

    	model.addAttribute("yearList", recommendedBookService.getYearList());
        model.addAttribute("targetList", recommendedBookService.getTargetList());
        
        return "searchRecommendedBook";
    }
    
    @GetMapping("/searchRecommendedBookPage/{year}")
    public String searchRecommendedBookPage(
            @PathVariable(required = false) String year, 
            Model model) {
    	
    	model.addAttribute("yearList", recommendedBookService.getYearList());
        model.addAttribute("targetList", recommendedBookService.getTargetList());
       
        if (year != null) {
            // ISBN 관련 처리 (예: 도서 정보 검색)
        	model.addAttribute("selectYear", year);
        }
        
        return "searchRecommendedBook";
    }
    
    @GetMapping("/searchRecommendedBook")
    public String searchRecommendedBook(RecommendedBookDto dto, Model model) {
    	
    	List<RecommendedBook> rbookList = recommendedBookService.searchRecommendedBooks(dto);
    	    	
        model.addAttribute("rbooklist", rbookList);
        model.addAttribute("totalCount", rbookList.size());
        return "recommendedBookTableFragment";
    }
}
