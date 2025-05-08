package com.namestats.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.namestats.dto.RegionDto;

@Mapper
public interface RegionMapper {
    List<RegionDto> selectAllRegions();
}