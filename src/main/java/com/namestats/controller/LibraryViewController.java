package com.namestats.controller;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.namestats.dto.LibSearchRequest;
import com.namestats.dto.LibraryResponse;
import com.namestats.dto.NaverBookDto;
import com.namestats.service.LibrarySearchService;
import com.namestats.service.NaverBookSearchService;
import com.namestats.service.RegionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class LibraryViewController {

	private final RegionService regionService;
    private final LibrarySearchService librarySearchService;
    private final NaverBookSearchService  naverBookSearchService ;

    @GetMapping("/searchLibraryPage")
    public String searchLibraryPage(Model model) {
        // 대분류 지역 리스트를 모델에 추가
        model.addAttribute("regionList", regionService.selectAllRegions());
        return "searchLibrary";
    }

    @GetMapping("/searchLibraryPage/{isbn}")
    public String searchLibraryPageTitle(
            @PathVariable(required = false) String isbn, 
            @RequestParam(required = false) String title,
            Model model) {

        // 대분류 지역 리스트를 모델에 추가
        model.addAttribute("regionList", regionService.selectAllRegions());
        
        // isbn, region, dtlRegion이 존재하는 경우에만 처리
        if (isbn != null) {
            // ISBN 관련 처리 (예: 도서 정보 검색)
            model.addAttribute("isbn", isbn);
        }
        if (title != null) {
            model.addAttribute("title", title);
        }        
        // 페이지를 위한 데이터 추가
        return "searchLibrary";
    }
    
    @GetMapping("/searchLibraryPage/{isbn}/{region}")
    public String searchLibraryPage(
            @PathVariable(required = false) String isbn, 
            @PathVariable(required = false) String region, 
            Model model) {

        // 대분류 지역 리스트를 모델에 추가
        model.addAttribute("regionList", regionService.selectAllRegions());
        
        // isbn, region, dtlRegion이 존재하는 경우에만 처리
        if (isbn != null) {
            // ISBN 관련 처리 (예: 도서 정보 검색)
            model.addAttribute("isbn", isbn);
        }
        if (region != null) {
            // region 관련 처리 (예: 해당 지역에 맞는 도서관 목록 필터링)
            model.addAttribute("region", region);
        }
        // 페이지를 위한 데이터 추가
        return "searchLibrary";
    }
    
    @GetMapping("/searchLibraryPage/{isbn}/{region}/{dtlRegion}")
    public String searchLibraryPage(
            @PathVariable(required = false) String isbn, 
            @PathVariable(required = false) String region, 
            @PathVariable(required = false) String dtlRegion, 
            Model model) {

        // 대분류 지역 리스트를 모델에 추가
        model.addAttribute("regionList", regionService.selectAllRegions());
        
        // isbn, region, dtlRegion이 존재하는 경우에만 처리
        if (isbn != null) {
            // ISBN 관련 처리 (예: 도서 정보 검색)
            model.addAttribute("isbn", isbn);
        }
        if (region != null) {
            // region 관련 처리 (예: 해당 지역에 맞는 도서관 목록 필터링)
            model.addAttribute("region", region);
        }
        if (dtlRegion != null) {
            // 세부 지역 처리 (예: 해당 세부 지역에 맞는 도서관 목록 필터링)
            model.addAttribute("dtlRegion", dtlRegion);
        }

        // 페이지를 위한 데이터 추가
        return "searchLibrary";
    }


    @GetMapping("/selectLibraryPage")
    public String selectLibraryPage(LibSearchRequest req, Model model) {
    	
    	List<LibraryResponse.LibWrapper> libraries = librarySearchService.searchAllLibraries(req);
    	
    	NaverBookDto naverBookDto = new NaverBookDto();
    	List<NaverBookDto> naverBookDtoList = naverBookSearchService.searchBookByQuery(req.getIsbn());
    	if(naverBookDtoList != null && !naverBookDtoList.isEmpty()) {
    		naverBookDto = naverBookDtoList.get(0);
    	}
    	
        model.addAttribute("libraries", libraries);
        model.addAttribute("isbn", req.getIsbn());
        model.addAttribute("book", naverBookDto);
        return "libraryTableFragment";
    }
}
