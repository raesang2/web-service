package com.namestats.controller;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namestats.common.Base64Util;
import com.namestats.dto.PollRequestDTO;
import com.namestats.service.PollService;
import com.namestats.vo.PollDetailVO;
import com.namestats.vo.PollMasterVO;

@RestController
@RequestMapping("/api")
public class PollController {

    @Autowired
    private PollService pollService;

    @PostMapping("/createPoll")
    public String createPoll(@ModelAttribute PollRequestDTO pollRequestDTO) {
    	PollMasterVO pollMasterVO = pollRequestDTO.getPollMasterVO();
    	List<PollDetailVO> pollDetailList = pollRequestDTO.getPollDetailList();    	

    	// master uuid 생성
    	String pollMasterId = UUID.randomUUID().toString();
    	pollMasterVO.setPollMasterId(pollMasterId);
    	pollService.createPoll(pollMasterVO, pollDetailList);
    	
    	String bas64pollMasterId = Base64Util.encodeBase64(pollMasterId);
        return "pollMasterId=" + bas64pollMasterId;
    }
    
    @PostMapping("/doPoll")
    public String doPoll(@ModelAttribute PollRequestDTO pollRequestDTO) {
    	PollMasterVO pollMasterVO = pollRequestDTO.getPollMasterVO();
    	List<PollDetailVO> pollDetailList = pollRequestDTO.getPollDetailList();
    	
    	String encodePollMasterId = pollMasterVO.getPollMasterId();
    	// base64 인코딩해서 들어온 id 값들 디코딩해서 전달
    	// 1. poll master id 세팅
        String decodePollMasterId = Base64Util.decodeBase64(encodePollMasterId);
        pollMasterVO.setPollMasterId(decodePollMasterId);
        pollRequestDTO.setPollMasterVO(pollMasterVO);
        
        // 2. poll detail id 세팅
        String decodePollDetailId = "";
        for(PollDetailVO pollDetailVO : pollDetailList) {
        	pollDetailVO.setPollMasterId(decodePollMasterId);
        	
        	decodePollDetailId = Base64Util.decodeBase64(pollDetailVO.getPollDetailId());
        	pollDetailVO.setPollDetailId(decodePollDetailId);
        }
        pollRequestDTO.setPollDetailList(pollDetailList);

    	pollService.doPoll(pollDetailList);
    	
        return "200";
    }
}

