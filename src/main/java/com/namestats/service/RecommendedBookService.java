package com.namestats.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.namestats.dto.RecommendedBookDto;
import com.namestats.mapper.RecommendedBookMapper;
import com.namestats.vo.RecommendedBook;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendedBookService {
	private final RecommendedBookMapper recommendedBookMapper;
	private final static String BLANK = " ";
	
	public List<RecommendedBook> searchRecommendedBooks(RecommendedBookDto dto){
		List<RecommendedBook> rbookList = recommendedBookMapper.searchRecommendedBooks(dto);
		
		// isbn이 여러개 들어있는 경우 공백으로 구분 후 두번째 리턴(보통 첫번째는 세트본 isbn이 들어있음)
		String isbn = "";
		for(RecommendedBook rbook : rbookList) {
			isbn = rbook.getIsbn();
			
			if(!isbn.isEmpty() && isbn.contains(BLANK)){
				String[] isbnStr = isbn.split(BLANK);
				if(isbnStr.length >= 2) {
					isbn = isbnStr[1];
				}else {
					isbn = isbnStr[0];
				}
				rbook.setIsbn(isbn);
			}
			
		}
		return rbookList;
	}
	
	public List<String> getYearList(){
    	return recommendedBookMapper.getDistinctYears();
    }

    public List<String> getTargetList(){
    	return recommendedBookMapper.getDistinctTargets();
    }

}
