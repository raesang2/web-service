package com.namestats.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.namestats.vo.Facility;

@Mapper
public interface FacilityMapper {
    void insertFacility(Facility facility);
    List<Facility> searchByRegionWithPaging(
            @Param("regionName") String regionName,
            @Param("limit") int limit,
            @Param("offset") int offset);

    int countByRegion(@Param("regionName") String regionName);
    
    int deleteAllFacility();
}