package com.namestats.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.namestats.dto.PostpartumRegionDto;
import com.namestats.dto.PostpartumSearchDto;
import com.namestats.mapper.PostpartumCenterMapper;
import com.namestats.vo.PostpartumCenter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostpartumCenterService {

    private final PostpartumCenterMapper postpartumCenterMapper;

    public List<PostpartumCenter> searchCenters(PostpartumSearchDto dto) {
        return postpartumCenterMapper.findPostpartumCenters(dto);
    }
    
	public List<PostpartumRegionDto> selectPostpartumRegions(){
		return postpartumCenterMapper.selectPostpartumRegions();
	}	
}