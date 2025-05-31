package com.namestats.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.namestats.service.AiRecommendedBookService;
import com.namestats.vo.AiRecommendedBook;
import com.namestats.vo.KcisaRecommendedBook;

@Controller
public class AiRecommendedBookController {

    @Autowired
    private AiRecommendedBookService service;

    @GetMapping("/aiRecommendedBookPage")
    public String aiRecommendedBookPage() {
        return "aiRecommendedBookPage";
    }

    @GetMapping("/aiRecommendedBook")
    public String aiRecommendedBook(@RequestParam String question, Model model) {
        List<AiRecommendedBook> similarBooks = service.getSimilarBooks(question);
        List<AiRecommendedBook> aiRecommendedBook = service.askGroqApi(similarBooks, question); 

        model.addAttribute("question", question);
        model.addAttribute("books", aiRecommendedBook);
        
        return "aiRecommendedbookTableFragment";
    }
}
