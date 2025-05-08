package com.namestats.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.namestats.dto.RegionDto;
import com.namestats.mapper.RegionMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegionService {

	private final RegionMapper regionMapper;

	public List<RegionDto> selectAllRegions(){
		return regionMapper.selectAllRegions();
	}

}