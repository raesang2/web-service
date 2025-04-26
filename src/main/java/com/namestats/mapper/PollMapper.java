package com.namestats.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.namestats.vo.PollDetailVO;
import com.namestats.vo.PollMasterVO;

@Mapper
public interface PollMapper {

	void insertPollMaster(PollMasterVO pollMasterVO);
	
	void insertPollDetail(PollDetailVO pollDetailVO);
	
    PollMasterVO getPollMasterByPollMasterId(String pollMasterId);
    
    List<PollDetailVO> getPollDetailByPollMasterId(String pollMasterId);

    void updatePollItemCount(PollDetailVO pollDetailVO);
    
    List<String> getPollMasterId();
}
