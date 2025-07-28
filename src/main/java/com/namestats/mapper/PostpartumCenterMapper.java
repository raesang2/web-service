package com.namestats.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.namestats.dto.PostpartumRegionDto;
import com.namestats.dto.PostpartumSearchDto;
import com.namestats.vo.LocalDataPostpartumCenter;
import com.namestats.vo.MohwPostpartumCenter;
import com.namestats.vo.PostpartumCenter;

@Mapper
public interface PostpartumCenterMapper {
    void insertLocalDataPostpartumCenter(LocalDataPostpartumCenter localDataPostpartumCenter);

    void deleteAllLocalDataPostpartumCenters();
    
    void insertMohwPostpartumCenter(MohwPostpartumCenter mohwPostpartumCenter);

    void deleteAllMohwPostpartumCenters();
    
    List<PostpartumCenter> findPostpartumCenters(PostpartumSearchDto dto);
    
    List<PostpartumRegionDto> selectPostpartumRegions();
}