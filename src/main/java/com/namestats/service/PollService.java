package com.namestats.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.namestats.mapper.PollMapper;
import com.namestats.vo.PollDetailVO;
import com.namestats.vo.PollMasterVO;

@Service
public class PollService {

    @Autowired
    private PollMapper pollMapper;

    @Transactional(rollbackFor = Exception.class)
    public void createPoll(PollMasterVO pollMasterVO, List<PollDetailVO> pollDetailList) {
    	//1. Poll Master
    	pollMapper.insertPollMaster(pollMasterVO);
    	
    	//2. Poll Detail
    	String pollDetailId = "";
    	
    	for(PollDetailVO pollDetailVO : pollDetailList) {
    		pollDetailVO.setPollMasterId(pollMasterVO.getPollMasterId());
    		pollDetailId = UUID.randomUUID().toString();
    		pollDetailVO.setPollDetailId(pollDetailId);
    		pollMapper.insertPollDetail(pollDetailVO);
    	}
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void doPoll(List<PollDetailVO> pollDetailList) {
    	for(PollDetailVO pollDetailVO : pollDetailList) {
    		pollMapper.updatePollItemCount(pollDetailVO);
    	}
    }
    
    public PollMasterVO getPollMasterByPollMasterId(String pollMasterId) {
    	return pollMapper.getPollMasterByPollMasterId(pollMasterId);
    }
    
    public List<PollDetailVO> getPollDetailByPollMasterId(String pollMasterId){
    	return pollMapper.getPollDetailByPollMasterId(pollMasterId);
    }
    
    public List<String> getPollMasterId(){
    	return pollMapper.getPollMasterId();
    }
    
}
